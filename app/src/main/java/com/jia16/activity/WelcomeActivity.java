package com.jia16.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.util.Constants;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * 欢迎、指引页面
 *
 * @author jiaohongyun
 * @date 2015年5月22日
 */
public class WelcomeActivity extends BaseActivity implements OnClickListener {

    private ViewPager viewPager;

    private MyPagerAdapter myAdapter;

    private LayoutInflater mInflater;

    private ArrayList<View> views;

    int[] images = {R.mipmap.welcome1, R.mipmap.welcome2, R.mipmap.welcome3, R.mipmap.welcome4};

    private ImageView point1;

    private ImageView point2;

    private ImageView point3;

    private ImageView point4;


    private Button start;
    private Button startRegister;
    private ImageView mWelcomeClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        initView();
    }

    /**
     * 初始化视图
     */
    @SuppressLint("InflateParams")
    protected void initView() {
//        findViewById(R.id.welcomeClose).setOnClickListener(this);
        mWelcomeClose = (ImageView) findViewById(R.id.welcomeClose);
        mWelcomeClose.setOnClickListener(this);
        point1 = (ImageView) findViewById(R.id.loading_point_1);
        point2 = (ImageView) findViewById(R.id.loading_point_2);
        point3 = (ImageView) findViewById(R.id.loading_point_3);
        point4 = (ImageView) findViewById(R.id.loading_point_4);

        point1.setOnClickListener(this);
        point2.setOnClickListener(this);
        point3.setOnClickListener(this);
        point4.setOnClickListener(this);

        views = new ArrayList<>(2);
        myAdapter = new MyPagerAdapter(views);
        viewPager = (ViewPager) findViewById(R.id.welcome_viewpager);
        mInflater = getLayoutInflater();
        for (int i = 0; i < 4; i++) {

            ImageView imageView = new ImageView(this);
            InputStream inputStream = this.getResources().openRawResource(images[i]);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            //默认应该是ARGB_8888，比较占用内存
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //让系统及时回收
            options.inPurgeable = true;
            options.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(imageView);
        }
        start = (Button) findViewById(R.id.start_btn);
        start.setOnClickListener(this);
        startRegister = (Button) findViewById(R.id.start_bt);
        startRegister.setOnClickListener(this);

        viewPager.setAdapter(myAdapter);
        //初始化当前显示的view
        viewPager.setCurrentItem(0);

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    start.setVisibility(View.GONE);
                    startRegister.setVisibility(View.GONE);
                    point1.setImageResource(R.mipmap.icon_page_yellow_dot);
                    point2.setImageResource(R.mipmap.icon_page_blue_dot);
                    point3.setImageResource(R.mipmap.icon_page_blue_dot);
                    point4.setImageResource(R.mipmap.icon_page_blue_dot);
                    mWelcomeClose.setVisibility(View.GONE);
                } else if (arg0 == 1) {
                    start.setVisibility(View.GONE);
                    startRegister.setVisibility(View.GONE);
                    point1.setImageResource(R.mipmap.icon_page_blue_dot);
                    point2.setImageResource(R.mipmap.icon_page_yellow_dot);
                    point3.setImageResource(R.mipmap.icon_page_blue_dot);
                    point4.setImageResource(R.mipmap.icon_page_blue_dot);
                    mWelcomeClose.setVisibility(View.GONE);
                } else if (arg0 == 2) {
                    start.setVisibility(View.GONE);
                    startRegister.setVisibility(View.GONE);
                    point1.setImageResource(R.mipmap.icon_page_blue_dot);
                    point2.setImageResource(R.mipmap.icon_page_blue_dot);
                    point3.setImageResource(R.mipmap.icon_page_yellow_dot);
                    point4.setImageResource(R.mipmap.icon_page_blue_dot);
                    mWelcomeClose.setVisibility(View.GONE);
                } else if (arg0 == 3) {
                    start.setVisibility(View.VISIBLE);
                    startRegister.setVisibility(View.VISIBLE);
                    point1.setImageResource(R.mipmap.icon_page_blue_dot);
                    point2.setImageResource(R.mipmap.icon_page_blue_dot);
                    point3.setImageResource(R.mipmap.icon_page_blue_dot);
                    point4.setImageResource(R.mipmap.icon_page_yellow_dot);
                    mWelcomeClose.setVisibility(View.VISIBLE);
                } else {
                    start.setVisibility(View.GONE);
                    startRegister.setVisibility(View.GONE);
                    point1.setImageResource(R.mipmap.icon_page_blue_dot);
                    point2.setImageResource(R.mipmap.icon_page_blue_dot);
                    point3.setImageResource(R.mipmap.icon_page_blue_dot);
                    mWelcomeClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!checkClick(v.getId())) {
            return;
        }
        switch (v.getId()) {
            case R.id.loading_point_1:
                viewPager.setCurrentItem(0);
                point1.setImageResource(R.mipmap.icon_page_yellow_dot);
                point2.setImageResource(R.mipmap.icon_page_blue_dot);
                point3.setImageResource(R.mipmap.icon_page_blue_dot);
                point4.setImageResource(R.mipmap.icon_page_blue_dot);
                break;
            case R.id.loading_point_2:
                viewPager.setCurrentItem(1);
                point1.setImageResource(R.mipmap.icon_page_blue_dot);
                point2.setImageResource(R.mipmap.icon_page_yellow_dot);
                point3.setImageResource(R.mipmap.icon_page_blue_dot);
                point4.setImageResource(R.mipmap.icon_page_blue_dot);
                break;
            case R.id.loading_point_3:
                viewPager.setCurrentItem(2);
                point1.setImageResource(R.mipmap.icon_page_blue_dot);
                point2.setImageResource(R.mipmap.icon_page_blue_dot);
                point3.setImageResource(R.mipmap.icon_page_yellow_dot);
                point4.setImageResource(R.mipmap.icon_page_blue_dot);
                break;
            case R.id.loading_point_4:
                viewPager.setCurrentItem(3);
                point1.setImageResource(R.mipmap.icon_page_blue_dot);
                point2.setImageResource(R.mipmap.icon_page_blue_dot);
                point3.setImageResource(R.mipmap.icon_page_blue_dot);
                point4.setImageResource(R.mipmap.icon_page_yellow_dot);
                break;
            case R.id.start_btn:
                //跳转到首页
                gotoMainPage();
                break;
            case R.id.welcomeClose:
                //跳转到首页
                //gotoMainPage();
                break;

            case R.id.start_bt:
                //跳转到注册页面
                gotoRegisterPage();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到首页
     */
    private void gotoMainPage() {
        sharedPreferences.edit().putBoolean("isFirstRun", false).apply();
        //跳转到注册页面
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
        finish();
    }



    /**
     * 跳转到注册页面
     */
    private void gotoRegisterPage() {

        sharedPreferences.edit().putBoolean("isFirstRun", false).apply();
        //跳转到注册页面
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("targetUrl", Constants.HOME_PAGE+"/#!register");
        startActivity(intent);
        finish();
    }

    private class MyPagerAdapter extends PagerAdapter {
        // 界面列表
        private ArrayList<View> views;

        public MyPagerAdapter(ArrayList<View> views) {
            this.views = views;
        }

        /**
         * 获得当前界面数
         */
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        /**
         * 初始化position位置的界面
         */
        @Override
        public Object instantiateItem(View view, int position) {
            ((ViewPager) view).addView(views.get(position), 0);
            View temp = views.get(position);
            temp.setVisibility(View.VISIBLE);
            return temp;
        }

        /**
         * 判断是否由对象生成界面
         */
        @Override
        public boolean isViewFromObject(View view, Object arg1) {
            return (view == arg1);
        }

        /**
         * 销毁position位置的界面
         */
        @Override
        public void destroyItem(View view, int position, Object arg2) {
            ((ViewPager) view).removeView(views.get(position));
        }
    }
}
