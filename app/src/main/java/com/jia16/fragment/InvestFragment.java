package com.jia16.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.base.BaseFragment;
import com.jia16.invest.iteminvest.EcpectYearFragmnet;
import com.jia16.invest.iteminvest.InvestDateFragmnet;
import com.jia16.invest.iteminvest.InvestMoneyFragmnet;
import com.jia16.invest.iteminvest.InvestProgressFragmnet;
import com.jia16.invest.iteminvest.TransFerRealizationFragmnet;
import com.jia16.more.helpercenter.MyInvestMentFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我要投资 - ---导航界面
 */
public class InvestFragment extends BaseFragment implements View.OnClickListener{

    private BaseApplication mContext;
    private LayoutInflater inflater;



    private List<Fragment> fragments = new ArrayList<Fragment>();
    private List<Fragment> fragments1 = new ArrayList<Fragment>();

    private ViewPager mHelperViewPager;
    private ViewPager mTransViewPager;

    /**
     * 帧布局
     */
    private FrameLayout mInvesterFrame1;//预期年化
    private FrameLayout mInvesterFrame2;//投资期限
    private FrameLayout mInvesterFrame3;//起投金额
    private FrameLayout mInvesterFrame4;//投资进度
    /**
     * 真布局描述
     */
    private TextView mTvInvest1;
    private TextView mTvInvest2;
    private TextView mTvInvest3;
    private TextView mTvInvest4;
    //描述条目的线性布局
    private LinearLayout mllInvestMethod;

    /**
     * 固定收益，个体网贷，转让变现的按钮
     */
    private RadioButton mrbCheck1;
    private RadioButton mrbCheck2;
    private RadioButton mrbCheck3;


    private FrameLayout mInvestFrame;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater=inflater;
        return inflater.inflate(R.layout.fragment_invest, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //同步应用程序当前版本的cookie
        synVersionNameCookie(getActivity());

        View mPatentView = getView();
        initView(mPatentView);

        if(BaseApplication.getInstance().isEarn){
            mrbCheck1.setChecked(true);
            mrbCheck2.setChecked(false);
        }else {
            mrbCheck1.setChecked(false);
            mrbCheck2.setChecked(true);
        }

    }

    private void initView(View view) {


        mHelperViewPager = (ViewPager)view.findViewById(R.id.helper_view_pager);
        mTransViewPager = (ViewPager)view.findViewById(R.id.transfer_view_pager);



        /**
         * 帧布局
         */
        mInvesterFrame1 = (FrameLayout) view.findViewById(R.id.invester_frame1);
        mInvesterFrame2 = (FrameLayout) view.findViewById(R.id.invester_frame2);
        mInvesterFrame3 = (FrameLayout) view.findViewById(R.id.invester_frame3);
        mInvesterFrame4 = (FrameLayout) view.findViewById(R.id.invester_frame4);

        mInvesterFrame1.setOnClickListener(this);
        mInvesterFrame2.setOnClickListener(this);
        mInvesterFrame3.setOnClickListener(this);
        mInvesterFrame4.setOnClickListener(this);

       mInvestFrame = (FrameLayout) view.findViewById(R.id.invest_frame);
        mInvestFrame.setOnClickListener(this);

        /**
         * 真布局描述
         */
        mTvInvest1 = (TextView) view.findViewById(R.id.tv_invest1);
        mTvInvest2 = (TextView) view.findViewById(R.id.tv_invest2);
        mTvInvest3 = (TextView) view.findViewById(R.id.tv_invest3);
        mTvInvest4 = (TextView) view.findViewById(R.id.tv_invest4);

        //描述条目的线性布局，当个人网贷选中时，那么就隐藏掉线性布局
        mllInvestMethod = (LinearLayout) view.findViewById(R.id.ll_invest_method);

        //固定收益，个体网贷，转让变现的按钮
        mrbCheck1 = (RadioButton) view.findViewById(R.id.rb_check1);
        mrbCheck2 = (RadioButton) view.findViewById(R.id.rb_check2);
        mrbCheck3 = (RadioButton) view.findViewById(R.id.rb_check3);



        initViewPager1();

        initViewPager();

        //绑定数据
        initDate();
    }

