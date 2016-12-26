package com.jia16.more.helpercenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮助中心界面界面
 */
public class HelperCenterActivity extends BaseActivity {


    private FrameLayout mInvesterFrame; //投资指南
    private FrameLayout mCommonFrame;//常见问题
    private FrameLayout mMobileFrame;//安全保障

    private TextView mInvesterTv;

    private TextView mCommonTv;

    private TextView mMobileTv;

    private View mInvesterLine;

    private View mCommonLine;

    private View mMobileLine;

    private ViewPager mHelperViewPager;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_center);

        //初始化布局
        initView();
        //绑定数据
        initData();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        ((TextView)findViewById(R.id.title_text)).setText("帮助中心");

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHelperViewPager = (ViewPager) findViewById(R.id.helper_view_pager);

        mInvesterFrame = (FrameLayout) findViewById(R.id.invester_frame);
        mCommonFrame = (FrameLayout) findViewById(R.id.common_frame);
        mMobileFrame = (FrameLayout) findViewById(R.id.mobile_frame);

        mInvesterFrame.setOnClickListener(this);
        mCommonFrame.setOnClickListener(this);
        mMobileFrame.setOnClickListener(this);

        mInvesterTv = (TextView) findViewById(R.id.invester_tv);
        mCommonTv = (TextView) findViewById(R.id.common_tv);
        mMobileTv = (TextView) findViewById(R.id.mobile_tv);

        mInvesterLine = findViewById(R.id.invester_line);
        mCommonLine = findViewById(R.id.common_line);
        mMobileLine = findViewById(R.id.mobile_line);


        initViewPager();
    }

    /**
     * 绑定数据
     */
    private void initData() {


    }

    private void initViewPager() {
        fragments.add(new InversterFragmnet());
        fragments.add(new CommonFragmnet());
        fragments.add(new MobileFragmnet());

        MyInvestMentFragmentAdapter adapter =
                new MyInvestMentFragmentAdapter(fragments, this.getSupportFragmentManager(), mHelperViewPager);
        adapter.setOnExtraPageChangeListener(new MyInvestMentFragmentAdapter.OnExtraPageChangeListener() {
            public void onExtraPageSelected(int i) {
                switchBtnList(i);
            }
        });
    }

    private void switchBtnList(int index) {
        switch (index) {
            case 0:
                mInvesterTv.setTextColor(getResources().getColor(R.color.main_color));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.text_gray));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                break;
            case 1:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.main_color));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));
                break;
            case 2:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.text_gray));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.main_color));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));
                break;

            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invester_frame: //投资指南
                mHelperViewPager.setCurrentItem(0);
                break;
            case R.id.common_frame: //常见问题
                mHelperViewPager.setCurrentItem(1);
                break;
            case R.id.mobile_frame: //安全保障
                mHelperViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }

    }


}
