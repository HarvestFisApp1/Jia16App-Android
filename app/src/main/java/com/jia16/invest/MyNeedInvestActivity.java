package com.jia16.invest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.activity.LoginActivity;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.InvestUserId;
import com.jia16.bean.Returnmoney;
import com.jia16.util.AmountUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.PopupWindowUtils;
import com.jia16.util.TimeUtils;
import com.jia16.util.UrlHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 我要投资界面，----投资详情
 */
public class MyNeedInvestActivity extends BaseActivity {

    private static final int INVESTLOGIN = 1001;
    /**
     * 开启这个activity的界面传递过来的数据
     */
    private String rightTitle;//标的类型
    private int investId;//投资标的id
    private int useredId;//用户的id
    private double remainAmount;//剩余投资金额


    //获取数据
    private double annualsRate;
    //投资天数
    private String investCount;
    private double getEarn;//实时计算得出的应得收益
    private int userInvestId;//用户投资id
    private double availableAmountted;//获取用户可用余额

    /**
     * 初始化
     */
    private TextView mTitleText;//投资标的名称
    private ImageView mBtnBack; //返回键按钮
    private TextView mTvTitleRight;//标题栏右边的子标题
    private TextView mTvRepaymentRule;//还款方式
    private LinearLayout mllWoucherUserRule;//代金券使用规则的线性布局
    private LinearLayout mllSelectVoucher;//请选择代金券的线性布局
    private TextView mTvyearEarn;//年化收益
    private TextView mTvProjectDesc;//标的描述
    private TextView mTvRemaianInvestAmount;//剩余投资金额
    private TextView mTvInvestDate;//投资期限
    private TextView mTvStartTransferDate;//是否可以转让，转让天数
    private TextView mTvStartAmount;//起投金额
    private TextView mTvInvestQuota;//投资限额
    private LinearLayout mllInvestQuota;//投资限额的线性布局

    private TextView mTvStartInterestDate;//预计起息日
    private TextView mTvExpireDate;//预计到期日
    private TextView mTvCashDate;//预计兑付日
    private EditText mEtInvestAmount;//投资金额输入框
    private TextView mTvExpectEarn;//预期收益
    private TextView mTvAllInvest;//可用余额（全投按钮

