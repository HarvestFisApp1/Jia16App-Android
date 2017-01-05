package com.jia16.more.invitefriends;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.base.BaseFragment;
import com.jia16.util.Constants;
import com.jia16.util.DMConstant;
import com.jia16.util.Lg;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * 更多界面 - ---邀请好友 --活动规则
 */
public class ActiviRuleFragment extends BaseFragment {


    private BaseApplication mContext;
    private LayoutInflater inflater;
    private Button mbtn;
    private WebView mHomeWebview;


    /**
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater=inflater;
        return inflater.inflate(R.layout.webview_common, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        View mPatentView = getView();

        mHomeWebview = (WebView) mPatentView.findViewById(R.id.home_webview);

        WebSettings webSettings = mHomeWebview.getSettings();

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
        webSettings.setSupportZoom(true);
        //webSettings.setBuiltInZoomControls(true);// 显示放大缩小
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUserAgentString("android/1.0");
        webSettings.setUseWideViewPort(true);// 影响默认满屏和双击缩放
        webSettings.setLoadWithOverviewMode(true);// 影响默认满屏和手势缩放

        mHomeWebview.loadUrl(DMConstant.H5Url.MORE_INVITE_FRIEND);

        mHomeWebview.setWebChromeClient(new WebChromeClient(){

        });

        mHomeWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    mHomeWebview.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mHomeWebview.loadDataWithBaseURL(null, "服务器较为繁忙，请稍后再试!", "text/html", "utf-8", null);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingDialog();
            }


            @Override
            public void onPageFinished( final WebView view, final String url) {
                Lg.e("onPageFinished",url);
                super.onPageFinished(view, url);
                dimissLoadingDialog();
            }
        });
        mHomeWebview.setWebChromeClient(new WebChromeClient());


    }


    @Override
    public void onPause() {
        if(mHomeWebview.canGoBack()){
            mHomeWebview.goBack();
        }
        super.onPause();
    }

}


