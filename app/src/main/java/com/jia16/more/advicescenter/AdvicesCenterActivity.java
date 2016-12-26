package com.jia16.more.advicescenter;

import android.os.Bundle;
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
 * 消息中心界面
 */
public class AdvicesCenterActivity extends BaseActivity {


    private FrameLayout mNoticeFrame; //公告
    private FrameLayout mNewsFrame;//新闻

    private TextView mNoticeTv;

    private TextView mNewsTv;


    private View mNoticeLine;

    private View mNewsLine;


    private ViewPager mAdvicesViewPager;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advices_center);

        //初始化布局
        initView();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        ((TextView)findViewById(R.id.title_text)).setText("消息中心");

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
    }


    private void initViewPager() {
        fragments.add(new AdvicesFragmnet());
        fragments.add(new NewsFragmnet());

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
            case R.id.notice_frame: //公告
                mAdvicesViewPager.setCurrentItem(0);
                break;
            case R.id.news_frame: //新闻
                mAdvicesViewPager.setCurrentItem(1);
                break;
            default:
                break;
        }

    }


}