    /**
     * 绑定数据
     */
    private void initDate() {

        mrbCheck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mrbCheck1.setChecked(true);
                mrbCheck2.setChecked(false);
                mrbCheck3.setChecked(false);
                mllInvestMethod.setVisibility(View.VISIBLE);

                BaseApplication.getInstance().isEarn=true;

                //发送广播，请求按固定收益排序的数据
                Intent intent=new Intent();
                intent.setAction("invest_fixation_earn");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                //intent.putExtra("isEarn",true);
                getActivity().sendBroadcast(intent);

                //隐藏和显示相应的viewpager
                mTransViewPager.setVisibility(View.GONE);
                mHelperViewPager.setVisibility(View.VISIBLE);
            }
        });

        mrbCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mrbCheck1.setChecked(false);
                mrbCheck2.setChecked(true);
                mrbCheck3.setChecked(false);
                mllInvestMethod.setVisibility(View.VISIBLE);


                BaseApplication.getInstance().isEarn=false;
                //发送广播，请求按个体网贷排序的数据
                Intent intent=new Intent();
                intent.setAction("invest_personage_loan");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                //intent.putExtra("isEarn",false);
                getActivity().sendBroadcast(intent);


                //隐藏和显示相应的viewpager
                mTransViewPager.setVisibility(View.GONE);
                mHelperViewPager.setVisibility(View.VISIBLE);
            }
        });
        mrbCheck3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mrbCheck1.setChecked(false);
                mrbCheck2.setChecked(false);
                mrbCheck3.setChecked(true);
                //表示转让变现按钮选中，那么就隐藏掉线性布局
                mllInvestMethod.setVisibility(View.GONE);

                //点击转让变现，发送广播，刷新数据
                Intent intent=new Intent();
                intent.setAction("invest_ftransfer_money");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                getActivity().sendBroadcast(intent);

                //隐藏和显示相应的viewpager
                mTransViewPager.setVisibility(View.VISIBLE);
                mHelperViewPager.setVisibility(View.GONE);
            }
        });

    }


    private void initViewPager() {
        fragments.add(new EcpectYearFragmnet());   //预期年化
        fragments.add(new InvestDateFragmnet());   //投资期限
        fragments.add(new InvestMoneyFragmnet());  //起投金额
        fragments.add(new InvestProgressFragmnet());//投资进度

        MyInvestMentFragmentAdapter adapter =
                new MyInvestMentFragmentAdapter(fragments, getActivity().getSupportFragmentManager(), mHelperViewPager);
        adapter.setOnExtraPageChangeListener(new MyInvestMentFragmentAdapter.OnExtraPageChangeListener() {
            public void onExtraPageSelected(int i) {
                switchBtnList(i);
            }
        });
    }

    private void initViewPager1() {
        fragments1.add(new TransFerRealizationFragmnet()); //转让变现

        MyInvestMentFragmentAdapter adapter =
                new MyInvestMentFragmentAdapter(fragments1, getActivity().getSupportFragmentManager(), mTransViewPager);
        adapter.setOnExtraPageChangeListener(new MyInvestMentFragmentAdapter.OnExtraPageChangeListener() {
            public void onExtraPageSelected(int i) {
                //switchBtnList(i);
            }
        });
    }

    private void switchBtnList(int index) {
        switch (index) {
            case 0:
                mTvInvest1.setTextColor(getResources().getColor(R.color.main_color));

                mTvInvest2.setTextColor(getResources().getColor(R.color.text_gray));

                mTvInvest3.setTextColor(getResources().getColor(R.color.text_gray));

                mTvInvest4.setTextColor(getResources().getColor(R.color.text_gray));

                break;
            case 1:
                mTvInvest1.setTextColor(getResources().getColor(R.color.text_gray));

                mTvInvest2.setTextColor(getResources().getColor(R.color.main_color));

                mTvInvest3.setTextColor(getResources().getColor(R.color.text_gray));

                mTvInvest4.setTextColor(getResources().getColor(R.color.text_gray));
                break;
            case 2:
                mTvInvest1.setTextColor(getResources().getColor(R.color.text_gray));

                mTvInvest2.setTextColor(getResources().getColor(R.color.text_gray));

                mTvInvest3.setTextColor(getResources().getColor(R.color.main_color));

                mTvInvest4.setTextColor(getResources().getColor(R.color.text_gray));
                break;

            case 3:
                mTvInvest1.setTextColor(getResources().getColor(R.color.text_gray));

                mTvInvest2.setTextColor(getResources().getColor(R.color.text_gray));

                mTvInvest3.setTextColor(getResources().getColor(R.color.text_gray));

                mTvInvest4.setTextColor(getResources().getColor(R.color.main_color));
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invester_frame1: //预期年化
                mHelperViewPager.setCurrentItem(0);
                break;
            case R.id.invester_frame2: //投资期限
                mHelperViewPager.setCurrentItem(1);
                break;
            case R.id.invester_frame3: //起投金额
                mHelperViewPager.setCurrentItem(2);
                break;

            case R.id.invester_frame4: //投资进度
                mHelperViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
