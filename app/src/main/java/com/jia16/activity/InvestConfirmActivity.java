package com.jia16.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.UserInfo;
import com.jia16.util.AlertUtil;
import com.jia16.util.AmountUtil;
import com.jia16.util.Constants;
import com.jia16.util.Lg;
import com.jia16.util.ToastUtil;
import com.jia16.util.UrlHelper;
import com.jia16.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangjun on 16/8/17.
 */
public class InvestConfirmActivity extends BaseActivity {
//http://10.139.98.226/#!list?
// InvestConfirmVC
// &userid=5
// &subjectid=95
// &title=APP新手标测试2016-08-15
// &voucherid=d
// &amount=1000
// &canVoucher=undefined
// &voucherAmount=null

//    intent.putExtra("userid", params[1].split("=")[1]);
//    intent.putExtra("subjectid", params[2].split("=")[1]);
//    intent.putExtra("title", params[3].split("=")[1]);
//    intent.putExtra("voucherid", params[4].split("=")[1]);
//    intent.putExtra("amount", params[5].split("=")[1]);
//    intent.putExtra("canVoucher", params[6].split("=")[1]);
//    intent.putExtra("voucherAmount", params[7].split("=")[1]);

    private String userid = "";
    private String subjectid = "";
    private String title = "";
    private String voucherid = "";
    private String amount = "";
    private String canVoucher = "";
    private boolean canVoucherbool = false;
    private String voucherAmount = "";

    private TextView mBidNameTv;//标的名称
    private TextView mBidAmountTv;//投资金额
    private TextView mCouponAmountTv;//代金券抵扣金额
    private TextView mPaidAmountTv;//需要支付的金额tv

    private LinearLayout mPaidAmountLinear;//实付金额

    private LinearLayout mVoucherLinear;

    private Button mInvestBtn;//确认投资按钮

    private CheckBox mCheckBox;
    private long timeStamp;//单个时间戳

    private TextView mServiceBtn;

    private ImageView mBackBtn;

    private String agreeUrl;

