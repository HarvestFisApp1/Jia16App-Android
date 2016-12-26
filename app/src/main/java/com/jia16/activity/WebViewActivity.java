package com.jia16.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.jia16.util.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangjun on 16/8/15.
 */
public class WebViewActivity extends BaseActivity {

    private final int REQUEST_CODE_SET_GUESTURE = 10005;
    private WebView mWebView;

    private final int REQUEST_CODE_LOGIN = 10001;
    private final int REQUEST_CODE_INVEST = 10002;

    private final int REQUEST_CODE_CHANGE_GUESTURE = 10004;
    private String targetUrl;

    private String errorUrl = "file:///android_asset/reload.html";

    private RelativeLayout mErrorInfoView;

    private RelativeLayout mLoadingView;


    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String cookie = getIntent().getStringExtra("cookie");
        initViews();
        if (!TextUtils.isEmpty(cookie)) {
            sharedPreferences.edit().putString("cookie", cookie).apply();
            synCookies(this);
            getCurrentUser();


            url=BaseApplication.getInstance().urlData;
            if(url != null){
                url=BaseApplication.getInstance().urlData;
                Lg.e("url..............",url);
            }else{
                url=Constants.HOME_PAGE;
            }
            mWebView.loadUrl(url);
            mWebView.reload();
        }
    }

    @Override
    public void onBackPressed() {
        AlertUtil.showTwoBtnDialog(this, "确定要退出?", new AlertUtil.Config("确定", "取消"), new AlertUtil.DialogListener() {
            @Override
            public void onLeftClick(AlertDialog dlg) {
                dlg.cancel();
                finish();
            }

            @Override
            public void onRightClick(AlertDialog dlg) {
                dlg.cancel();
            }

            @Override
            public void onTopClick(AlertDialog dlg) {

            }

            @Override
            public void onCenterClick(AlertDialog dlg) {

            }

            @Override
            public void onBottomClick(AlertDialog dlg) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
        synCookies(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    private void initViews() {

        mWebView = (WebView) findViewById(R.id.main_webview);
        mErrorInfoView = (RelativeLayout) findViewById(R.id.errorview);
        mLoadingView = (RelativeLayout) findViewById(R.id.loading_view);
        mLoadingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLoadingView.isShown()) {
                    mLoadingView.setVisibility(View.GONE);
                }
            }
        }, 10000);
        ImageView mLoadingImg = (ImageView) findViewById(R.id.loading_img);
        mLoadingImg.setBackgroundResource(R.drawable.frame_anim);
        AnimationDrawable anim = (AnimationDrawable) mLoadingImg.getBackground();
        anim.start();
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);// 显示放大缩小
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setPluginsEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getPath());
        webSettings.setUseWideViewPort(true);// 影响默认满屏和双击缩放
        webSettings.setLoadWithOverviewMode(true);// 影响默认满屏和手势缩放
        webSettings.setUserAgentString("android/1.0");
        Lg.e("ua", webSettings.getUserAgentString());

        targetUrl = getIntent().getStringExtra("targetUrl");

        url=BaseApplication.getInstance().urlData;
        if (targetUrl == null) {
            if(url != null){
                url=BaseApplication.getInstance().urlData;
                Lg.e("url..............",url);
            }else{
                url=Constants.HOME_PAGE;
            }
            mWebView.loadUrl(url);
        } else {
            mWebView.loadUrl(targetUrl);
        }


