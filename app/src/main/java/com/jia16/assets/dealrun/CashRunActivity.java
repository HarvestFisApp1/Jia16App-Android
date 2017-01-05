package com.jia16.assets.dealrun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.more.helpercenter.MyInvestMentFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的资产--资金流水界面
 */
public class CashRunActivity extends BaseActivity {

    private FrameLayout mNoticeFrame; //全部交易
    private FrameLayout mNewsFrame;//在途交易

    public TextView mNoticeTv;

    private TextView mNewsTv;


    private View mNoticeLine;

    private View mNewsLine;


    private ViewPager mAdvicesViewPager;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    private int userId;

    private BroadcastReceiver receiver1;

    private BroadcastReceiver receiver2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_run);


        //初始化布局
        initView();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        userId = getIntent().getIntExtra("userId", 0);



        ((TextView)findViewById(R.id.title_text)).setText("资金流水");

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdvicesViewPager = (ViewPager) findViewById(R.id.advices_view_pager);

        mNoticeFrame = (FrameLayout) findViewById(R.id.notice_frame);
        mNewsFrame = (FrameLayout) findViewById(R.id.news_frame);

        mNoticeFrame.setOnClickListener(this);
        mNewsFrame.setOnClickListener(this);

        mNoticeTv = (TextView) findViewById(R.id.notice_tv);
        mNewsTv = (TextView) findViewById(R.id.news_tv);

        mNoticeLine = findViewById(R.id.notice_line);
        mNewsLine = findViewById(R.id.news_line);


        initViewPager();

        //全部交易流水界面发送过来的广播
        registereReceiver1();

        //在途交易流水界面发送过来的广播
        registereReceiver2();
    }


    /**
     * 全部交易流水界面发送过来的广播
     */
    private void registereReceiver1() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("all_deal_investment");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String totalCount = intent.getStringExtra("totalCount");
                        //设置交易流水的数量
                        mNoticeTv.setText("全部交易("+totalCount+")");
                    }
                },0);
            }
        };
        registerReceiver(receiver1,intentFilter);
    }


    /**
     * 全部交易流水界面发送过来的广播
     */
    private void registereReceiver2() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("proceed_deal_investment");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String totalCount = intent.getStringExtra("totalCount");
                        //设置交易流水的数量
                        mNewsTv.setText("在途交易("+totalCount+")");
                    }
                },0);
            }
        };
        registerReceiver(receiver2,intentFilter);
    }


    @Override
    protected void onDestroy() {
        //界面销毁时，取消广播注册
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        super.onDestroy();
    }

    private void initViewPager() {
        fragments.add(new AllDealFragmnet());
        fragments.add(new ProceedDealFragmnet());

        MyInvestMentFragmentAdapter adapter =
                new MyInvestMentFragmentAdapter(fragments, this.getSupportFragmentManager(), mAdvicesViewPager);
        adapter.setOnExtraPageChangeListener(new MyInvestMentFragmentAdapter.OnExtraPageChangeListener() {
            public void onExtraPageSelected(int i) {
                switchBtnList(i);
            }
        });
    }

    private void switchBtnList(int index) {
        switch (index) {
            case 0:
                mNoticeTv.setTextColor(getResources().getColor(R.color.main_color));
                mNoticeLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mNewsTv.setTextColor(getResources().getColor(R.color.text_gray));
                mNewsLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                break;
            case 1:
                mNoticeTv.setTextColor(getResources().getColor(R.color.text_gray));
                mNoticeLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mNewsTv.setTextColor(getResources().getColor(R.color.main_color));
                mNewsLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                break;

            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notice_frame: //全部交易
                mAdvicesViewPager.setCurrentItem(0);
                break;
            case R.id.news_frame: //在途交易
                mAdvicesViewPager.setCurrentItem(1);
                break;
            default:
                break;
        }

    }




}
