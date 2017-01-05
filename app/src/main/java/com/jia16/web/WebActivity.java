package com.jia16.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.util.Lg;

import java.util.ArrayList;

/**
 * @author tangjian
 * @date 2015-6-8
 */
public class WebActivity extends BaseActivity implements OnClickListener {

    private static final int LOGIN_REQUEST_CODE = 0x103;
    private WebView mWebView;
    /**
     * 加载的url
     */
    private String mUrl;
    /**
     * h5页面标题
     */
    private String title;
    private TextView tvTitle;

    private LinearLayout mContentView;
    /**
     * 是否自动获取h5标题
     */
    private boolean mIsTitleAuto;

    /**
     * h5标题的集合
     *
     * @param savedInstanceState
     */
    private ArrayList<String> mTitleList;

    /**
     * 首次加载
     * @param savedInstanceState
     */
    private boolean isFirstLoad = true;
    private RelativeLayout mRlfinance;
    private boolean isTitle;    //是否需要标题栏,默认为false需要
    private ImageView mBtnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview_content);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("linkUrl");
        title = intent.getStringExtra("title");
        mIsTitleAuto = intent.getBooleanExtra("autoTitle", false); //webview的title自动获取
        isTitle = intent.getBooleanExtra("isTitle", false);//是否需要标题栏
        mTitleList = new ArrayList<>();

        initView();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void initView() {

        //设置标题
        setTitle();

        mRlfinance = (RelativeLayout) findViewById(R.id.rl_finance);
        mBtnback = (ImageView) findViewById(R.id.btn_back);

        if(isTitle){//表示不需要标题栏
            mRlfinance.setVisibility(View.GONE);
        }else {
            mRlfinance.setVisibility(View.VISIBLE);
        }

        mContentView = (LinearLayout) findViewById(R.id.content_view);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getPath());
        webSettings.setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
        //webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);// 支持缩放
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 加载需要显示的网页
        Lg.d("加载的网页：" + mUrl);
        mWebView.loadUrl(mUrl);
        // 设置Web视图
        mWebView.setWebViewClient(new MyWebViewClient(WebActivity.this,mWebView));
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());

        // 设置返回按钮事件
        findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtnBack();
            }
        });

    }



    private void clickBtnBack() {

        //返回的事件
        if (mWebView.canGoBack()) {
            back();
        } else {
            finish();
        }
    }

    private void setTitle() {

        tvTitle = (TextView) findViewById(R.id.title_text);
        if (title != null) {

            tvTitle.setText(title);
        }
        if (mIsTitleAuto) {

            tvTitle.setText(""); //自动获取标题时，先置标题为空
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mWebView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * webview 回退
     */
    private void back() {

        //当网页回退时，能够获取到当前网页的标题
        mWebView.goBack();
        int size = mTitleList.size();
        if (size > 0) {

            mTitleList.remove(size - 1);
            size = mTitleList.size();
            if(size > 0) {

                String title = mTitleList.get(size - 1);//获取当前位置网页的标题并设置
                tvTitle.setText(title);
            }
        }




    }

    @Override
    protected void onDestroy() {

        releaseWebView();
        super.onDestroy();
    }

    /**
     * 释放webview占用的资源，清除缓存
     */
    private void releaseWebView() {

        //清理缓存
        if(mWebView != null) {

            //放置webview ZoomButtonsController 引起的leak
            ((ViewGroup) getWindow().getDecorView()).removeAllViews();
            mWebView.setVisibility(View.GONE);
            mWebView.clearCache(true);
            mWebView.setWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.removeAllViews();
            mContentView.removeView(mWebView);
            mWebView.destroy();
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        //自动获取当前网页的标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mIsTitleAuto) {
                tvTitle.setText(title);//自动获取网页标题，并且设置
                Lg.d(title);
            }
            mTitleList.add(title);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
            super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            quotaUpdater.updateQuota(2L * requiredStorage);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                mWebView.reload(); //刷新当前页面
            } else {

                //未登录,退出当前页面
                finish();
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if(!isFirstLoad && TextUtils.equals(DMConstant.H5Url.MALL,mUrl)) {
//
//            mWebView.loadUrl("javascript:getUserBaseInfo()");
//        }
//        isFirstLoad = false;
//    }

    //当碰到页面有下载链接的时候，开启实现文件下载的功能
    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