//        if (targetUrl == null) {
//            mWebView.loadUrl(Constants.HOME_PAGE);
//        } else {
//            mWebView.loadUrl(targetUrl);
//        }

        mWebView.setWebChromeClient(new WebChromeClient() {
        });
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                Lg.e("shouldOverrideUrlLoading", url);
                if (handleUrl(url)) {
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Lg.e("onReceivedError", "xxx");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
//                Lg.e("onLoadResource", mWebView.getUrl());
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Lg.e("onPageStarted", url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                Lg.e("onPageFinished", url);
                if (!Util.isNetworkAvailable(WebViewActivity.this)) {
                    mErrorInfoView.setVisibility(View.VISIBLE);
                    mErrorInfoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Util.isNetworkAvailable(WebViewActivity.this)) {
                                mErrorInfoView.setVisibility(View.GONE);
                                mWebView.reload();
                            }
                        }
                    });
                } else {
                    handleUrl(url);
                }
                super.onPageFinished(view, url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SET_GUESTURE) {
                mWebView.loadUrl(WebViewActivity.this.targetUrl);
                mWebView.reload();
            } else if (requestCode == REQUEST_CODE_CHANGE_GUESTURE) {
                String targetUrl = data.getStringExtra("targetUrl");
                if (!TextUtils.isEmpty(targetUrl)) {
                    mWebView.loadUrl(targetUrl);
//                    final String finalUrl = targetUrl;
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (!Constants.HOME_PAGE.equals(finalUrl)) {//主要是由于锚点  #的原因  带了#的url web.loadurl无效  但不带的有效
//                                mWebView.reload();
//                            }
//                        }
//                    }, 500);
                } else {
                    mWebView.loadUrl(WebViewActivity.this.targetUrl);
                    mWebView.reload();
                }

            } else if (requestCode == REQUEST_CODE_INVEST) {
                mWebView.loadUrl(targetUrl);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.reload();
                    }
                }, 500);
            } else if (requestCode == REQUEST_CODE_LOGIN) {
                String cookie = data.getStringExtra("cookie");
                String targetUrl = data.getStringExtra("targetUrl");

                //shangjing修改
                if(targetUrl==null){
                    if(!data.getBooleanExtra("viewhome",false)){
                        //没有设置手势密码
                        targetUrl=WebViewActivity.this.targetUrl;
                    }
                }

                if (data.getBooleanExtra("viewhome", false)) {//如果是设置完手势密码 需要跳转首页
                    targetUrl = Constants.HOME_PAGE;
                }
                if (!TextUtils.isEmpty(cookie)) {
                    sharedPreferences.edit().putString("cookie", cookie).apply();
//                    cookies = new Gson().fromJson(cookie, new TypeToken<List<String>>() {
//                    }.getType());
                    synCookies(this);
                    getCurrentUser();
                }
                if (!TextUtils.isEmpty(targetUrl)) {
                    mWebView.loadUrl(targetUrl);
                    final String finalUrl = targetUrl;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!Constants.HOME_PAGE.equals(finalUrl)) {//主要是由于锚点  #的原因  带了#的url web.loadurl无效  但不带的有效
                                mWebView.reload();
                            }
                        }
                    }, 500);
                } else {
                    mWebView.loadUrl(WebViewActivity.this.targetUrl);
                    mWebView.reload();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            if (requestCode == 10003 || requestCode ==
                    REQUEST_CODE_CHANGE_GUESTURE) {//getUrl()获取当前页面的URL
                String url = mWebView.getUrl();//此时应该在个人设置页面
                Lg.e("getUrl....",url);
                if (url != null && url.contains("?")) {
                    String[] urls = url.split("[?]");
                    if (urls != null && urls.length > 0) {
                        mWebView.loadUrl(urls[0]);
                        mWebView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.reload();
                            }
                        }, 500);
                    }
                }
                //关闭手势密码 直接退出 这里不能刷新 刷新的话会重新进页面
                //必须去掉后面的标识符 再刷新
//                mWebView.reload();
            } else if (requestCode == REQUEST_CODE_INVEST) {
                mWebView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.reload();
                    }
                }, 500);
            }
            synCookies(this);
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    CookieManager cookieManager;

    /**
     * 同步一下cookie
     */
    public void synCookies(Context context) {
        CookieSyncManager.createInstance(context);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除
        String cookieStr = sharedPreferences.getString("cookie", "");
        List<String> cookies = new Gson().fromJson(cookieStr, new TypeToken<List<String>>() {
        }.getType());
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
                //复制一份 usersessionid
                if (cookie != null && cookie.contains("p2psessionid")) {
                    String copyCookie = cookie;
                    copyCookie = copyCookie.replace("p2psessionid", "usersessionid");
                    if (copyCookie.contains("HttpOnly;")) {
                        copyCookie = copyCookie.replace("HttpOnly;", "");
                    }
                    cookieManager.setCookie(Constants.HOME_PAGE, copyCookie);
                }
                cookieManager.setCookie(Constants.HOME_PAGE, cookie);
            }
        }
        String cookieGesture = "gesturestatus=" + sharedPreferences.getString(Constants.GESTURE_STATUS, "0");
        cookieManager.setCookie(Constants.HOME_PAGE, cookieGesture);
        CookieSyncManager.getInstance().sync();
