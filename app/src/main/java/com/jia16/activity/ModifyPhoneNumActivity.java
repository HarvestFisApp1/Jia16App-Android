package com.jia16.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.jia16.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改手机号页面
 */
public class ModifyPhoneNumActivity extends BaseActivity {

    private boolean mCountDowning;

    /**
     * 手机验证码倒计时
     */
    private CountDownTimer mVerifyCodeTimer;

    /**
     * 验证码输入框
     */
    private EditText mEtModifyPhoneCode;

    /**
     * 获取验证码按钮
     */
    private Button mBtnModifyPhone;


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
     * 我的账户界面传递过来的电话号码
     */
    private String modify_phone;
    /**
     * 我的账户界面传递过来的用户id
     */
    private String userId;
    /**
     * 需要修改的手机号码
     */
    private TextView mTvPhone;

    /**
     * 下一步按钮
     */
    private Button mBtModifyPhoneNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        mSharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        initView();
        initTimer();
    }

    @Override
    protected void onDestroy() {

        if (mVerifyCodeTimer != null) {

            mLastTime = System.currentTimeMillis();
            mSharedPreferences.edit().putLong("registerRemainTime", mRemainTime).putLong("registerLastTime", mLastTime).commit();

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

        Intent intent = getIntent();
        modify_phone = intent.getStringExtra("modify_phone");
        userId = intent.getStringExtra("userId");

        ((TextView) findViewById(R.id.title_text)).setText("修改手机号");


        //需要修改的手机号码
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvPhone.setText(modify_phone);

        //验证码输入框
        mEtModifyPhoneCode = (EditText) findViewById(R.id.et_modify_phone_code);

        //获取验证码按钮
        mBtnModifyPhone = (Button) findViewById(R.id.btn_modify_phone);
        mBtnModifyPhone.setOnClickListener(this);

        //下一步按钮
        mBtModifyPhoneNext = (Button) findViewById(R.id.bt_modify_phone_next);
        mBtModifyPhoneNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //防止重复点击
        if (checkClick(view.getId())) {
            switch (view.getId()) {
                case R.id.btn_modify_phone://获取验证码按钮
                    getModifyPhoneCode(view);
                    break;
                case R.id.bt_modify_phone_next://下一步按钮
                    ModifyPhoneNext(view);
                break;
                default:
                    break;
            }
        }
        super.onClick(view);
    }

    private void initTimer() {

        mRemainTime = mSharedPreferences.getLong("registerRemainTime", 0L);
        mLastTime = mSharedPreferences.getLong("registerLastTime", 0);
        long currentTime = System.currentTimeMillis();
        mRemainTime = mRemainTime - (currentTime - mLastTime);
        if (mRemainTime > 0) {

            startDownTimer(mRemainTime);
        }
    }

    /**
     * 下一步按钮
     */
    public void ModifyPhoneNext(View v) {

            String url = "/ums/users/"+userId+"/phone/notification-verification";
            url = UrlHelper.getUrl(url);

            // 判断验证码和手机号是否正确
            if (checkParams(false)) {

                FakeX509TrustManager.allowAllSSL();
                if (Util.isNetworkAvailable(this)) {
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("smsVerifyCode", mEtModifyPhoneCode.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    JSONObject obj = response;

                                    //点击下一步按钮跳转到修改手机号界面
                                   Intent intent=new Intent(ModifyPhoneNumActivity.this,ModifyPhoneNumActivity.cla);


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
                                        AlertUtil.showOneBtnDialog(ModifyPhoneNumActivity.this, message, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });
                                    } else {
                                        AlertUtil.showOneBtnDialog(ModifyPhoneNumActivity.this, "验证失败", new View.OnClickListener() {
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
                } else {
                    AlertUtil.showOneBtnDialog(ModifyPhoneNumActivity.this, "网络异常，请检查网络连接后重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

            }

    }


    /**
     * 获取验证码
     *
     * @param v
     */
    public void getModifyPhoneCode(View v) {

        //防重复点击
        if (checkClick(v.getId())) {
            String url = "/ums/users/" + userId + "/phone/notification-app";
            url = UrlHelper.getUrl(url);

            JSONObject jsonObject = new JSONObject();

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Lg.e("response..获取验证码", response);

                            //下发验证码成功
                            DMAlertUtil.showOneBtnDialog(ModifyPhoneNumActivity.this,
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
                                AlertUtil.showOneBtnDialog(ModifyPhoneNumActivity.this, message, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                            } else {
                                AlertUtil.showOneBtnDialog(ModifyPhoneNumActivity.this, "获取验证码失败", new View.OnClickListener() {
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

            BaseApplication.getRequestQueue().add(stringRequest);

        }
    }

    /**
     * 下一步之前检查参数
     *
     * @param getVerifyCode true 获取验证码 false 注册
     * @return
     */
    private boolean checkParams(boolean getVerifyCode) {

        String modifiPhoneCode = mEtModifyPhoneCode.getText().toString();
        if (TextUtils.isEmpty(modifiPhoneCode)) {
            //验证码为空
            DMAlertUtil.showOneBtnDialog(ModifyPhoneNumActivity.this, getString(R.string.find_pwd_phone_empty), false);
            return false;
        }


        if (modifiPhoneCode.length() < 6) {

            DMAlertUtil.showOneBtnDialog(ModifyPhoneNumActivity.this, "验证码输入有误，请重新输入", false);
            mEtModifyPhoneCode.setText("");
            return false;
        }

//        if (TextUtils.isEmpty(mUserId)) {
//
//            DMAlertUtil.showOneBtnDialog(ModifyPhoneActivity.this, "您还没获取验证码，请先获取验证码", false);
//            return false;
//        }


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
                mBtnModifyPhone.setText(getString(R.string.find_pwd_wait_time, temp));
                mBtnModifyPhone.setEnabled(false);
            }

            @Override
            public void onFinish() {

                mCountDowning = false;
                mBtnModifyPhone.setEnabled(true);
                mBtnModifyPhone.setText(getString(R.string.get_verify_code));
            }
        }.start();
    }
}
