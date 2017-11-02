package com.jia16.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
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
import android.widget.Toast;

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
import com.jia16.util.AppManager;
import com.jia16.util.Constants;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.jia16.util.Util;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
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


    /**
     * 添加友盟分享
     */
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    /**
     * 分享的标题，内容，图片
     */
    private String sharTitle;
    private String sharImage;
    private String sharText;
    private String sharUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        //同步应用程序当前版本的cookie
        synVersionNameCookie(WebViewActivity.this);

        mShareListener = new CustomShareListener(this);


        String cookie = getIntent().getStringExtra("cookie");
        initViews();
        if (!TextUtils.isEmpty(cookie)) {
            sharedPreferences.edit().putString("cookie", cookie).apply();
            synCookies(this);
            getCurrentUser();
        }
        //处理唤醒
        String url = BaseApplication.getInstance().urlData;
        if (url == null) {
            url = getIntent().getStringExtra("targetUrl");
        }
        if (url == null) {
            url = Constants.HOME_PAGE;
        }
        mWebView.loadUrl(url);
        mWebView.reload();

        String app_channel = getAppMetaData(WebViewActivity.this, "UMENG_CHANNEL");
        Lg.e(".....app_channel......",app_channel);


        /**
         * 添加友盟分享
         //                 */
//          /*无自定按钮的分享面板*/
//        mShareAction = new ShareAction(WebViewActivity.this).setDisplayList(
//                SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ)
//                .withText("我是分享内容,快来下载嘉石榴一起投资吧")  //设置分享内容
//                .withTitle("来自友盟自定义分享面板")//设置分享title
//                .withTargetUrl("http://www.qq.com")//设置分享链接
//                //.withMedia(new UMImage(WebViewActivity.this,getResources().getDrawable(R.drawable.umeng_back_icon)))
//                .withMedia(new UMImage(WebViewActivity.this, R.mipmap.ic_launcher))//设置分享的图片
////                .addButton("umeng_sharebutton_custom","umeng_sharebutton_custom","umeng_socialize_qq","umeng_socialize_qq")
////                .setShareboardclickCallback(shareBoardlistener)
//                .setCallback(mShareListener);


        /**
         * 添加友盟分享
         */
        mShareAction = new ShareAction(WebViewActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ)
                .addButton("umeng_sharebutton_custom", "umeng_sharebutton_custom", "umeng_socialize_menu_default", "umeng_socialize_menu_default")
                .setShareboardclickCallback(shareBoardlistener);
    }

    @Override
    public void onBackPressed() {
        AlertUtil.showTwoBtnDialog(this, "确定要退出?", new AlertUtil.Config("确定", "取消"), new AlertUtil.DialogListener() {
            @Override
            public void onLeftClick(AlertDialog dlg) {
                dlg.cancel();
                //finish();
                int currentVersion = android.os.Build.VERSION.SDK_INT;
                if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    System.exit(0);
                } else {// android2.1
                    finish();
                }
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


    //增加自定义分享按钮
    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media == null) {
                if (snsPlatform.mKeyword.equals("umeng_sharebutton_custom")) {
                    Toast.makeText(WebViewActivity.this, "复制链接成功", Toast.LENGTH_LONG).show();
                    // 得到剪贴板管理器
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(Constants.HOME_PAGE+sharUrl);
                }

            } else {

                //友盟分享sdk升级前
//                new ShareAction(WebViewActivity.this).setPlatform(share_media)
//                        .withText(sharText)  //设置分享内容
//                        //.withTitle(sharTitle)//设置分享title
//                        //.withTargetUrl(Constants.HOME_PAGE+sharUrl)//设置分享链接
//                        //https://app.jia16.com/mjia/dist/page/more/0.0.2/images/news.png
//                        //Constants.HOME_PAGE+sharImage
//                        .withMedia(new UMImage(WebViewActivity.this,Constants.HOME_PAGE+sharImage))//设置分享的图片
//                        .setCallback(mShareListener)
//                        .share();

                //友盟分享sdk升级后
                UMWeb web = new UMWeb(Constants.HOME_PAGE+sharUrl);//(分享的链接)
                web.setTitle(sharTitle);//标题
                web.setThumb(new UMImage(WebViewActivity.this,Constants.HOME_PAGE+sharImage));  //缩略图(设置分享的图片)
                web.setDescription(sharText);//描述(设置分享内容)

                new ShareAction(WebViewActivity.this).setPlatform(share_media)
                        //.withText(sharText)  //设置分享内容
                        .withMedia(web)//设置分享的图片
                        .setCallback(mShareListener)
                        .share();
            }
        }
    };






    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
//            String url = BaseApplication.getInstance().urlData;
//            if(url != null){
//                mWebView.loadUrl(url);
//                //mWebView.reload();
//            }
        }

        MobclickAgent.onResume(this);
        synCookies(this);


        //统计用户的设备号码
        TelephonyManager telephonyManager = (TelephonyManager) WebViewActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        //获取本机MIEI号码（仅手机存在）
        //String deviceId = telephonyManager.getDeviceId();
        //获取设备序列号
        String sn = telephonyManager.getSimSerialNumber();

        //获取用户机型
        String phonetype = android.os.Build.MODEL;

        //获取Android版本号
        String phoneVersionCode = android.os.Build.VERSION.RELEASE;

        Lg.e("设备：" + sn + "机型" + phonetype + "版本号：" + phoneVersionCode);

        //int duration=1;
        //统计用户机型，版本号,设备序列号
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userType", phonetype);//机型
        map.put("viersionCode", phoneVersionCode);//Android版本
        map.put("serialNumber", sn);//设备序列号
        MobclickAgent.onEvent(WebViewActivity.this, "phone_information", map);
        //MobclickAgent.onEventValue(WebViewActivity.this, "phoneinformation", map, duration);



        //友盟分享sdk版本
        Lg.e("...版本....", SocializeConstants.SDK_VERSION);

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }

        MobclickAgent.onPause(this);
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

        webSettings.setTextZoom(100);//用户设置字体大小不影响webview的字体改变

        //webSettings.setUserAgentString("android/1.0");

        // 修改ua使得web端正确判断
