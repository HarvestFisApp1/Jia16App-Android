package com.jia16.assets;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.util.AmountUtil;
import com.jia16.util.Lg;
import com.jia16.util.ToastUtil;
import com.jia16.util.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的资产--申请转让--界面
 */
public class ApplyTransferActivity extends BaseActivity {
    /**
     * 是持有中（holdForHolder）界面传递过来的数据
     */
    private int usersID;
    private int subjectId;
    private int investsId;

    /**
     * 数据展示
     */
    private TextView mtvProjectName;//项目名称
    private TextView mTvMinInvestAmount;//起投金额
    private TextView mTvHoldAmount;//持有金额
    private TextView mTvCanTransferAmount;//可转让金额
    private TextView mTvYearEarn;//预期年化收益
    private TextView mTvResidueDate;//剩余天数
    private EditText mEtAmount;//输入转让本金数额
    private TextView mTvAllTransfer;//全转按钮
    private TextView mTvDueEarn;//显示应得收益
    private Button mBtNext;//下一步按钮

    /**
     * 请求接口获取的数据
     */
    private double transfermount;//可转让金额
    private double minInvestAmount; //起投金额
    private String title;//标的名称

    private String annualRate;//年化收益

    //输入框中的转让金额
    private double getTransferAmount;
    private double getEarn;//实时计算得出的应得收益

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_fransfer);

        Intent intent = getIntent();
        //是持有中（holdForHolder）界面传递过来的数据
        usersID = intent.getIntExtra("usersID", 0);
        subjectId = intent.getIntExtra("subjectId", 0);
        investsId = intent.getIntExtra("investsId", 0);

        //初始化布局
        initView();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        ((TextView) findViewById(R.id.title_text)).setText("原始标的信息");

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mtvProjectName = (TextView) findViewById(R.id.tv_project_name);
        mTvMinInvestAmount = (TextView) findViewById(R.id.tv_min_invest_amount);
        mTvHoldAmount = (TextView) findViewById(R.id.tv_hold_amount);
        mTvCanTransferAmount = (TextView) findViewById(R.id.tv_can_transfer_amount);
        mTvYearEarn = (TextView) findViewById(R.id.tv_year_earn);
        mTvResidueDate = (TextView) findViewById(R.id.tv_residue_date);
        mEtAmount = (EditText) findViewById(R.id.et_amount);
        mTvAllTransfer = (TextView) findViewById(R.id.tv_all_transfer);
        mTvDueEarn = (TextView) findViewById(R.id.tv_due_earn);
        mBtNext = (Button) findViewById(R.id.bt_next);

        //请求需要申请标的数据
        postApplyFransferData();

    }

    /**
     * 请求需要申请标的数据
     */
    private void postApplyFransferData() {
        showLoadingDialog();

        String url="/api/users/"+usersID+"/subjects/"+subjectId;
        url= UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                dimissLoadingDialog();

                try {
                    String contract = response.optString("contract");
                    JSONObject contractJson=new JSONObject(contract);
                    String subject = contractJson.getString("subject");
                    JSONObject subjectJson=new JSONObject(subject);
                    //获取标的名称
                    title = subjectJson.getString("title");
                    if(title!=null){
                        mtvProjectName.setText(title);
                    }

                    //获取起投金额
                    String investmentPolicy = subjectJson.getString("investmentPolicy");
                    JSONObject investmentPolicyJson=new JSONObject(investmentPolicy);
                    String minimumInvestmentAmount = investmentPolicyJson.getString("minimumInvestmentAmount");
                    JSONObject minimumInvestmentAmountJson=new JSONObject(minimumInvestmentAmount);
                    String amount = minimumInvestmentAmountJson.getString("amount");
                    minInvestAmount = Double.parseDouble(amount);
                    mTvMinInvestAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(minInvestAmount))+"元");

                    //持有金额


                    //可转让金额
                    String minimumTransferableAmount = subjectJson.getString("minimumTransferableAmount");
                    JSONObject minimumTransferableAmountJson=new JSONObject(minimumTransferableAmount);
                    final String transferAmount = minimumTransferableAmountJson.getString("amount");
                    transfermount = Double.parseDouble(transferAmount);//可转让金额
                    mTvCanTransferAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(transfermount))+"元");

                    //预期年化收益
                    final String instalmentPolicy = subjectJson.getString("instalmentPolicy");
                    JSONObject instalmentPolicyJson=new JSONObject(instalmentPolicy);
                    String annual = instalmentPolicyJson.getString("annualRate");
                    final double annuals = Double.parseDouble(annual);
                    double annualed = annuals*100;
                    annualRate = AmountUtil.addComma(AmountUtil.DT.format(annualed));
                    mTvYearEarn.setText(annualRate+"%");

                    //剩余天数
                    String residualDays = subjectJson.getString("residualDays");
                    mTvResidueDate.setText(residualDays);

                    //全转按钮
                    mTvAllTransfer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mEtAmount.setText(transferAmount);
                            //将光标设置为最后
                            mEtAmount.setSelection(mEtAmount.getText().length());
                        }
                    });

                    //应得收益显示
                    //获取总的投资期限
                    String interval = instalmentPolicyJson.getString("interval");
                    JSONObject intervalJson=new JSONObject(interval);
                    String count = intervalJson.getString("count");
                    double acountDate = Double.parseDouble(count);//总投资期限
                    //获取已经已有的投资期限=总投资期限-剩余投资期限
                    String shenyu = residualDays.replace("天", "");
                    final double shenyued = Double.parseDouble(shenyu);//剩余天数
                    final double investOkDate = (acountDate-shenyued);//已投资期限
                    //动态计算应得收益
                    //Lg.e("已投资日期.................",investOkDate);

                    //设置editText的监听，动态设置textview显示的文本
                    mEtAmount.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            String ss = mEtAmount.getText().toString();
                            if(!TextUtils.isEmpty(ss)){
                                double investAmount = Double.parseDouble(ss);
                                //计算应得收益=年化收益*已投资天数*投资金额/360
                                getEarn = (investOkDate * annuals * investAmount) / 360;
                                //Lg.e("...............",shenyued+"..."+annuals+"...."+investAmount);
                                // Lg.e("实时数据。。。。。",getEarn);
                                mTvDueEarn.setText(AmountUtil.addComma(AmountUtil.DT.format(getEarn)));
                            }else {
                                mTvDueEarn.setText(0+"");
                            }

                        }
                    });

                    //下一步按钮
                    mBtNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //请求网络，获取转让该标的转让手续费
                            postTrandferCharge();

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dimissLoadingDialog();
            }
        }){
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


    /**
     * 请求网络，获取转让该标的转让手续费
     */
    private void postTrandferCharge() {
        String url="/api/biz-params/transfer.fee";
        url=UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //转让手续费
                String value = response.optString("value");
                Lg.e("转让手续费",value);
                //点击按钮时检查参数
                if(checkParams(false)){
                    //下一步后，跳转到转让补充界面
                    Intent intent=new Intent(ApplyTransferActivity.this,ApplyTransferNextActivity.class);
                    //转让本金
                    intent.putExtra("transferPrincipal",getTransferAmount);//获取输入框中的转让金额
                    //应得收益
                    intent.putExtra("dueEarn",getEarn);//获取textview中的收益
                    //收益率
                    intent.putExtra("annualRate",annualRate);
                    //转让手续费
                    intent.putExtra("transferCharge",value);
                    //标的名称
                    intent.putExtra("title",title);
                    //投资的id
                    intent.putExtra("investsId",investsId);
                    //subjectid
                    intent.putExtra("subjectId",subjectId);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dimissLoadingDialog();
            }
        }){
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


    /**
     * 点击下一步前，检查转让本金数额是否正确
     */
    private boolean checkParams(boolean getVerifyCode) {

        String TransferAmount = mEtAmount.getText().toString();


        if (TextUtils.isEmpty(TransferAmount)) {

            //转让本金数额不能为空
            //DMAlertUtil.showOneBtnDialog(ApplyTransferActivity.this,"转让本金数额不能为空",false);
            ToastUtil.getInstant().show(ApplyTransferActivity.this,"转让本金数额不能为空");
            return false;
        }
        //获取输入框中的转让金额
        getTransferAmount = Double.parseDouble(TransferAmount);

        if (getTransferAmount>transfermount) {

            //DMAlertUtil.showOneBtnDialog(ApplyTransferActivity.this, "最高不能超过可转让金额", false);
            ToastUtil.getInstant().show(ApplyTransferActivity.this,"最高不能超过可转让金额");
            return false;
        }

        if (getTransferAmount<minInvestAmount) {

            //DMAlertUtil.showOneBtnDialog(ApplyTransferActivity.this, "剩余金额不能小于起投金额", false);
            ToastUtil.getInstant().show(ApplyTransferActivity.this,"剩余金额不能小于起投金额");
            return false;
        }

        return true;
    }
}
