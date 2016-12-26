package com.jia16.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.jia16.util.FormatUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 找回登录密码界面
 */
public class FindLoginPwdActivity extends BaseActivity {

    /**
     * 密码输入框
     */
    private EditText mEtFindPwd;
    /**
     * 密码显示隐藏开关
     */
    private CheckBox mSwitchPwd;
    /**
     * 完成修改按钮
     */
    private Button mBtFinish;
    /**
     * 上一个界面传递过来的电话号码
     */
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_login_pwd);
        initView();
    }


    /**
     * 初始化页面元素
     */
    @Override
    protected void initView() {
        super.initView();

        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phoneNum");

        ((TextView) findViewById(R.id.title_text)).setText("找回登录密码");

        mEtFindPwd = (EditText) findViewById(R.id.et_find_pwd);

        mSwitchPwd = (CheckBox) findViewById(R.id.switch_pwd);
        mSwitchPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mEtFindPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtFindPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isChecked = !isChecked;
                mEtFindPwd.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = mEtFindPwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });

        //完成修改按钮
        mBtFinish = (Button) findViewById(R.id.bt_finish);
        mBtFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //完成修改按钮的方法
                modifiFinishLoginPwd(view);
            }
        });
    }


    /**
     *完成修改按钮
     */
    public void modifiFinishLoginPwd(View v) {

        //防重复点击
        if (checkClick(v.getId())) {
            String url = "/ums/users/recover";
            url = UrlHelper.getUrl(url);

            if (checkParams(true)) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phone",phoneNum);
                    jsonObject.put("channel", "app");
                    jsonObject.put("password",mEtFindPwd.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                               AlertUtil.showOneBtnDialog(FindLoginPwdActivity.this, "找回登录密码成功", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       //找回成功。回到登录界面
                                       finish();
                                   }
                               });


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
                                    AlertUtil.showOneBtnDialog(FindLoginPwdActivity.this, message, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //设置失败，那么久回到获取验证码的页面
                                            Intent intent=new Intent(FindLoginPwdActivity.this,FindLoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    AlertUtil.showOneBtnDialog(FindLoginPwdActivity.this, "设置密码失败", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //设置失败，那么久回到获取验证码的页面
                                            Intent intent=new Intent(FindLoginPwdActivity.this,FindLoginActivity.class);
                                            startActivity(intent);
                                            finish();
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

        String mFinishPwd = mEtFindPwd.getText().toString();
        if (TextUtils.isEmpty(mFinishPwd)) {

            //密码不能为空
            DMAlertUtil.showOneBtnDialog(FindLoginPwdActivity.this, getString(R.string.find_pwd_phone_empty), false);
            return false;
        }

        if (mFinishPwd.length() < 8) {

            DMAlertUtil.showOneBtnDialog(FindLoginPwdActivity.this, "密码长度不够，请重新输入", false);
            return false;
        }

        if (!FormatUtil.validateLoginPwd(mFinishPwd)) {

            //手机号码格式不正确
            DMAlertUtil.showOneBtnDialog(FindLoginPwdActivity.this, getString(R.string.find_pwd_phone_format_error), false);
            return false;
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
