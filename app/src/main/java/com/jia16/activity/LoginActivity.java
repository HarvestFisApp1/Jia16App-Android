package com.jia16.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.LockPwd;
import com.jia16.bean.UserInfo;
import com.jia16.util.AlertUtil;
import com.jia16.util.Constants;
import com.jia16.util.FakeX509TrustManager;
import com.jia16.util.Lg;
import com.jia16.util.ToastUtil;
import com.jia16.util.UrlHelper;
import com.jia16.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by huangjun on 16/8/15.
 */
public class LoginActivity extends BaseActivity {


    Button mSureBtn;//确定按钮

    EditText mEditPhone;

    EditText mEditPword;

    CheckBox mPwdSwitch;

    private TextView mRegisterBtn;//注册按钮

    private TextView mForgetPwdBtn;//忘记密码按钮

    private ImageView mBackBtn;//返回按钮

    private boolean removePwd = false;//清除手势密码 这时候自动填入用户名

    private boolean changeUser = false;//切换用户  此时进入不保存用户名

    private boolean isSetting = false;//是否为修改密码

    private String cookie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        removePwd = getIntent().getBooleanExtra("removePwd", false);//UnlockGesturePasswordActivity传递过来的清除手势密码
        changeUser = getIntent().getBooleanExtra("changeUser", false);//UnlockGesturePasswordActivity传递过来的使用其他账户登录
        isSetting = getIntent().getBooleanExtra("isSetting", false);//UnlockGesturePasswordActivity传递过来是否为修改密码
    }


    @Override
    public void onBackPressed() {
        goHome();
    }

    private void goHome() {
        if (isSetting) {//如果是修改手势密码进到登录 一定是重置了手势密码  返回的时候 直接都关闭
            Intent it = getIntent();
            it.putExtra("targetUrl", Constants.HOME_PAGE);
            setResult(RESULT_OK, it);
            finish();
        } else if (removePwd || changeUser) {
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent it = getIntent();
            it.putExtra("targetUrl", Constants.HOME_PAGE);
            setResult(RESULT_OK, it);
            finish();
        }
    }

    private void initViews() {
        mBackBtn = (ImageView) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mSureBtn = (Button) findViewById(R.id.btn_sure);
        mSureBtn.setOnClickListener(this);
        mEditPhone = (EditText) findViewById(R.id.edit_phone);
        mEditPword = (EditText) findViewById(R.id.edit_pword);
        mPwdSwitch = (CheckBox) findViewById(R.id.switch_pword);
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
        mRegisterBtn = (TextView) findViewById(R.id.btn_register);
        mRegisterBtn.setOnClickListener(this);
        mForgetPwdBtn = (TextView) findViewById(R.id.btn_forget_pwd);
        mForgetPwdBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                goHome();
                break;
            case R.id.btn_sure:
                String mPhone = mEditPhone.getText().toString();
                String mPwd = mEditPword.getText().toString();
                if (TextUtils.isEmpty(mPhone)) {
                    AlertUtil.showOneBtnDialog(LoginActivity.this, "手机号不能为空", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    return;
                }
                if (TextUtils.isEmpty(mPwd)) {
                    AlertUtil.showOneBtnDialog(LoginActivity.this, "密码不能为空", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    return;
                }
                FakeX509TrustManager.allowAllSSL();
                if (Util.isNetworkAvailable(this)) {
                    postLogin();
                } else {
                    AlertUtil.showOneBtnDialog(LoginActivity.this, "网络异常,请检查网络连接后重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                break;
            case R.id.btn_register:
                String registUrl = Constants.HOME_PAGE + "/#!register";
                jumpBack(registUrl);
                break;
            case R.id.btn_forget_pwd:
                String forgetUrl = Constants.HOME_PAGE + "/#!findloginpwd";
                jumpBack(forgetUrl);
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    private void jumpBack(String url) {
        if (isSetting) {
            Intent intent = getIntent();
            intent.putExtra("targetUrl", url);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (changeUser || removePwd) {
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("targetUrl", url);
                startActivity(intent);
                finish();
            } else {
                Intent it = getIntent();
                it.putExtra("targetUrl", url);
                setResult(RESULT_OK, it);
                finish();
            }
        }
    }


    private void postLogin() {
        String url = "/ums/users/app-login";
        url = UrlHelper.getUrl(url);
        Lg.e("app-login ==", url);
        /***
         18065573819
         18196621300

         abcd1234
         ss123456
         }
         ***/

        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("name", "18065573819");
//            jsonObject.put("password", "abcd1234");
            jsonObject.put("name", mEditPhone.getText().toString());
            jsonObject.put("password", mEditPword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener() {


                    @Override
                    public void onResponse(Object response) {
                        //登录成功重置retry次数
                        sharedPreferences.edit().putInt("retry", 5).apply();
                        JSONObject obj = (JSONObject) response;
                        cookie = obj.optString("cookie");
                        Lg.e("cookie........##",cookie);
                        if (!TextUtils.isEmpty(cookie)) {
                            //TODO HUANGJUN 切换账号的时候 不需要重置手势密码的状态  自动根据缓存判断
//                            if (removePwd || changeUser) {//忘记密码和切换登录账号
//                                sharedPreferences.edit().putString(Constants.LOCK_PWD_REMIND, "0").apply();
//                                sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "0").apply();
//                            }
                            doAfterLogin(cookie);
                        } else {
                            AlertUtil.showOneBtnDialog(LoginActivity.this, "登录失败", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
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
                            AlertUtil.showOneBtnDialog(LoginActivity.this, message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        } else {
                            AlertUtil.showOneBtnDialog(LoginActivity.this, "登录失败", new View.OnClickListener() {
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
        BaseApplication.getRequestQueue().add(stringRequest);
    }

    private List<String> cookies = new ArrayList<>();

    private void doAfterLogin(String cookie) {
        cookies = new Gson().fromJson(cookie, new TypeToken<List<String>>() {
        }.getType());
        synCookies(this);
    }

    CookieManager cookieManager;

    private void synCookies(Context context) {
        CookieSyncManager.createInstance(context);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除
        if (cookies != null && cookies.size() > 0) {
            int size = cookies.size();
            for (int i = 0; i < size; i++) {
                String cookie = cookies.get(i);
                if (cookie != null && cookie.contains("_csrf")) {
                    String[] paras = cookie.split(";");
                    String csrf = paras[0].split("=")[1];
                    Lg.e("csrf", csrf);
                    sharedPreferences.edit().putString("_csrf", csrf).apply();
                }

                //shangjing修改
                if(cookie!=null&&cookie.contains("p2psessionid")){
                    String[] paras = cookie.split(";");
                    String p2psessionid = paras[0].split("=")[1];
                    Lg.e("p2psessionid",p2psessionid);
                    sharedPreferences.edit().putString("p2psessionid",p2psessionid).apply();
                }

                cookieManager.setCookie(Constants.HOME_PAGE, cookie);
                Lg.e("cookie.....%%",cookie);
            }
        }
        String cookieGesture =  "";
        if ("1".equals(sharedPreferences.getString(Constants.GESTURE_STATUS, "0"))) {//已经设置密码 已经开启
            cookieGesture = "gesturestatus=1";
        } else if ("4".equals(sharedPreferences.getString(Constants.GESTURE_STATUS, "0"))) {//已经设置密码 关闭
            cookieGesture = "gesturestatus=2";
        }
        cookieManager.setCookie(Constants.HOME_PAGE, cookieGesture);
        CookieSyncManager.getInstance().sync();
//        Lg.e("cookie====", cookieManager.getCookie(Constants.HOME_PAGE));
        sharedPreferences.edit().putString("Cookie", cookieManager.getCookie(Constants.HOME_PAGE)).apply();
        Lg.e("Cookie", cookieManager.getCookie(Constants.HOME_PAGE));
        getCurrentUser();
    }


    private void getCurrentUser() {
        String url = UrlHelper.getUrl("/ums/users/current");
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("getcurrentUser", response.toString());
                        UserInfo userInfo = new Gson().fromJson(response.toString(), new TypeToken<UserInfo>() {
                        }.getType());
                        BaseApplication.getInstance().setUserInfo(userInfo);


                        String lockPwdstr = sharedPreferences.getString(Constants.LOCK_PWD, "");
                        boolean matched = false;
                        if (!TextUtils.isEmpty(lockPwdstr)) {//已经保存过手势密码
                            List<LockPwd> lockPwds = new Gson().fromJson(lockPwdstr, new TypeToken<List<LockPwd>>() {
                            }.getType());
                            if (lockPwds != null && lockPwds.size() > 0) {
                                if (userInfo != null) {//如果用户都未登录 那么不展示手势密码解锁
                                    for (LockPwd lockPwd : lockPwds) {
                                        if (lockPwd != null && lockPwd.getUserId() == userInfo.getId()) {
                                            matched = true;
                                            String gestureStatus = sharedPreferences.getString(Constants.GESTURE_STATUS, "0");
                                            if ("0".equals(gestureStatus)) {
                                                sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "1").apply();
                                            }
                                            setCurrentRemindStatus(userInfo.getId(), "4");
//                                            sharedPreferences.edit().putString(Constants.LOCK_PWD_REMIND, "4").apply();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!matched) {
                            sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "0").apply();
                            String remind = getCurrentRemindStatus(userInfo.getId());
//                            String remind = sharedPreferences.getString(Constants.LOCK_PWD_REMIND, "0");
                            if (!"3".equals(remind)) {
                                setCurrentRemindStatus(userInfo.getId(), "0");
//                                sharedPreferences.edit().putString(Constants.LOCK_PWD_REMIND, "0").apply();
                            }
                        }

                        String lockStatus = getCurrentRemindStatus(userInfo.getId());
//                        String lockStatus = sharedPreferences.getString(Constants.LOCK_PWD_REMIND, "0");
                        switch (lockStatus) {
                            case "0"://默认情况弹出
                            case "1"://需要设置手势密码
                            case "2"://暂不设置 第二次要提醒
                                gotoSetGesturePwd(cookie);
                                break;
                            case "3"://永不设置
                            case "4"://设置过了
                                viewhome = false;
                                loginSuccess(cookie);
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
//                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };
        BaseApplication.getRequestQueue().add(stringRequest);
    }

    private void gotoSetGesturePwd(final String cookie) {


        AlertUtil.showThreeBtnDialog(LoginActivity.this, new AlertUtil.DialogListener() {
            @Override
            public void onLeftClick(AlertDialog dlg) {

            }

            @Override
            public void onRightClick(AlertDialog dlg) {

            }

            @Override
            public void onTopClick(AlertDialog dlg) {
                UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                if (userInfo != null) {
                    setCurrentRemindStatus(userInfo.getId(), "1");
                }
//                sharedPreferences.edit().putString(Constants.LOCK_PWD_REMIND, "1").apply();
                dlg.cancel();
                Intent intent = new Intent(LoginActivity.this, GesturePwdActivity.class);
                intent.putExtra("cookie", cookie);
                intent.putExtra("removePwd", removePwd);
                startActivityForResult(intent, REQUEST_CODE_SET_PWD);
            }

            @Override
            public void onCenterClick(AlertDialog dlg) {
                UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                if (userInfo != null) {
                    setCurrentRemindStatus(userInfo.getId(), "2");
                }
//                sharedPreferences.edit().putString(Constants.LOCK_PWD_REMIND, "2").apply();
                dlg.cancel();
                viewhome = false;
                loginSuccess(cookie);
            }

            @Override
            public void onBottomClick(AlertDialog dlg) {
                UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                if (userInfo != null) {
                    setCurrentRemindStatus(userInfo.getId(), "3");
                }
//                sharedPreferences.edit().putString(Constants.LOCK_PWD_REMIND, "3").apply();
                dlg.cancel();
                viewhome = false;
                loginSuccess(cookie);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SET_PWD && resultCode == RESULT_OK) {
            String cookie = data.getStringExtra("cookie");
            loginSuccess(cookie);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean viewhome = true;

    private final static int REQUEST_CODE_SET_PWD = 1001;

    private void loginSuccess(String cookie) {
        ToastUtil.getInstant().show(LoginActivity.this, "登录成功");
        if (isSetting) {
            Intent intent = new Intent();
            intent.putExtra("cookie", cookie);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (removePwd || changeUser) {
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("cookie", cookie);
                startActivity(intent);
                finish();
            } else {
                Intent intent = getIntent();
                intent.putExtra("cookie", cookie);
                intent.putExtra("viewhome", viewhome);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
