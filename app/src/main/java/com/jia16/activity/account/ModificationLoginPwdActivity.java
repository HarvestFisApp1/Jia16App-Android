package com.jia16.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
 * 修改登录密码的界面
 */
public class ModificationLoginPwdActivity extends BaseActivity {

    /**
     * 原登录密码输入框
     */
    private EditText mEtOriginalPword;
    /**
     * 原登录密码输入框的显示，隐藏
     */
    private CheckBox mSwitchOriginalPword;

    /**
     * 新登录密码输入框
     */
    private EditText mEtNowPword;

    /**
     * 新登录密码输入框的显示，隐藏
     */
    private CheckBox mSwitchNowPword;
    /**
     * 完成修改按钮
     */
    private Button mBtnOriginalFinish;
    /**
     * 我的账户界面传递过来的用户id
     */
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_login_pwd);
        initView();
    }


    /**
     * 初始化页面元素
     */
    @Override
    protected void initView() {
        super.initView();

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",0);
        Lg.e("修改登录密码界面收到账户界面传递的id",userId);

        ((TextView) findViewById(R.id.title_text)).setText("修改登录密码");

        //原登录密码输入框
        mEtOriginalPword = (EditText) findViewById(R.id.et_original_pword);
        //原登录密码输入框的显示，隐藏
        mSwitchOriginalPword = (CheckBox) findViewById(R.id.switch_original_pword);
        mSwitchOriginalPword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置EditText文本为可见的
                    mEtOriginalPword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                    mEtOriginalPword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isChecked = !isChecked;
                mEtOriginalPword.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = mEtOriginalPword.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });

        //新登录密码输入框
        mEtNowPword = (EditText) findViewById(R.id.et_now_pword);
        //新登录密码输入框的显示，隐藏
        mSwitchNowPword = (CheckBox) findViewById(R.id.switch_now_pword);
        mSwitchNowPword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置EditText文本为可见的
                    mEtNowPword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                    mEtNowPword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isChecked = !isChecked;
                mEtNowPword.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = mEtNowPword.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });

        mBtnOriginalFinish = (Button) findViewById(R.id.btn_original_finish);
        mBtnOriginalFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_original_finish://完成修改登录密码按钮
            //检查，防止点击多次
            if(checkClick(view.getId())){
                //获取修改之前检查参数
                if(checkParams()){

                    String url="/ums/users/"+userId+"/password";
                    url= UrlHelper.getUrl(url);

                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("newPassword",mEtNowPword.getText().toString());
                        jsonObject.put("oldPassword",mEtOriginalPword.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest stringRequest=new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    AlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this, getString(R.string.modification_pwd_success), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //回到我的账户界面
                                            finish();
                                        }
                                    });

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            try {
                                String errorMessage = new String(error.networkResponse.data);
                                JSONObject objMessage=new JSONObject(errorMessage);
                                String message = (String) objMessage.opt("message");
                                if(message!=null){
                                    AlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this, message, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //回到我的账户界面
                                            finish();
                                        }
                                    });
                                }else{
                                    AlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this,getString(R.string.modification_pwd_fail), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //回到我的账户界面
                                            finish();
                                        }
                                    });
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }){
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
            break;
        }
        super.onClick(view);
    }


    /**
     * 获取修改之前检查参数
     */
    private boolean checkParams() {
        //原登录密码
        String OriginalPword = mEtOriginalPword.getText().toString();

        if (TextUtils.isEmpty(OriginalPword)) {
            //原登录密码为空
            DMAlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this, getString(R.string.original_login_pword), false);
            return false;
        }

        if(OriginalPword.contains(" ")){
            DMAlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this,"原密码包含有空格字符",false);
            return false;
        }

        if(OriginalPword.length()<8){
            DMAlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this,"密码长度8位及以上,区分大小写",false);
            return false;
        }


        if(FormatUtil.jiaShivalidateLoginPwd(OriginalPword)){
            DMAlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this,"原密码格式错误");
            return false;
        }


        //新登录密码
        String NowPword = mEtNowPword.getText().toString();

        if (TextUtils.isEmpty(NowPword)) {

            DMAlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this,getString(R.string.now_login_pword), false);
            return false;
        }

        if (NowPword.length() < 8) {

            DMAlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this, "新密码长度8位及以上,区分大小写", false);
            return false;
        }


        if(NowPword.contains(" ")){
            //不能带有空格字符
            DMAlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this,"新密码格式不正确，不能包含空字符串",false);
            return false;
        }

        if(FormatUtil.jiaShivalidateLoginPwd(NowPword)){
            DMAlertUtil.showOneBtnDialog(ModificationLoginPwdActivity.this,"新密码格式错误，不全为数字");
            return false;
        }
        return true;
    }

}
