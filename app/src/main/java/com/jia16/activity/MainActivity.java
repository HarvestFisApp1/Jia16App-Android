package com.jia16.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.fragment.AssetsFragment;
import com.jia16.fragment.HomeFragment;
import com.jia16.fragment.InvestFragment;
import com.jia16.fragment.MoreFragmnet;
import com.jia16.util.AlertUtil;
import com.jia16.util.AppManager;
import com.jia16.view.DMFragmentTabHost;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends BaseActivity {

    /**
     * 跳转到登录
     */
    public static final int LoginRequestCode=100;

    public static final int AssetsLoginRequestCode=200;

    /**
     * 用于切换tab页面
     */
    public static final String SWITCH_INDEX_ACTION = "com.zhuoerjinfu.std.switch_index";
    public static DMFragmentTabHost mTabHost = null;
    /**
     * 指定当前在哪一页
     */
    public static int index=0;
    private View indicator = null;
    private String[] titles = null;
    private Class[] clazzs = {HomeFragment.class, InvestFragment.class, AssetsFragment.class, MoreFragmnet.class};

    /***
     * index const
     */
    public static final int HOME_FRAGMENT = 0;
    public static final int LEND_FRAGMENT = 1;
    public static final int FINANCE_FRAGMENT = 2;
    public static final int MY_FRAGMENT = 3;

    public static final String MAIN_PAGE_TAB_CHANGED = "main_page_tab_changed";
    public static final String MAIN_PAGE_INDEX = "main_page_index";

    private int[] imagesR = {R.drawable.tab_home_img, R.drawable.tab_finance_img, R.drawable.tab_invest_img, R.drawable.tab_mine_img};
    private String preTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        index = getIntent().getIntExtra("index",0);

        mTabHost = (DMFragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        titles = getResources().getStringArray(R.array.bottom_titles);
        for (int i=0;i<titles.length;i++){
            String title = titles[i];
            Class clazz = clazzs[i];
            String tabSpace = "tabSpace" + i;
            View indicator = getIndicatorView(title, R.layout.common_tab_indicator, imagesR[i]);
            mTabHost.addTab(mTabHost.newTabSpec(tabSpace).setIndicator(indicator),clazz,null);
        }

//        mTabHost.getTabWidget().getChildAt(3).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if(!BaseApplication.getInstance().isLogined()){//表示没有登录，那么就跳转到登录页面
//                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                    startActivityForResult(intent,LoginRequestCode);
//                }else {
//                    mTabHost.setCurrentTab(3);
//                }
//            }
//        });

        mTabHost.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!BaseApplication.getInstance().isLogined()){
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivityForResult(intent,AssetsLoginRequestCode);
                }else {
                    mTabHost.setCurrentTab(2);
                }
            }
        });

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("tabSpace0")){
                    index=0;
                    mTabHost.setCurrentTabByTag(preTab);
                }else if(tabId.equals("tabSpace1")){
                    index=1;
                    mTabHost.setCurrentTabByTag(preTab);
                }else if(tabId.equals("tabSpace2")){
                    index=2;
                    mTabHost.setCurrentTabByTag(preTab);
                }else if(tabId.equals("tabSpace3")){
                    index=3;
                    mTabHost.setCurrentTabByTag(preTab);
                }else{
                    preTab=tabId;
                    index=mTabHost.getCurrentTab();
                }
            }
        });

        mTabHost.setCurrentTab(index);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case LoginRequestCode://登录成功后停留在金融助理页面
                    index=3;
                    break;
                case AssetsLoginRequestCode:
                    //index=2;
                    mTabHost.setCurrentTab(2);
                break;
                default:
                    break;
            }
        }
    }



    private View getIndicatorView(String name, int layoutId, int drawableId) {
        View v = getLayoutInflater().inflate(layoutId, null);
        TextView tv = (TextView) v.findViewById(R.id.tabText);
        tv.setText(name);
        ImageView imageView = (ImageView) v.findViewById(R.id.tabImage);
        imageView.setImageResource(drawableId);
        return v;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTabHost.setCurrentTab(index);

        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent!=null){
            index=intent.getIntExtra("index",0);
        }
        mTabHost.setCurrentTab(index);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击手机物理返回键
        if(keyCode==KeyEvent.KEYCODE_BACK){
            AlertUtil.showTwoBtnDialog(MainActivity.this, "确定要退出?", new AlertUtil.Config("确定", "取消"), new AlertUtil.DialogListener() {
                @Override
                public void onLeftClick(AlertDialog dlg) {
                    AppManager.getAppManager().AppExit(MainActivity.this);
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
        return super.onKeyDown(keyCode, event);
    }
}
