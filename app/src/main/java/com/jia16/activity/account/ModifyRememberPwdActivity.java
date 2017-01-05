package com.jia16.activity.account;

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
 * 修改交易密码 (我记得原交易密码)界面
 */
public class ModifyRememberPwdActivity extends BaseActivity {

    private EditText mEtOldPwd; //旧密码的文本输入框
    private ImageView mIvDeleteOldPwd;//清除旧文本框已经输入的内容的按钮
    private EditText mEtNewPwd;//新密码的文本输入框
    private ImageView mIvDeleteNewPwd;//清除新文本框已经输入的内容的按钮
    private CheckBox mCbShowPwd;    //显示与隐藏新密码
    private Button mBtAffirm;       //确认按钮
    private String etOldPwd;        //原交易密码
    private String etNewPwd;        //新交易密码
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_remember_pwd);
        initView();
        initData();
    }

    //绑定数据
    private void initData() {
        //设置checkBox勾选框的状态
        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {//为勾选状态，表示需要显示新的密码
                    mEtNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isChecked = !isChecked;
                mEtNewPwd.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = mEtNewPwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });

    }


    /**
     * 初始化页面元素
     */
    @Override
    protected void initView() {
        super.initView();

        userid = getIntent().getIntExtra("userid", 0);

        ((TextView) findViewById(R.id.title_text)).setText("修改交易密码");

        mEtOldPwd = (EditText) findViewById(R.id.et_old_pwd);//旧密码的文本输入框
        mIvDeleteOldPwd = (ImageView) findViewById(R.id.iv_delete_old_pwd);//清除旧文本框已经输入的内容的按钮
        mIvDeleteOldPwd.setOnClickListener(this);

        mEtNewPwd = (EditText) findViewById(R.id.et_new_pwd);//新密码的文本输入框
        mIvDeleteNewPwd = (ImageView) findViewById(R.id.iv_delete_new_pwd);//清除新文本框已经输入的内容的按钮
        mIvDeleteNewPwd.setOnClickListener(this);

        mCbShowPwd = (CheckBox) findViewById(R.id.cb_show_pwd);//显示与隐藏新密码

        mBtAffirm = (Button) findViewById(R.id.bt_affirm); //确认按钮
        mBtAffirm.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (checkClick(view.getId())) {

            etOldPwd = mEtOldPwd.getText().toString();
            etNewPwd = mEtNewPwd.getText().toString();

            switch (view.getId()) {
                case R.id.iv_delete_old_pwd://清除旧文本框已经输入的内容的按钮
                    if (etOldPwd.length() == 0) {
                        //表示文本输入框中还没有输入，那么什么也不做
                    } else {
                        mEtOldPwd.setText("");
                    }
                    break;

                case R.id.iv_delete_new_pwd://清除新文本框已经输入的内容的按钮
                    if (etNewPwd.length() == 0) {
                        //表示文本输入框中还没有输入，那么什么也不做
                    } else {
                        mEtNewPwd.setText("");
                    }
                    break;

                case R.id.bt_affirm: //确认按钮

                    if (checkParams()) {//检查参数
                        //确认按钮，请求服务器，修改交易密码
                        doAffirmButton();
                    }


                    break;
                default:
                    break;
            }
        }
        super.onClick(view);
    }

    /**
     * 确认按钮，请求服务器，修改交易密码
     */
    private void doAffirmButton() {

        String url = "/ums/users/" + userid + "/deal-password/app-dealPassword";
        url = UrlHelper.getUrl(url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldPassword", mEtOldPwd.getText().toString());
            jsonObject.put("newPassword", mEtNewPwd.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AlertUtil.showOneBtnDialog(ModifyRememberPwdActivity.this, "修改交易密码成功", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //修改密码成功，那么久回到我的账户界面
                                finish();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String errorMsg = new String(error.networkResponse.data);
                    JSONObject objJson = new JSONObject(errorMsg);
                    String message = objJson.getString("message");
                    if (message != null) {
                        AlertUtil.showOneBtnDialog(ModifyRememberPwdActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //修改密码失败，那么就要回到我的账户界面
                                finish();
                            }
                        });
                    } else {
                        AlertUtil.showOneBtnDialog(ModifyRememberPwdActivity.this, "修改失败", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
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
        BaseApplication.getRequestQueue().add(jsonObjectRequest);
    }


    /**
     * 点击确认按钮之前检查参数
     *
     * @param
     * @return
     */
    private boolean checkParams() {

        if (TextUtils.isEmpty(etOldPwd)) {

            //原交易密码为空
            DMAlertUtil.showOneBtnDialog(ModifyRememberPwdActivity.this, "原交易密码不能为空", false);
            return false;
        }

        if (FormatUtil.isMobileNO(etNewPwd)) {

            //新交易密码不能为空
            DMAlertUtil.showOneBtnDialog(ModifyRememberPwdActivity.this, "新交易密码不能为空", false);
            return false;
        }

        if (etOldPwd.contains(" ") || etNewPwd.contains(" ")) {
            //不能带有空格字符
            DMAlertUtil.showOneBtnDialog(ModifyRememberPwdActivity.this, "密码格式不正确，不能带有空格字符", false);
            return false;
        }

        if (etOldPwd.length() < 6) {
            DMAlertUtil.showOneBtnDialog(ModifyRememberPwdActivity.this, "密码长度为6-18位，请重新输入", false);
            return false;
        }

        if (etNewPwd.length() < 6) {
            DMAlertUtil.showOneBtnDialog(ModifyRememberPwdActivity.this, "新密码长度为6-18位，请重新输入", false);
            return false;
        }


        if (!FormatUtil.validatejiashiPaymenPwd(etNewPwd)) {
            DMAlertUtil.showOneBtnDialog(ModifyRememberPwdActivity.this, "新密码由6-18个字母，数字或符号组成");
            return false;
        }

        return true;
    }

}