//        Lg.e("cookie====", cookieManager.getCookie(Constants.HOME_PAGE));
        sharedPreferences.edit().putString("Cookie", cookieManager.getCookie(Constants.HOME_PAGE)).apply();
        Lg.e("Cookie", cookieManager.getCookie(Constants.HOME_PAGE));
    }

    public void getCurrentUser() {
        String url = UrlHelper.getUrl("/ums/users/current");
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("getcurrentUser", response.toString());
                        UserInfo userInfo = new Gson().fromJson(response.toString(), new TypeToken<UserInfo>() {
                        }.getType());
                        BaseApplication.getInstance().setUserInfo(userInfo);
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


    private boolean handleUrl(String url) {
        if (url.contains("?LoadSuccess")) {
            if (mLoadingView.isShown()) {
                mLoadingView.setVisibility(View.GONE);
            }
        } else if (url.contains("AutoLogin")) {
            Lg.e("AutoLogin...",url);
            getCurrentUser();
            synCookies(this);
        } else if (url.contains("?PDFurl")) {//注册协议
            url = url.replace("?PDFurl", "");
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        } else if (url.contains("LoginVC") && url.contains("?")) { //http://100.100.5.96:1990/#!account?LoginVC 跳转登录
            Lg.e("currentcookie====", CookieManager.getInstance().getCookie(Constants.HOME_PAGE));
            String[] strings = url.split("[?]");
            targetUrl = strings[0];
            Intent intent = new Intent(WebViewActivity.this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        } else if (url.contains("LogoutVC")) {
            UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
            int userid = 0;
            if (userInfo != null) {
                userid = userInfo.getId();
            }
            String status = getCurrentRemindStatus(userid);
            if (!"3".equals(status)) {
                setCurrentRemindStatus(userid, "0");
            }
            BaseApplication.getInstance().setUserInfo(null);
//            sharedPreferences.edit().putString(Constants.LOCK_PWD_REMIND, "0").apply();
            //清除登录状态
            removeCookie();
        } else if (url.contains("?InvestConfirmVC") && url.contains("?")) {
            mWebView.goBack();
//http://10.139.98.226/#!list?
// InvestConfirmVC
// &userid=5
// &subjectid=95
// &title=APP新手标测试2016-08-15
// &voucherid=d
// &amount=1000
// &canVoucher=undefined
// &voucherAmount=null
            String[] strings = url.split("[?]");
            targetUrl = strings[0];
//            targetUrl = targetUrl.split("&")[0];
            String paramStr = strings[1];
            String[] params = paramStr.split("&");
            Intent intent = new Intent(WebViewActivity.this, InvestConfirmActivity.class);
            if (params[1].split("=").length > 1) {
                intent.putExtra("userid", params[1].split("=")[1]);
            }
            if (params[2].split("=").length > 1) {
                intent.putExtra("subjectid", params[2].split("=")[1]);
            }
            if (params[3].split("=").length > 1) {
                intent.putExtra("title", params[3].split("=")[1]);
            }
            if (params[4].split("=").length > 1) {
                intent.putExtra("voucherid", params[4].split("=")[1]);
            }
            if (params[5].split("=").length > 1) {
                intent.putExtra("amount", params[5].split("=")[1]);
            }
            if (params[6].split("=").length > 1) {
                intent.putExtra("canVoucher", params[6].split("=")[1]);
            }
            if (params[7].split("=").length > 1) {
                intent.putExtra("voucherAmount", params[7].split("=")[1]);
            }
            if (params[8].split("=").length > 1) {
                intent.putExtra("agreeUrl", params[8].split("=")[1]);
            }
            startActivityForResult(intent, REQUEST_CODE_INVEST);
        } else if (url != null && url.contains("SwitchClick")) {
            String[] paras = url.split("&");
            String typeString = "";
            if (paras != null && paras.length > 0) {
                String temp = paras[paras.length - 1];
                typeString = temp.split("=")[1];
            }
            if ("on".equals(typeString)) {//开
                sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "1").apply();
                synCookies(this);
            } else if ("off".equals(typeString)) {
                String lockPwdstr = sharedPreferences.getString(Constants.LOCK_PWD, "");
                if (!TextUtils.isEmpty(lockPwdstr)) {//已经保存过手势密码
                    List<LockPwd> lockPwds = new Gson().fromJson(lockPwdstr, new TypeToken<List<LockPwd>>() {
                    }.getType());
                    if (lockPwds != null && lockPwds.size() > 0) {
                        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                        if (userInfo == null) {
                            userInfo = new UserInfo();
                            userInfo.setId(-1);
                        }
                        for (LockPwd lockPwd : lockPwds) {
                            if (lockPwd != null && lockPwd.getUserId() == userInfo.getId()) {
                                Intent intent = new Intent(WebViewActivity.this, UnlockGesturePasswordActivity.class);
                                intent.putExtra("lockPwd", lockPwd);
                                intent.putExtra("isClose", true);
                                startActivityForResult(intent, 10003);
                                break;
                            }
                        }

                    }
                }
            }
//            cookieManager = CookieManager.getInstance();
//            String cookie = cookieManager.getCookie(Constants.HOME_PAGE);
//
//            String[] cookieList = cookie.split(";");
//            for (String str : cookieList) {
//                if (str != null && str.contains("gesturestatus") && str.contains("=")) {
//                    String status = str.split("=")[1];
//                    if ("0".equals(status)) {
//                        Intent intent = new Intent(WebViewActivity.this, GesturePwdActivity.class);
//                        intent.putExtra("isOpen", true);
//                        startActivity(intent);
//                    } else if ("1".equals(status)) {//本地开启
//
//                    } else if ("2".equals(status)) {
//
//                    }
////                            gesturestatus    0：本地未设置手势，1：本地开启手势  2：本地关闭手势
//                }
//            }
//            synCookies(this);
//            Lg.e("xxx", cookie);
        } else if (url.contains("GesturePasswordUpdate")) {//修改手势密码
            String lockPwdstr = sharedPreferences.getString(Constants.LOCK_PWD, "");
            if (!TextUtils.isEmpty(lockPwdstr)) {//已经保存过手势密码
                List<LockPwd> lockPwds = new Gson().fromJson(lockPwdstr, new TypeToken<List<LockPwd>>() {
                }.getType());
                if (lockPwds != null && lockPwds.size() > 0) {
                    UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                    if (userInfo == null) {
                        userInfo = new UserInfo();
                        userInfo.setId(-1);
                    }
                    for (LockPwd lockPwd : lockPwds) {
                        if (lockPwd != null && lockPwd.getUserId() == userInfo.getId()) {
                            Intent intent = new Intent(WebViewActivity.this, UnlockGesturePasswordActivity.class);
                            intent.putExtra("lockPwd", lockPwd);
                            intent.putExtra("isSetting", true);
                            startActivityForResult(intent, REQUEST_CODE_CHANGE_GUESTURE);
                            break;
                        }
                    }

                }
            }
        } else if (url.contains("GesturePasswordSetup")) {//设置手势密码
            String[] strings = url.split("[?]");
            targetUrl = strings[0];
            Intent intent = new Intent(WebViewActivity.this, GesturePwdActivity.class);
            intent.putExtra("setup", true);
            startActivityForResult(intent, REQUEST_CODE_SET_GUESTURE);
        } else {
            return false;
        }

        return true;

    }
}
