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
 * 修改交易密码 (我忘记原交易密码了)界面
 */
public class ModifyForgetPwdActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_foget_pwd);
        initView();
        initData();
    }

    //绑定数据
    private void initData() {


    }


    /**
     * 初始化页面元素
     */
    @Override
    protected void initView() {
        super.initView();


        ((TextView) findViewById(R.id.title_text)).setText("修改交易密码");


    }



//    /**
//     * 确认按钮，请求服务器，修改交易密码
//     */
//    private void doAffirmButton() {
//
//        String url = "/ums/users/" + userid + "/deal-password/app-dealPassword";
//        url = UrlHelper.getUrl(url);
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("oldPassword", mEtOldPwd.getText().toString());
//            jsonObject.put("newPassword", mEtNewPwd.getText().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        AlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this, "修改交易密码成功", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //修改密码成功，那么久回到我的账户界面
//                                finish();
//                            }
//                        });
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                try {
//                    String errorMsg = new String(error.networkResponse.data);
//                    JSONObject objJson = new JSONObject(errorMsg);
//                    String message = objJson.getString("message");
//                    if (message != null) {
//                        AlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this, message, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //修改密码失败，那么就要回到我的账户界面
//                                finish();
//                            }
//                        });
//                    } else {
//                        AlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this, "修改失败", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        });
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                String token = sharedPreferences.getString("_csrf", "");
//                String cookie = sharedPreferences.getString("Cookie", "");
//                Lg.e("token", token, "cookie", cookie);
//                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
//                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
//                return headers;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }
//
//        };
//        BaseApplication.getRequestQueue().add(jsonObjectRequest);
//    }
//
//
//    /**
//     * 点击确认按钮之前检查参数
//     *
//     * @param
//     * @return
//     */
//    private boolean checkParams() {
//
//        if (TextUtils.isEmpty(etOldPwd)) {
//
//            //原交易密码为空
//            DMAlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this, "原交易密码不能为空", false);
//            return false;
//        }
//
//        if (FormatUtil.isMobileNO(etNewPwd)) {
//
//            //新交易密码不能为空
//            DMAlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this, "新交易密码不能为空", false);
//            return false;
//        }
//
//        if (etOldPwd.contains(" ") || etNewPwd.contains(" ")) {
//            //不能带有空格字符
//            DMAlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this, "密码格式不正确，不能带有空格字符", false);
//            return false;
//        }
//
//        if(etOldPwd.length()<6){
//            DMAlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this,"密码长度为6-18位，请重新输入",false);
//            return false;
//        }
//
//        if(etNewPwd.length()<6){
//            DMAlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this,"新密码长度为6-18位，请重新输入",false);
//            return false;
//        }
//
//        if (!FormatUtil.validatePaymenPwd(etOldPwd)) {
//            DMAlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this, "旧密码由6-18个字母，数字或符号组成");
//            return false;
//        }
//
//        if(!FormatUtil.validatejiashiPaymenPwd(etNewPwd)){
//            DMAlertUtil.showOneBtnDialog(ModifyForgetPwdActivity.this, "新密码由6-18个字母，数字或符号组成");
//            return false;
//        }
//
//        return true;
//    }

}
