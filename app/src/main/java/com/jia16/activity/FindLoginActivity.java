package com.jia16.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.util.AlertUtil;
import com.jia16.util.DMAlertUtil;
import com.jia16.util.FakeX509TrustManager;
import com.jia16.util.FormatUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.jia16.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 找回登录密码界面
 */
public class FindLoginActivity extends BaseActivity {

    private boolean mCountDowning;

    /**
     * 手机验证码倒计时
     */
    private CountDownTimer mVerifyCodeTimer;

    /**
     * 手机号码
     */
    private EditText mEtFindPhone;
    /**
     * 验证码输入框
     */
    private EditText mEtFindCode;
    /**
     * 获取验证码按钮
     */
    private Button mBtFindsmsCode;

    private SharedPreferences mSharedPreferences;

    /**
     * 倒计时所剩时间
     */
    private long mRemainTime = 60000L;

    /**
     * 最后停止时间
     */
    private long mLastTime;
    /**
     * 下一步按钮
     */
    private Button mBtNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_login);
        mSharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        initView();
        initTimer();
    }

    @Override
    protected void onDestroy() {

        if (mVerifyCodeTimer != null) {

            mLastTime = System.currentTimeMillis();
            mSharedPreferences.edit().putLong("findRemainTime", mRemainTime).putLong("findLastTime", mLastTime).commit();

            mVerifyCodeTimer.cancel();
        }
        super.onDestroy();
    }

    /**
     * 初始化页面元素
     */
    @Override
    protected void initView() {
        super.initView();

        ((TextView) findViewById(R.id.title_text)).setText("找回登录密码");
        //手机号码输入框
        mEtFindPhone = (EditText) findViewById(R.id.et_find_phone);

        //获取验证码按钮
        mBtFindsmsCode = (Button) findViewById(R.id.bt_findsms_code);
        mBtFindsmsCode.setOnClickListener(this);

        //验证码输入框
        mEtFindCode = (EditText) findViewById(R.id.et_find_code);

        //下一步按钮
        mBtNext = (Button) findViewById(R.id.bt_next);
        mBtNext.setOnClickListener(this);
    }

    private void initTimer() {

        mRemainTime = mSharedPreferences.getLong("findRemainTime", 0L);
        mLastTime = mSharedPreferences.getLong("findLastTime", 0);
        long currentTime = System.currentTimeMillis();
        mRemainTime = mRemainTime - (currentTime - mLastTime);
        if (mRemainTime > 0) {

            startDownTimer(mRemainTime);
        }
    }

    /**
     * 下一步按钮
     *
     * @param v
     */
    public void btnNext(View v) {

        //防重复点击
        if (checkClick(v.getId())) {

            String url = "/ums/users/recover/verification-sms";
            url = UrlHelper.getUrl(url);

            // 判断验证码和手机号是否正确
            if (checkParams(false)) {

                FakeX509TrustManager.allowAllSSL();
                if (Util.isNetworkAvailable(this)) {
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("smsVerifyCode", mEtFindCode.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    JSONObject obj = response;

                                    //验证成功就跳转到输入登录密码界面
                                    Intent intent = new Intent(FindLoginActivity.this, FindLoginPwdActivity.class);
                                    intent.putExtra("phoneNum",mEtFindPhone.getText().toString());
                                    startActivity(intent);
                                    finish();
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
                                        AlertUtil.showOneBtnDialog(FindLoginActivity.this, message, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });
                                    } else {
                                        AlertUtil.showOneBtnDialog(FindLoginActivity.this, "验证失败", new View.OnClickListener() {
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
                            headers.put("CSRF-TOKEN", sharedPreferences.getString("find_csrf", ""));
                            headers.put("Cookie", sharedPreferences.getString("findCookie", ""));
                            Lg.e("headers...Cookie",sharedPreferences.getString("findCookie", ""));
                            //              headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };
                    BaseApplication.getRequestQueue().add(jsonObjectRequest);
                } else {
                    AlertUtil.showOneBtnDialog(FindLoginActivity.this, "网络异常，请检查网络连接后重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_findsms_code:
                //获取验证码按钮
                getFindVerifyCode(view);
                break;

            case R.id.bt_next:
                //下一步按钮
                btnNext(view);
                break;
            default:
                break;
        }

    }


    /**
     * 获取验证码
     *
     * @param v
     */
    public void getFindVerifyCode(View v) {

        //防重复点击
        if (checkClick(v.getId())) {
            String url = "/ums/users/recover/notification";

            url = UrlHelper.getUrl(url);
            Lg.e("获取短信验证码",url);
            FakeX509TrustManager.allowAllSSL();
            if (checkParams(true)) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phone", mEtFindPhone.getText().toString());
                    jsonObject.put("channel","app");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Lg.e("response..获取验证码", response);

                                //下发验证码成功
                                DMAlertUtil.showOneBtnDialog(FindLoginActivity.this,
                                        "验证码已发送到您的手机，请查收！", false);
                                startDownTimer(60000L);


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            String errorMsg = new String(error.networkResponse.data);
                            JSONObject json = new JSONObject(errorMsg);
                            if (json != null) {
                                String message = (String) json.opt("message");
                                if (message != null) {
                                    AlertUtil.showOneBtnDialog(FindLoginActivity.this, message, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                } else {
                                    AlertUtil.showOneBtnDialog(FindLoginActivity.this, "获取验证码失败", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }) {
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        HashMap<String, String> headers = new HashMap<String, String>();
//                        return headers;
//                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("CSRF-TOKEN", sharedPreferences.getString("find_csrf", ""));
                        headers.put("Cookie", sharedPreferences.getString("findCookie", ""));
                        Lg.e("headers...Cookie",sharedPreferences.getString("findCookie", ""));
        //              headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };

                BaseApplication.getRequestQueue().add(stringRequest);
            }
        }
    }

    /**
     * 获取验证码和注册之前检查参数
     *
     * @param getVerifyCode true 获取验证码 false 注册
     * @return
     */
    private boolean checkParams(boolean getVerifyCode) {

        String phoneNum = mEtFindPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNum)) {

            //手机号码为空
            DMAlertUtil.showOneBtnDialog(FindLoginActivity.this, getString(R.string.find_pwd_phone_empty), false);
            return false;
        }

        if (!FormatUtil.isMobileNO(phoneNum)) {

            //手机号码格式不正确
            DMAlertUtil.showOneBtnDialog(FindLoginActivity.this, getString(R.string.find_pwd_phone_format_error), false);
            return false;
        }

        if (getVerifyCode) {

            return true;
        }

//        if (TextUtils.isEmpty(mUserId)) {
//
//            DMAlertUtil.showOneBtnDialog(FindLoginActivity.this, "您还没获取验证码，请先获取验证码", false);
//            return false;
//        }

        String verifyCode = mEtFindCode.getText().toString();
        if (TextUtils.isEmpty(verifyCode)) {

            DMAlertUtil.showOneBtnDialog(FindLoginActivity.this, getString(R.string.find_pwd_code_empty), false);
            return false;
        }

        if (verifyCode.length() < 6) {

            DMAlertUtil.showOneBtnDialog(FindLoginActivity.this, "验证码输入有误，请重新输入", false);
            return false;
        }

        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mCountDowning) {

                onPause();
            } else {

                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 开始倒计时
     *
     * @param time
     */
    private void startDownTimer(long time) {

        mVerifyCodeTimer = new CountDownTimer(time, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                mCountDowning = true;
                int temp = (int) (millisUntilFinished / 1000);
                mBtFindsmsCode.setText(getString(R.string.find_pwd_wait_time, temp));
                mBtFindsmsCode.setEnabled(false);
            }

            @Override
            public void onFinish() {

                mCountDowning = false;
                mBtFindsmsCode.setEnabled(true);
                mBtFindsmsCode.setText(getString(R.string.get_verify_code));
            }
        }.start();
    }
}