//        String ua = webSettings.getUserAgentString();
//
//        Lg.e("ua", webSettings.getUserAgentString());

        // 修改ua使得web端正确判断
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+"; android/1.0");

        Lg.e("ua", webSettings.getUserAgentString());

        targetUrl = getIntent().getStringExtra("targetUrl");


        if (targetUrl == null) {
            mWebView.loadUrl(Constants.HOME_PAGE);
        } else {
            mWebView.loadUrl(targetUrl);
        }

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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.reload();
                    }
                },20);

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
                }, 20);
            } else if (requestCode == REQUEST_CODE_LOGIN) {
                String cookie = data.getStringExtra("cookie");
                String targetUrl = data.getStringExtra("targetUrl");

                //shangjing修改
                if (targetUrl == null) {
                    if (!data.getBooleanExtra("viewhome", false)) {
                        //没有设置手势密码
                        targetUrl = WebViewActivity.this.targetUrl;
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
                    }, 20);
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
                Lg.e("getUrl....", url);
                if (url != null && url.contains("?")) {
                    String[] urls = url.split("[?]");
                    if (urls != null && urls.length > 0) {
                        mWebView.loadUrl(urls[0]);
                        mWebView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.reload();
                            }
                        }, 20);
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

        //添加友盟分享
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);


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
            Lg.e("AutoLogin...", url);
            getCurrentUser();
            synCookies(this);
        } else if (url.contains("?PDFurl")) {//注册协议
            url = url.replace("?PDFurl", "");
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        } else if (url.contains("LoginVC") && url.contains("?")) { //                http://100.100.5.96:1990/#!account?LoginVC 跳转登录
            Lg.e("进来未登陆的地址。。。。",url);
            if(AppManager.getAppManager().getActivity(LoginActivity.class) == null){
                Lg.e("currentcookie====", CookieManager.getInstance().getCookie(Constants.HOME_PAGE));
                String[] strings = url.split("[?]");
                targetUrl = strings[0];
                Intent intent = new Intent(WebViewActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOGIN);
            }


        } else if (url.contains("shareNative")) {

            Lg.e("分享的url.....",url);

            String[] strin = url.split("[?]");
            targetUrl = strin[0];
            String paramStr = strin[1];
            String[] params = paramStr.split("&");
            for(int i=0;i<params.length;i++){
                if(params[i].contains("title")){
                    sharTitle = params[i].split("=")[1];
                }else if(params[i].contains("image")){
                    sharImage = params[i].split("=")[1];
                }else if(params[i].contains("text")){
                    sharText = params[i].split("=")[1];
                }/*else if(params[i].contains("url")){
                    sharUrl = params[i].split("=")[1];
                }*/
            }

            if(url.contains("&url")){
                sharUrl = url.split("&url=")[1];
            }


            Lg.e("分享的相关",sharTitle+"..."+sharImage+"...."+sharText);

            //友盟分享
            mShareAction.open();

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
            //清除登录状态
            removeCookie();

            //退出登录成功后，取消账号统计
            MobclickAgent.onProfileSignOff();

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

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = BaseApplication.getInstance().urlData;
        if (url != null) {
            mWebView.loadUrl(url);
            mWebView.reload();
        }
    }


    /**
     * 添加友盟分享的监听
     */
    private static class CustomShareListener implements UMShareListener {

        private WeakReference<WebViewActivity> mActivity;

        private CustomShareListener(WebViewActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.WEIXIN && platform != SHARE_MEDIA.WEIXIN_CIRCLE

//                        &&platform!=SHARE_MEDIA.FOURSQUARE
//                        &&platform!=SHARE_MEDIA.TUMBLR
//                        &&platform!=SHARE_MEDIA.POCKET
//                        &&platform!=SHARE_MEDIA.PINTEREST
//                        &&platform!=SHARE_MEDIA.LINKEDIN
//                        &&platform!=SHARE_MEDIA.INSTAGRAM
//                        &&platform!=SHARE_MEDIA.GOOGLEPLUS
//                        &&platform!=SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.QQ) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.WEIXIN
                    && platform != SHARE_MEDIA.WEIXIN_CIRCLE
//                    &&platform!=SHARE_MEDIA.FOURSQUARE
//                    &&platform!=SHARE_MEDIA.TUMBLR
//                    &&platform!=SHARE_MEDIA.POCKET
//                    &&platform!=SHARE_MEDIA.PINTEREST
//                    &&platform!=SHARE_MEDIA.LINKEDIN
//                    &&platform!=SHARE_MEDIA.INSTAGRAM
//                    &&platform!=SHARE_MEDIA.GOOGLEPLUS
//                    &&platform!=SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.QQ) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        /** attention to this below ,must add this**/
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }


}
