package com.jia16.invest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.activity.LoginActivity;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.InvestUserId;
import com.jia16.util.AmountUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.PopupWindowUtils;
import com.jia16.util.TimeUtils;
import com.jia16.util.UrlHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 我要投资界面，--个体网贷--投资详情页面
 */
public class UnityLoanDetailActivity extends BaseActivity {

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
    private LinearLayout mllAddVoucher;//隐藏或显示（展示代金券规则的线性布局）


    private PopupWindow popupWindow;

    /**
     * 项目信息
     */
    private TextView mTvDescInformation;//项目描述
    private TextView mTvHouseLocation; //房产位置
    private TextView mTvBuildArea;//建筑面积
    private TextView mTvPrijectUser;//规划用途
    private TextView mTvAssessValue;//评估价值
    private LinearLayout mllHouseInformation;//房产信息总的布局
    private TextView mTvName;//姓名
    private TextView mTvSex;//性别
    private TextView mTvAge;//年龄
    private TextView mTvMarriageState;//婚姻状态
    private TextView mTvLoationCity;//所在城市
    private TextView mTvBrrow;//借款用途
    private LinearLayout mllDebtorDesc;//债务人描述的总的布局
    private TextView mTvRepaymentSafeguard;//还款保障
    private TextView mTvProjectRisk;//项目的风控措施
    private LinearLayout mllProjectInformation;//整个项目信息的线性布局
    private LinearLayout mllShowProjectInformation;//项目信息的线性布局，点击控制显示或隐藏项目信息