    private LinearLayout mllVoucher;//代金券抵扣的线性布局
    private TextView mTvVoucher;//显示代金券的使用规则
    private ImageView mIvVoucherArrow;//展开显示代金券的使用规则的箭头按钮


    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_need_invest);

        Intent intent = getIntent();
        rightTitle = intent.getStringExtra("rightTitle");
        investId = intent.getIntExtra("investId", 0);
        useredId = intent.getIntExtra("useredId", 0);
        remainAmount = intent.getDoubleExtra("remainAmount", 0);

        showLoadingDialog();
        //请求接口，获取投资标的详情的数据
        postInvestdetail();

        //初始化数据
        initViews();

        //绑定数据
        initDate();

        if (BaseApplication.getInstance().isLogined()) {
            //表示已经登录，那么就去请求接口显示（可用余额）
            //获取用户投资标的信息
            postCurrentUserId();
        } else {
            //表示还没有登录,那么就显示（请登陆）
            mTvAllInvest.setText("请登录");
        }

    }

    /**
     * 获取用户投资标的信息
     */
    private void postCurrentUserId() {
        showLoadingDialog();
        String url = "/api/users/current";
        url = UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dimissLoadingDialog();

                String accounts = response.optString("accounts");

                ArrayList<InvestUserId> infos = (ArrayList<InvestUserId>) JsonUtil.parseJsonToList(accounts, new TypeToken<List<InvestUserId>>() {
                }.getType());

                for (int i = 0; i < infos.size(); i++) {
                    if (infos.get(i).getDescriptionType().equals("INVESTMENT")) {
                        userInvestId = infos.get(i).getId();
                        //Lg.e(".........investId...........", investId);
                    }
                }

                //获取用户的可用余额
                postUserAvailableAmount();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dimissLoadingDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        BaseApplication.getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * 获取用户的可用余额
     */
    private void postUserAvailableAmount() {
        String url = "/pay/lanmao/form/QUERY_USER_INFORMATION.json?platformUserNo=" + userInvestId;
        url = UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //获取用户可用余额
                String availableAmount = response.optString("availableAmount");
                availableAmountted = Double.parseDouble(availableAmount);
                String availableAmounts = AmountUtil.addComma(AmountUtil.DT.format(Double.parseDouble(availableAmount)));
                mTvAllInvest.setText(availableAmounts + "全投");
                Lg.e("..............获取数据成功", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        BaseApplication.getRequestQueue().add(jsonObjectRequest);
    }


    /**
     * 初始化
     */
    private void initViews() {
        mTitleText = ((TextView) findViewById(R.id.title_text));
        mBtnBack = (ImageView) findViewById(R.id.btn_back);
        mTvTitleRight = (TextView) findViewById(R.id.tv_title_right);
        mTvRepaymentRule = (TextView) findViewById(R.id.tv_repayment_rule);
        mTvRepaymentRule.setOnClickListener(this);

        ////代金券使用规则的线性布局
        mllWoucherUserRule = (LinearLayout) findViewById(R.id.ll_woucher_user_rule);
        mllWoucherUserRule.setOnClickListener(this);

        //请选择代金券的线性布局
        mllSelectVoucher = (LinearLayout) findViewById(R.id.ll_select_voucher);
        mllSelectVoucher.setOnClickListener(this);
        //年化收益
        mTvyearEarn = (TextView) findViewById(R.id.tv_year_earn);
        //标的描述
        mTvProjectDesc = (TextView) findViewById(R.id.tv_project_desc);
        //剩余投资金额
        mTvRemaianInvestAmount = (TextView) findViewById(R.id.tv_remain_invest_amount);
        //投资期限
        mTvInvestDate = (TextView) findViewById(R.id.tv_invest_date);
        //是否可以转让，转让天数
        mTvStartTransferDate = (TextView) findViewById(R.id.tv_start_transfer_date);

        //起投金额
        mTvStartAmount = (TextView) findViewById(R.id.tv_start_amount);
        //投资限额
        mTvInvestQuota = (TextView) findViewById(R.id.tv_invest_quota);
        mllInvestQuota = (LinearLayout) findViewById(R.id.ll_invest_quota);

        //预计起息日
        mTvStartInterestDate = (TextView) findViewById(R.id.tv_start_interest_date);
        //预计到期日
        mTvExpireDate = (TextView) findViewById(R.id.tv_expire_date);
        //预计兑付日
        mTvCashDate = (TextView) findViewById(R.id.tv_cash_date);

        //投资金额输入框
        mEtInvestAmount = (EditText) findViewById(R.id.et_invest_amount);
        //预期收益
        mTvExpectEarn = (TextView) findViewById(R.id.tv_expect_earn);

        //可用余额（全投按钮）
        mTvAllInvest = (TextView) findViewById(R.id.tv_all_invest);

        //代金券抵扣的线性布局
        mllVoucher = (LinearLayout) findViewById(R.id.ll_voucher);
        mTvVoucher = (TextView) findViewById(R.id.tv_voucher);
        mIvVoucherArrow = (ImageView) findViewById(R.id.iv_voucher_arrow);

    }


    /**
     * 请求接口，获取投资标的详情的数据
     */
    private void postInvestdetail() {
        long currentTimeMillis = System.currentTimeMillis();
        String url = "/api/users/" + useredId + "/subjects/" + investId + "?timestamp=" + currentTimeMillis;
        url = UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                dimissLoadingDialog();

                Lg.e("获取投资标的详情的数据", response);
                //获取投资标的名称
                String title = response.optString("title");
                mTitleText.setText(title);

                try {
                    //获取年化收益率
                    String instalmentPolicy = response.optString("instalmentPolicy");
                    JSONObject instalmentPolicyJson = new JSONObject(instalmentPolicy);
                    String annualRate = instalmentPolicyJson.getString("annualRate");
                    annualsRate = Double.parseDouble(annualRate);
                    double annualrate = annualsRate * 100;
                    String annualRates = AmountUtil.addComma(AmountUtil.DT.format(annualrate));
                    mTvyearEarn.setText(annualRates + " %");

                    //标的描述
                    String config = response.optString("config");
                    JSONObject configJson = new JSONObject(config);
                    String tagName = configJson.getString("tagName");
                    mTvProjectDesc.setText(tagName);

                    //投资期限
                    String interval = instalmentPolicyJson.getString("interval");
                    JSONObject intervalJson = new JSONObject(interval);
                    investCount = intervalJson.getString("count");
                    mTvInvestDate.setText(investCount + " 天");

                    //是否可以转让，转让天数
                    String transferableStartDays = response.optString("transferableStartDays");
                    if(transferableStartDays.equals("null")){
                        //表示该标不能转让，那么就隐藏布局
                        mTvStartTransferDate.setVisibility(View.GONE);
                    }else {
                        //表示该标可以转让，那么就显示布局
                        mTvStartTransferDate.setVisibility(View.VISIBLE);
                        mTvStartTransferDate.setText("  ("+transferableStartDays+"天可转)");
                    }


                    //起投金额
                    String investmentPolicy = response.optString("investmentPolicy");
                    JSONObject investmentPolicyJson = new JSONObject(investmentPolicy);
                    String minimumInvestmentAmount = investmentPolicyJson.getString("minimumInvestmentAmount");
                    JSONObject minimumInvestmentAmountJson = new JSONObject(minimumInvestmentAmount);
                    String startAmount = minimumInvestmentAmountJson.getString("amount");
                    double startamount = Double.parseDouble(startAmount);
                    mTvStartAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(startamount)) + " 元");

                    //代金券抵扣

                    //投资限额
                    if (investmentPolicyJson.has("maximumInvestmentAmount")) {
                        String maximumInvestmentAmount = investmentPolicyJson.getString("maximumInvestmentAmount");
                        JSONObject maximumInvestmentAmountJson = new JSONObject(maximumInvestmentAmount);
                        String quotaAmount = maximumInvestmentAmountJson.getString("amount");
                        double quotaAmounted = Double.parseDouble(quotaAmount);
                        mTvInvestQuota.setText(AmountUtil.addComma(AmountUtil.DT.format(quotaAmounted)) + " 元");
                        mllInvestQuota.setVisibility(View.VISIBLE);
                    } else {
                        mllInvestQuota.setVisibility(View.GONE);
                    }


                    //预计起息日
                    String passDays = configJson.getString("passDays");
                    int dates = Integer.parseInt(passDays);
                    Lg.e("........dates.........", dates);
                    long currentTimeMillis = System.currentTimeMillis() + 86400000 * dates;
                    String startTime = TimeUtils.getTime(currentTimeMillis, TimeUtils.DATE_FORMAT_DATE);
                    mTvStartInterestDate.setText(startTime);

                    //预计到期日
                    String financialAssetsHeldTerminationDate = configJson.getString("financialAssetsHeldTerminationDate");
                    mTvExpireDate.setText(financialAssetsHeldTerminationDate);

                    //预计兑付日
                    long assignDate = TimeUtils.getAssignDate(financialAssetsHeldTerminationDate);
                    String graceDays = configJson.getString("graceDays");
                    int graceDayed = Integer.parseInt(graceDays);
                    long cashDateMillis = assignDate + 86400000 * graceDayed;
                    String cashTime = TimeUtils.getTime(cashDateMillis, TimeUtils.DATE_FORMAT_DATE);
                    mTvCashDate.setText("不晚于" + cashTime);

                    //  代金券抵扣的线性布局
                    String voucherPolicy = response.optString("voucherPolicy");
                    Lg.e("...voucherPolicy....",voucherPolicy);
                    if(!voucherPolicy.equals("null")){
                        //表示该标可以使用代金券
                        mllVoucher.setVisibility(View.VISIBLE);

                        JSONObject voucherPolicyJson = new JSONObject(voucherPolicy);
                        if(voucherPolicyJson.has("steps")){
                            //表示有规律的按累加计算可以使用的代金券金额
                            JSONArray array = voucherPolicyJson.getJSONArray("steps");

                            for(int i = 0; i < array.length(); i++){

                                JSONObject obj1 =  array.getJSONObject(i);

                                String invest = obj1.getString("invest");
                                JSONObject investJson = new JSONObject(invest);
                                String amount = investJson.getString("amount");
                                Lg.e("......amount......",amount);

                            }

                        }else {

                        }


                    }else {
                        //表示该标的不能使用代金券,那么就需要隐藏该线性布局
                        mllVoucher.setVisibility(View.GONE);
                    }


                    //Lg.e("........cashTime.........", cashTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dimissLoadingDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        BaseApplication.getRequestQueue().add(jsonObjectRequest);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_repayment_rule://还款方式
                //弹出弹框，显示还款方式
                // 一个自定义的布局，作为显示的内容
                View contentView = LayoutInflater.from(MyNeedInvestActivity.this).inflate(
                        R.layout.repayment_rule_popwindow, null);
                //弹出使用规则的弹框
                popupWindow = PopupWindowUtils.showPopupWindow(contentView, 38);

                ImageView mIvButton = (ImageView) contentView.findViewById(R.id.iv_button);

                mIvButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }
                });
                //显示popupWindow弹窗
                popupWindow.showAsDropDown(contentView);
                break;

            case R.id.ll_woucher_user_rule://代金券使用规则的线性布局，弹出框
                // 一个自定义的布局，作为显示的内容
                View contentViews = LayoutInflater.from(MyNeedInvestActivity.this).inflate(
                        R.layout.pop_window, null);
                //弹出使用规则的弹框
                popupWindow = PopupWindowUtils.showPopupWindow(contentViews, 38);

                ImageView mIvButtons = (ImageView) contentViews.findViewById(R.id.iv_button);

                mIvButtons.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                    }
                });
                //显示popupWindow弹窗
                popupWindow.showAsDropDown(contentViews);

                break;

            case R.id.ll_select_voucher://请选择代金券的线性布局
                intent = new Intent(MyNeedInvestActivity.this, SelectVoucherActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        super.onClick(view);
    }


    /**
     * 绑定数据
     */
    private void initDate() {
        mTvTitleRight.setText(rightTitle);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvTitleRight.setVisibility(View.VISIBLE);
        mTvTitleRight.setTextColor(getResources().getColor(R.color.white));
        mTvTitleRight.setBackgroundColor(getResources().getColor(R.color.invest_object_state));

        //剩余投资金额
        mTvRemaianInvestAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(remainAmount)) + "元");

        //投资金额输入框
        //设置editText的监听，动态设置textview显示的文本
        mEtInvestAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String ss = mEtInvestAmount.getText().toString();
                if (!TextUtils.isEmpty(ss)) {
                    double investAmount = Double.parseDouble(ss);
                    //计算应得收益=年化收益*投资天数*投资金额/360
                    getEarn = (annualsRate * Integer.parseInt(investCount) * investAmount) / 360;
                    //Lg.e("...............",shenyued+"..."+annuals+"...."+investAmount);
                    // Lg.e("实时数据。。。。。",getEarn);
                    mTvExpectEarn.setText(AmountUtil.addComma(AmountUtil.DT.format(getEarn)));
                } else {
                    mTvExpectEarn.setText(AmountUtil.addComma(AmountUtil.DT.format(0)));
                }

            }
        });


        //全投按钮（如果没有登录，那么就先跳转到登录）
        mTvAllInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseApplication.getInstance().isLogined()) {
                    //表示已经登录，那么点击就（把可用余额全投）
                    mEtInvestAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(availableAmountted)));
                    mEtInvestAmount.setSelection(mEtInvestAmount.getText().length());
                } else {
                    //表示还没有登录，点击跳转到登录界面
                    Intent intent = new Intent(MyNeedInvestActivity.this, LoginActivity.class);
                    startActivityForResult(intent, INVESTLOGIN);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == INVESTLOGIN) {
                //登录完成后返回，那么就需要请求可用余额的数据
                //获取用户投资标的信息
                postCurrentUserId();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