    private JSONArray vouchers = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_confirm);
        initViews();
        initData();
    }

    private void initViews() {
        mBidNameTv = (TextView) findViewById(R.id.bid_name_tv);
        mBidAmountTv = (TextView) findViewById(R.id.bid_amount_tv);
        mCouponAmountTv = (TextView) findViewById(R.id.coupon_amount_tv);
        mPaidAmountTv = (TextView) findViewById(R.id.paid_amount_tv);
        mInvestBtn = (Button) findViewById(R.id.invest_confirm_btn);
        mInvestBtn.setOnClickListener(this);
        mCheckBox = (CheckBox) findViewById(R.id.agreement_check);
        mCheckBox.setChecked(false);
        mVoucherLinear = (LinearLayout) findViewById(R.id.voucher_linear);
        mPaidAmountLinear = (LinearLayout) findViewById(R.id.paid_amount_linear);
        mServiceBtn = (TextView) findViewById(R.id.btn_service_contact);
        mServiceBtn.setOnClickListener(this);
        mBackBtn = (ImageView) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_service_contact:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(Constants.HOME_PAGE + agreeUrl);
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.invest_confirm_btn:
                if (mCheckBox.isChecked()) {
                    AlertUtil.showDealOrLoginPwdDialog(this, false, "请输入交易密码", "请输入交易密码", new AlertUtil.SubmitListenerWithDialog() {
                        @Override
                        public void submit(String pwd, AlertDialog dialog) {
                            if (Util.isNetworkAvailable(InvestConfirmActivity.this)) {
                                submitPwd(pwd, dialog);
                            } else {
                                ToastUtil.getInstant().show(InvestConfirmActivity.this, "网络异常,请检查网络连接后重试");
                            }
                        }
                    });
                } else {
                    AlertUtil.showOneBtnDialog(InvestConfirmActivity.this, "请先同意《网站投资协议》", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    private void submitPwd(String pwd, final AlertDialog dialog) {
        timeStamp = System.currentTimeMillis();
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo == null) {
            AlertUtil.showOneBtnDialog(this, "session已过期,请重新登录", null);
            return;
        }
        String url = "/ums/users/" + userInfo.getId() + "/deal-password/verification";
        url = UrlHelper.getUrl(url);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timestamp", String.valueOf(timeStamp));
            jsonObject.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Lg.e("verification", "验证交易密码成功");
                        dialog.cancel();
                        confirmInvest();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                String errorMsg = new String(error.networkResponse.data);
                try {
                    JSONObject json = new JSONObject(errorMsg);
                    if (json != null) {
                        String message = (String) json.opt("message");
                        if (message != null) {
                            AlertUtil.showOneBtnDialog(InvestConfirmActivity.this, message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        } else {
                            AlertUtil.showOneBtnDialog(InvestConfirmActivity.this, "交易密码错误", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("_csrf", "");
                String cookie = sharedPreferences.getString("Cookie", "");
                Lg.e("token", token, "cookie", cookie);
                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };
        BaseApplication.getRequestQueue().add(stringRequest);
    }


    private void confirmInvest() {
        String url = "/api/users/" + userid + "/subjects/" + subjectid + "/investment-requests";
        url = UrlHelper.getUrl(url);
        JSONObject jsonObject = new JSONObject();
        JSONObject dic = new JSONObject();
        try {
            dic.put("amount", amount);
            dic.put("currency", "CNY");
            if (canVoucherbool) {
                jsonObject.put("vouchers", vouchers);
            }
            jsonObject.put("investmentAmount", dic);
            jsonObject.put("timestamp", String.valueOf(timeStamp));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        AlertUtil.showOneBtnDialog(InvestConfirmActivity.this, "恭喜您，投资申请已成功!", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = getIntent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                try {
                    String errorMsg = new String(error.networkResponse.data);
                    JSONObject json = new JSONObject(errorMsg);
                    if (json != null) {
                        String message = (String) json.opt("message");
                        if (message != null) {
                            AlertUtil.showOneBtnDialog(InvestConfirmActivity.this, message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        } else {
                            AlertUtil.showOneBtnDialog(InvestConfirmActivity.this, "投资失败", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("_csrf", "");
                String cookie = sharedPreferences.getString("Cookie", "");
                Lg.e("token", token, "cookie", cookie);
                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };
        BaseApplication.getRequestQueue().add(stringRequest);

    }

    private void initData() {
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        subjectid = intent.getStringExtra("subjectid");
        title = intent.getStringExtra("title");
        voucherid = intent.getStringExtra("voucherid");
        amount = intent.getStringExtra("amount");
        canVoucher = intent.getStringExtra("canVoucher");
        voucherAmount = intent.getStringExtra("voucherAmount");
        agreeUrl = intent.getStringExtra("agreeUrl");

        mBidNameTv.setText(title);
        mBidAmountTv.setText(AmountUtil.format(Double.valueOf(amount)) + "元");
        double needPayAmout = Double.valueOf(0d);
        if ("y".equals(canVoucher)) {
            canVoucherbool = true;
        }
        if (canVoucherbool && TextUtils.isDigitsOnly(voucherAmount)) {//有代金券
            mCouponAmountTv.setText("-" + AmountUtil.format(Double.valueOf(voucherAmount)) + "元");
        } else {
            mCouponAmountTv.setText("未使用");
        }
        double amountD;
        try {
            amountD = Double.valueOf(amount);
        } catch (NumberFormatException e) {
            amountD = Double.valueOf(0d);
        }
        double couponAmount;
        try {
            couponAmount = Double.valueOf(voucherAmount);
        } catch (NumberFormatException e) {
            couponAmount = Double.valueOf(0d);
        }
        mPaidAmountTv.setText(AmountUtil.format(amountD - couponAmount) + "元");
        if (couponAmount > Double.valueOf(0d)) {//使用了代金券
            mPaidAmountTv.setTextColor(getResources().getColor(R.color.main_color));
        }

        //拼接vouchers
        if (!TextUtils.isEmpty(voucherid)) {
            if (voucherid.contains(",")) {
                String[] voucherStrings = voucherid.split(",");
                if (voucherStrings != null && voucherStrings.length > 0) {
                    for (String s : voucherStrings) {
                        if (TextUtils.isDigitsOnly(s)) {
                            vouchers.put(Integer.valueOf(s));
                        }
                    }
                }
            } else {
                if (TextUtils.isDigitsOnly(voucherid)) {
                    vouchers.put(Integer.valueOf(voucherid));
                }
            }
        }
        if (canVoucherbool) {
            mVoucherLinear.setVisibility(View.VISIBLE);
            findViewById(R.id.voucher_divider).setVisibility(View.VISIBLE);
            mPaidAmountLinear.setVisibility(View.VISIBLE);
            findViewById(R.id.paid_amount_divider).setVisibility(View.VISIBLE);
        } else {
            mVoucherLinear.setVisibility(View.GONE);
            findViewById(R.id.voucher_divider).setVisibility(View.GONE);
            mPaidAmountLinear.setVisibility(View.GONE);
            findViewById(R.id.paid_amount_divider).setVisibility(View.GONE);

        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