    /**
     * 其他信息
     */
    private TextView mTvPrijectLight; //项目亮点
    private TextView mTvShiftDate;//预计转入生效日期
    private TextView mTvInvestPeriod;//投资期间
    private TextView mTvRemoneyDate;//预计回款期
    private LinearLayout mllShowOtherInformation;//其他信息的线性布局，点击控制显示或隐藏项目信息
    private LinearLayout mllOtherContent;//其他信息的内容
    private TextView mTvProductTransfer;//产品转让
    private LinearLayout mllShowProductTransfer;//产品转让的线性布局


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity_loan_detail);

        Intent intent = getIntent();
        rightTitle = intent.getStringExtra("rightTitle");
        investId = intent.getIntExtra("investId", 0);
        useredId = intent.getIntExtra("useredId", 0);
        remainAmount = intent.getDoubleExtra("remainAmount", 0);

        //初始化数据
        initViews();


        if (BaseApplication.getInstance().isLogined()) {
            //表示已经登录，那么就去请求接口显示（可用余额）
            //获取用户投资标的信息
            postCurrentUserId();
        } else {
            //表示还没有登录,那么就显示（请登陆）
            mTvAllInvest.setText("请登录");
        }

        showLoadingDialog();
        //请求接口，获取投资标的详情的数据
        postInvestdetail();

        //绑定数据
        initDate();
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
        mllVoucher = (LinearLayout) findViewById(R.id.collapse_value);

        mTvVoucher = (TextView) findViewById(R.id.tv_voucher);
        //隐藏或显示（展示代金券规则的线性布局）
        mllAddVoucher = (LinearLayout) findViewById(R.id.ll_add_voucher);


        /**
         * 项目信息
         */
        //项目描述
        mTvDescInformation = (TextView) findViewById(R.id.tv_desc_information);
        //房产位置
        mTvHouseLocation = (TextView) findViewById(R.id.tv_house_location);
        //建筑面积
        mTvBuildArea = (TextView) findViewById(R.id.tv_build_area);
        //规划用途
        mTvPrijectUser = (TextView) findViewById(R.id.tv_prject_user);
        //评估价值
        mTvAssessValue = (TextView) findViewById(R.id.tv_assess_value);
        //房产信息总的布局
        mllHouseInformation = (LinearLayout) findViewById(R.id.ll_house_information);

        //姓名
        mTvName = (TextView) findViewById(R.id.tv_name);
        //性别
        mTvSex = (TextView) findViewById(R.id.tv_sex);
        //年龄
        mTvAge = (TextView) findViewById(R.id.tv_age);
        //婚姻状态
        mTvMarriageState = (TextView) findViewById(R.id.tv_marriage_state);
        //所在城市
        mTvLoationCity = (TextView) findViewById(R.id.tv_location_city);
        //借款用途
        mTvBrrow = (TextView) findViewById(R.id.tv_borrow);
        //债务人描述的总的布局
        mllDebtorDesc = (LinearLayout) findViewById(R.id.ll_debtor_desc);

        //还款保障
        mTvRepaymentSafeguard = (TextView) findViewById(R.id.tv_repayment_safeguard);
        //项目的风控措施
        mTvProjectRisk = (TextView) findViewById(R.id.tv_project_risk);
        //整个项目信息的线性布局
        mllProjectInformation = (LinearLayout) findViewById(R.id.ll_project_information);
        //项目信息的线性布局，点击控制显示或隐藏项目信息
        mllShowProjectInformation = (LinearLayout) findViewById(R.id.ll_show_project_information);
        mllShowProjectInformation.setOnClickListener(this);

        /**
         * 其他信息
         */
        //项目亮点
        mTvPrijectLight = (TextView) findViewById(R.id.tv_priject_light);
        //预计转入生效日期
        mTvShiftDate = (TextView) findViewById(R.id.tv_shift_date);
        //投资期间
        mTvInvestPeriod = (TextView) findViewById(R.id.tv_invest_period);
        //预计回款期
        mTvRemoneyDate = (TextView) findViewById(R.id.tv_remoney_date);
        //其他信息的线性布局，点击控制显示或隐藏项目信息
        mllShowOtherInformation = (LinearLayout) findViewById(R.id.ll_show_other_information);
        mllShowOtherInformation.setOnClickListener(this);
        //其他信息的内容
        mllOtherContent = (LinearLayout) findViewById(R.id.ll_other_content);
        //产品转让
        mTvProductTransfer = (TextView) findViewById(R.id.tv_product_transfer);
        //产品转让的线性布局
        mllShowProductTransfer = (LinearLayout) findViewById(R.id.ll_show_product_transfer);

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
                    if (transferableStartDays.equals("null")) {
                        //表示该标不能转让，那么就隐藏布局
                        mTvStartTransferDate.setVisibility(View.GONE);
                    } else {
                        //表示该标可以转让，那么就显示布局
                        mTvStartTransferDate.setVisibility(View.VISIBLE);
                        mTvStartTransferDate.setText("  (" + transferableStartDays + "天可转)");
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
                    String expiryDate = configJson.getString("expiryDate");
                    mTvExpireDate.setText(expiryDate);

                    //预计兑付日
                    mTvCashDate.setText("产品到期3个工作日内");

                    //  代金券抵扣的线性布局
                    String voucherPolicy = response.optString("voucherPolicy");
                    Lg.e("...voucherPolicy....", voucherPolicy);
                    if (!voucherPolicy.equals("null")) {
                        //表示该标可以使用代金券，显示代金券抵扣的线性布局
                        mllVoucher.setVisibility(View.VISIBLE);

                        JSONObject voucherPolicyJson = new JSONObject(voucherPolicy);
                        if (voucherPolicyJson.has("steps")) {
                            //表示有规律的按累加计算可以使用的代金券金额
                            JSONArray array = voucherPolicyJson.getJSONArray("steps");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject obj1 = array.getJSONObject(i);

                                String invest = obj1.getString("invest");
                                JSONObject investJson = new JSONObject(invest);
                                String investAmount = investJson.getString("amount");
                                Lg.e("......investAmount......", investAmount);

                                String voucher = obj1.getString("voucher");
                                JSONObject voucherJson = new JSONObject(voucher);
                                String voucherAmount = voucherJson.getString("amount");
                                Lg.e("......voucherAmount......", voucherAmount);

                                //动态的新建一个textview
                                final TextView textView = new TextView(UnityLoanDetailActivity.this);
                                textView.setTextSize(15);
                                textView.setTextColor(Color.parseColor("#333333"));
                                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                textView.setPadding(0, 5, 0, 5);
                                textView.setText("投资达到" + investAmount + "元可使用" + voucherAmount + "元代金券");

                                //动态的将textview添加到线性布局中
                                mllAddVoucher.addView(textView);

                            }


                            //代金券抵扣的线性布局需要展示第一条数据
                            JSONObject obj1 = array.getJSONObject(0);
                            String invest1 = obj1.getString("invest");
                            JSONObject invest1Json = new JSONObject(invest1);
                            String investAmount1 = invest1Json.getString("amount");
                            Lg.e("......investAmount1......", investAmount1);

                            String voucher1 = obj1.getString("voucher");
                            JSONObject voucher1Json = new JSONObject(voucher1);
                            String voucherAmount1 = voucher1Json.getString("amount");
                            Lg.e("......voucherAmount1......", voucherAmount1);
                            mTvVoucher.setText("满" + investAmount1 + "元可抵" + voucherAmount1 + "元");


                        } else {
                            //表示没有规律，代金券金额

                        }


                    } else {
                        //表示该标的不能使用代金券,那么就需要隐藏该线性布局
                        mllVoucher.setVisibility(View.GONE);
                    }

                    /**
                     * 项目信息
                     */
                    String loaneeInformation = response.optString("loaneeInformation");
                    JSONObject loaneeInformationJson = new JSONObject(loaneeInformation);
                    //项目描述
                    String subjectDesc = loaneeInformationJson.getString("subjectDesc");
                    mTvDescInformation.setText(subjectDesc);

                    if(loaneeInformationJson.has("housePropertyPosition")){
                        //表示有房产信息，那就需要显示房产信息布局
                        mllHouseInformation.setVisibility(View.VISIBLE);
                        //表示有债务人描述一栏，那么就显示
                        mllDebtorDesc.setVisibility(View.VISIBLE);
                        //房产位置
                        String housePropertyPosition = loaneeInformationJson.getString("housePropertyPosition");
                        if(TextUtils.isEmpty(housePropertyPosition)){
                            //表示为空，没有信息
                            mTvHouseLocation.setText("无");
                        }else {
                            mTvHouseLocation.setText(housePropertyPosition);
                        }
                    }else {
                        //表示没有房产信息，那就需要隐藏房产信息布局
                        mllHouseInformation.setVisibility(View.GONE);
                        //表示有债务人描述一栏，那么就显示
                        mllDebtorDesc.setVisibility(View.GONE);
                    }



                    //建筑面积
                    String coveredArea = loaneeInformationJson.getString("coveredArea");
                    if(TextUtils.isEmpty(coveredArea)){
                        mTvBuildArea.setText("无");
                    }else {
                        mTvBuildArea.setText(coveredArea);
                    }

                    //规划用途
                    String planningPurposes = loaneeInformationJson.getString("planningPurposes");
                    if(TextUtils.isEmpty(planningPurposes)){
                        mTvPrijectUser.setText("无");
                    }else {
                        mTvPrijectUser.setText(planningPurposes);
                    }


                    //评估价值
                    String evaluationValue = loaneeInformationJson.getString("evaluationValue");
                    if(TextUtils.isEmpty(evaluationValue)){
                        mTvAssessValue.setText("无");
                    }else {
                        mTvAssessValue.setText(evaluationValue);
                    }


                        //姓名
                        String debtorName = loaneeInformationJson.getString("debtorName");
                        if(TextUtils.isEmpty(debtorName)){
                            mTvName.setText("无");
                        }else {
                            String subName = debtorName.substring(0, 1);
                            String endName = "";
                            for (int i = 0; i < debtorName.length() - 1; i++) {
                                endName += "*";
                            }
                            endName = subName + endName;
                            mTvName.setText(endName);
                        }



                    //性别
                    String debtorSex = loaneeInformationJson.getString("debtorSex");
                    if(TextUtils.isEmpty(debtorSex)){
                        mTvSex.setText("无");
                    }else {
                        mTvSex.setText(debtorSex);
                    }

                    //年龄
                    String debtorAge = loaneeInformationJson.getString("debtorAge");
                    if(TextUtils.isEmpty(debtorAge)){
                        mTvAge.setText("无");
                    }else {
                        mTvAge.setText(debtorAge);
                    }

                    //婚姻状态
                    String debtorMaritalStatus = loaneeInformationJson.getString("debtorMaritalStatus");
                    if(TextUtils.isEmpty(debtorMaritalStatus)){
                        mTvMarriageState.setText("无");
                    }else {
                        mTvMarriageState.setText(debtorMaritalStatus);
                    }

                    //所在城市 mTvLoationCity
                    String debtorCity = loaneeInformationJson.getString("debtorCity");
                    if(TextUtils.isEmpty(debtorCity)){
                        mTvLoationCity.setText("无");
                    }else {
                        mTvLoationCity.setText(debtorCity);
                    }

                    //借款用途
                    String usage = loaneeInformationJson.getString("usage");
                    if(TextUtils.isEmpty(usage)){
                        mTvBrrow.setText("无");
                    }else {
                        mTvBrrow.setText(usage);
                    }

                    //还款保障
                    String repaymentEnsure = loaneeInformationJson.getString("repaymentEnsure");
                    if(TextUtils.isEmpty(repaymentEnsure)){
                        mTvRepaymentSafeguard.setText("无");
                    }else {
                        mTvRepaymentSafeguard.setText(repaymentEnsure);
                    }

                    //项目的风控措施
                    String riskMeasure = loaneeInformationJson.getString("riskMeasure");
                    if(TextUtils.isEmpty(riskMeasure)){
                        mTvProjectRisk.setText("无");
                    }else {
                        mTvProjectRisk.setText(riskMeasure);
                    }


                    /**
                     * 其他信息
                     */

                    //预计转入生效日期
                    String predicateValueDate = loaneeInformationJson.getString("predicateValueDate");
                    mTvShiftDate.setText(predicateValueDate);

                    //预计投资期间
                    String originalObligatoryRightEndDate = loaneeInformationJson.getString("originalObligatoryRightEndDate");
                    mTvInvestPeriod.setText(predicateValueDate+"--"+originalObligatoryRightEndDate);


                    JSONArray arrayed = loaneeInformationJson.getJSONArray("_customizedFields");
                    if(arrayed.length()!=0){
                        for (int i = 0; i < arrayed.length(); i++) {
                            Lg.e("...................", arrayed);
                            JSONObject objed = arrayed.getJSONObject(i);

                            String titleed = objed.getString("title");
                            if (titleed.equals("预计投资期间")) {
                                String contented = objed.getString("content");
                                mTvInvestPeriod.setText(contented);
                            } else if (titleed.equals("预计回款期")) {
                                String contenteds = objed.getString("content");
                                mTvRemoneyDate.setText(contenteds);
                            } else if (titleed.equals("产品转让")) {
                                String contentteds = objed.getString("content");
                                mTvProductTransfer.setText(contentteds);
                                mllShowProductTransfer.setVisibility(View.VISIBLE);
                            } else if (!titleed.equals("产品转让")) {
                                mllShowProductTransfer.setVisibility(View.GONE);
                            }

                        }
                    }else {
                        mllShowProductTransfer.setVisibility(View.GONE);
                    }

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

    private boolean isProjectInformationShow = false;//默认项目信息的状态
    private boolean isOtherInformation = false;//默认其他信息的状态

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_repayment_rule://还款方式
                //弹出弹框，显示还款方式
                // 一个自定义的布局，作为显示的内容
                View contentView = LayoutInflater.from(UnityLoanDetailActivity.this).inflate(
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
                View contentViews = LayoutInflater.from(UnityLoanDetailActivity.this).inflate(
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
                intent = new Intent(UnityLoanDetailActivity.this, SelectVoucherActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_show_project_information://隐藏或显示（展示代金券规则的线性布局）
                if (isProjectInformationShow) {
                    //表示需要显示项目信息
                    mllProjectInformation.setVisibility(View.VISIBLE);

                } else {
                    //表示需要隐藏项目信息
                    mllProjectInformation.setVisibility(View.GONE);
                }
                //更新显示状态
                isProjectInformationShow = !isProjectInformationShow;

                break;

            case R.id.ll_show_other_information://其他信息的线性布局，点击控制显示或隐藏项目信息
                if (isOtherInformation) {
                    //表示需要显示其他信息
                    mllOtherContent.setVisibility(View.VISIBLE);
                } else {
                    //表示需要隐藏其他信息
                    mllOtherContent.setVisibility(View.GONE);
                }
                //更新显示状态
                isOtherInformation = !isOtherInformation;
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
                    Intent intent = new Intent(UnityLoanDetailActivity.this, LoginActivity.class);
                    startActivityForResult(intent, INVESTLOGIN);
                }
            }
        });

    }

    private boolean isDown = false;

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
