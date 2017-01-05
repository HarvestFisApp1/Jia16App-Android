package com.jia16.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jia16.activity.MainActivity;
import com.jia16.base.BaseApplication;
import com.jia16.util.Lg;

/**
 * Created by Administrator on 2016/2/25.
 * 监听所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
 */
public class MyWebViewClient extends WebViewClient {

    private WebView mWebView;
    private WebActivity mContext;

    public MyWebViewClient(WebActivity context, WebView webView){

        this.mContext = context;
        this.mWebView = webView;
        //SynCookies();
        //CommoUtil.initCookies(BaseApplication.getInstance());//初始化cookie
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void onPageFinished(WebView webView, String url) {
        super.onPageFinished(webView, url);
        webView.getSettings().setJavaScriptEnabled(true);
        Lg.e("onPageFinished",url);
        mContext.dimissLoadingDialog();
        handlerUrl(url);


    }

    @Override
    public void onLoadResource(WebView view, String url) {
        //Lg.e("onLoadResource", mWebView.getUrl());
        //super.onLoadResource(view, url);
        if(url.contains("?openjia16")){
            Lg.e("huanxin--url",url);//https://app.jia16.com/#!newdetail&userid=96560&subjectid=10057&canVoucher=n&type=PERSONAL_LOAN&title=项目详情?openjia16
           Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            url =  "jia16://"+url.substring(url.indexOf("#"));
            Lg.e("url唤醒jia16_app",url);//jia16://#!newdetail&userid=96560&subjectid=10057&canVoucher=n&type=PERSONAL_LOAN&title=项目详情?openjia16
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try{
                mContext.startActivity(intent);
            }catch (Exception e){}
        }
        if(url.startsWith("http://a.app.qq.com/")){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try{
                mContext.startActivity(intent);
            }catch (Exception e){}
            view.stopLoading();
        }

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Lg.d("onPageStarted");
        mContext.showLoadingDialog();
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        mWebView.loadDataWithBaseURL(null, "服务器较为繁忙，请稍后再试!", "text/html", "utf-8", null);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Lg.d("shouldOverrideUrlLoading",url);

        if( handlerUrl(url)){
        }else{
            view.loadUrl(url);
        }
        return true;
    }


    //检查当前是否是登录状态
    public boolean CheckLogin(String url) {
        String cookies = BaseApplication.getInstance().sharedPreferences.getString("Cookie", null);
        Lg.e("&&&cookies.....",cookies);
        if(cookies==null){//表示没有登录，就跳转到登录界面
            Intent intent=new Intent(mContext,MainActivity.class);
            intent.putExtra("index",3);
            mContext.startActivity(intent);
            return false;
        }
        return true;
    }

    private boolean handlerUrl(String url) {

        if(url.contains("&loginCheck")){

            boolean b = CheckLogin(url);
            if(!b){
                return true;
            }
        }

        if(url.contains("&homePage0")){//跳转到首页
            Intent intent=new Intent(mContext,MainActivity.class);
            intent.putExtra("index",0);
            mContext.startActivity(intent);

        }else if(url.contains("&homePage1")) {//点击更多产品跳转到融资借款

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("index", 1);
            mContext.startActivity(intent);
        }else if(url.contains("&DetailPage")){
            return true;
        }else if(url.contains("md=integral")){//跳转到积分详情
            return true;
        }
        return true;
    }


}
