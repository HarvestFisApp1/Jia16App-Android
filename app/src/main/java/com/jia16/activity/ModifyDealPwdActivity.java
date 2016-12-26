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
 * 注册
 *
 * @author jiaohongyun
 * @date 2015年5月25日
 */
public class ModifyDealPwdActivity extends BaseActivity {

    private boolean mCountDowning;

    /**
     * 手机验证码倒计时
     */
    private CountDownTimer mVerifyCodeTimer;

    /**
     * 手机号码
     */
    private EditText mPhoneNumEditText;

    /**
     * 验证码输入框
     */
    private EditText mVerifyCodeEditText;

    /**
     * 获取验证码按钮
     */
    private Button mGetVerifyCodeButton;

    private CheckBox mPwdSwitch;//密码的显示与隐藏

    private SharedPreferences mSharedPreferences;

    /**
     * 倒计时所剩时间
     */
    private long mRemainTime = 60000L;

    /**
     * 最后停止时间
     */
    private long mLastTime;

    private String mUserId;

    private CheckBox mCheckBox1;    //同意条款选择框
    private EditText mEditPword;    //密码文本输入框


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        ((TextView) findViewById(R.id.title_text)).setText("注册");
        mPhoneNumEditText = (EditText) findViewById(R.id.register_phone);
        String phoneNum = getIntent().getStringExtra("phoneNum");

        //获取验证码按钮
        mGetVerifyCodeButton = (Button) findViewById(R.id.find_pwd_get_btn);

        mVerifyCodeEditText = (EditText) findViewById(R.id.register_verify_code);//验证码输入框

        mEditPword = (EditText) findViewById(R.id.et_password);

        mPwdSwitch = (CheckBox) findViewById(R.id.checkbox);
        mPwdSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置EditText文本为可见的
                    mEditPword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                    mEditPword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isChecked = !isChecked;
                mEditPword.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = mEditPword.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });

        if (null != phoneNum) {

            mPhoneNumEditText.setText(phoneNum);
            mPhoneNumEditText.setSelection(phoneNum.length());
        }
        mCheckBox1 = (CheckBox) findViewById(R.id.register_check_box);

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
     * 注册按钮
     *
     * @param v
     */
    public void register(View v) {

        //防重复点击
        if (checkClick(v.getId())) {

            String url = "/ums/users/register";
            url = UrlHelper.getUrl(url);

            Lg.e("register_url", url);

            // 判断验证码和手机号是否正确
            if (checkParams(false)) {

                FakeX509TrustManager.allowAllSSL();
                if(Util.isNetworkAvailable(this)){
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("phone", mPhoneNumEditText.getText().toString());
                        jsonObject.put("password", mEditPword.getText().toString());
                        jsonObject.put("smsVerifyCode", mVerifyCodeEditText.getText().toString());
                        //jsonObject.put("channel",)
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    JSONObject obj = response;

                                   AlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, "注册成功", new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                       }
                                   });
                                    //注册成功后跳转到登录界面
                                    Intent intent=new Intent(ModifyDealPwdActivity.this,LoginActivity.class);
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
                                        AlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, message, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });
                                    } else {
                                        AlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, "注册失败", new View.OnClickListener() {
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
                            HashMap<String, String> headers = new HashMap<String, String>();
                            return headers;
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };
                    BaseApplication.getRequestQueue().add(jsonObjectRequest);
                }else{
                    AlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, "网络异常，请检查网络连接后重试", new View.OnClickListener() {
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
//        switch (view.getId()){
//            case R.id.:
//            break;
//        }
    }


    /**
     * 打开注册协议
     *
     * @param v
     */
    public void gotoProtocol(View v) {
        String url="//images/register-license.pdf";
        url=UrlHelper.getUrl(url);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);

    }

    /**
     * 获取验证码
     *
     * @param v
     */
    public void getVerifyCode(View v) {

        //防重复点击
        if (checkClick(v.getId())) {
            String url="/ums/users/register/h5-notification";
            url=UrlHelper.getUrl(url);

            if (checkParams(true)) {

                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("phone", mPhoneNumEditText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest stringRequest=new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Lg.e("response..获取验证码",response);

                                //下发验证码成功
                                DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this,
                                        "验证码已发送到您的手机，请查收！",false);
                                startDownTimer(60000L);


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            String errorMsg = new String(error.networkResponse.data);
                            JSONObject json=new JSONObject(errorMsg);
                            if(json!=null){
                                String message = (String) json.opt("message");
                                if(message!=null){
                                    AlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, message, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                }else {
                                    AlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, "获取验证码失败", new View.OnClickListener() {
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
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
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

        String RegistPwd = mEditPword.getText().toString();
        String phoneNum = mPhoneNumEditText.getText().toString();
        if (TextUtils.isEmpty(phoneNum)) {

            //手机号码为空
            DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, getString(R.string.find_pwd_phone_empty), false);
            return false;
        }

        if (!FormatUtil.isMobileNO(phoneNum)) {

            //手机号码格式不正确
            DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, getString(R.string.find_pwd_phone_format_error), false);
            return false;
        }

        if (getVerifyCode) {

            return true;
        }

        if (TextUtils.isEmpty(mUserId)) {

            DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, "您还没获取验证码，请先获取验证码", false);
            return false;
        }

        String verifyCode = mVerifyCodeEditText.getText().toString();
        if (TextUtils.isEmpty(verifyCode)) {

            DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, getString(R.string.find_pwd_code_empty), false);
            return false;
        }

        if (verifyCode.length() < 6) {

            DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, "验证码输入有误，请重新输入", false);
            return false;
        }

        if(TextUtils.isEmpty(RegistPwd)){
            DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this,"密码不能为空",false);
            return false;
        }

        if(RegistPwd.contains(" ")){
            //不能带有空格字符
            DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this,"密码格式不正确，或者密码不能为空",false);
            return false;
        }

        if(!FormatUtil.validateLoginPwd(RegistPwd)){
            DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this,"密码6-16个字符，区分大小写!");
            return false;
        }

        if (!mPwdSwitch.isChecked()) {

            DMAlertUtil.showOneBtnDialog(ModifyDealPwdActivity.this, "请先阅读服务条款并勾选！", false);
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
                mGetVerifyCodeButton.setText(getString(R.string.find_pwd_wait_time, temp));
                mGetVerifyCodeButton.setEnabled(false);
            }

            @Override
            public void onFinish() {

                mCountDowning = false;
                mGetVerifyCodeButton.setEnabled(true);
                mGetVerifyCodeButton.setText(getString(R.string.get_verify_code));
            }
        }.start();
    }
}
