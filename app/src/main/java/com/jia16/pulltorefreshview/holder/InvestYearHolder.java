package com.jia16.pulltorefreshview.holder;

/**
 * 主界面的holder
 */

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.InvestConstant;
import com.jia16.util.AmountUtil;
import com.jia16.view.RoundProgressBar;

public class InvestYearHolder extends BaseHolder<InvestConstant> {

    private TextView mAssetsTitle;//标的名称
    private TextView mTvWelfare;//是否可以使用代金券
    private TextView mTvAssetsState;//是否可以转让或满标
    private TextView mTvYearEarn;//年化收益
    private TextView mTvBeginMoney;//起投金额
    private TextView mTvInvestDate;//投资期限
    private RoundProgressBar mRbProgress;//进度条
    private TextView mTvEarnDesc;//投资收益描述
    //RoundProgressBar progressBar=new RoundProgressBar(BaseApplication.getInstance());


    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_invest_year_item,null);
        mAssetsTitle = (TextView) view.findViewById(R.id.assets_title);
        mTvWelfare = (TextView) view.findViewById(R.id.tv_welfare);
        mTvAssetsState = (TextView) view.findViewById(R.id.tv_assets_state);
        mTvYearEarn = (TextView) view.findViewById(R.id.tv_year_earn);
        mTvBeginMoney = (TextView) view.findViewById(R.id.tv_begin_money);
        mTvInvestDate = (TextView) view.findViewById(R.id.tv_invest_date);
        mRbProgress = (RoundProgressBar) view.findViewById(R.id.rb_progress);
        mTvEarnDesc = (TextView) view.findViewById(R.id.tv_earn_desc);
        return view;
    }

    //绑定数据的操作
    public void bindData(InvestConstant appinfo) {
        //标的名称
        mAssetsTitle.setText(appinfo.getTitle());


        //如果已经满标，那么久要隐藏券，和可转让标识，显示已满标
        String status = appinfo.getStatus();
        //是否可以使用代金券
        String canUseVoucherTag = appinfo.getCanUseVoucherTag();
        if(canUseVoucherTag.equals("canUseVoucher")){
            if(status.equals("FUNDING")){//没有满标
                mTvWelfare.setVisibility(View.VISIBLE);
            }else if(status.equals("SIGNED")){//已满标
                mTvWelfare.setVisibility(View.GONE);
            }
        }else if(canUseVoucherTag.equals("noUseVoucher")){
            mTvWelfare.setVisibility(View.GONE);
        }


        //是否可以转让或满标
        boolean transferable = appinfo.isTransferable();
        if(transferable){

            if(status.equals("FUNDING")){//没有满标
                mTvAssetsState.setVisibility(View.VISIBLE);
                mTvAssetsState.setText("可转让");
                mTvAssetsState.setBackgroundColor(Color.parseColor("#ff0000"));
            }else if(status.equals("SIGNED")){//已满标
                mTvAssetsState.setVisibility(View.VISIBLE);
                mTvAssetsState.setText("已满标");
                mTvAssetsState.setBackgroundColor(Color.parseColor("#ff666666"));
            }
        }else {
            mTvAssetsState.setVisibility(View.GONE);
        }



        //年化收益
        double annuals = appinfo.getInstalmentPolicy().getAnnualRate();
        String annualRate = AmountUtil.addComma(AmountUtil.DT.format(annuals * 100));
        mTvYearEarn.setText(annualRate+"%");

        //起投金额
        mTvBeginMoney.setText("起投 "+appinfo.getInvestmentPolicy().getMinimumInvestmentAmount().getAmount()+"元");
        //投资期限
        int count = appinfo.getInstalmentPolicy().getInterval().getCount();
        mTvInvestDate.setText("期限 "+count+"天");

        //进度条
        mRbProgress.setMax(appinfo.getAmount().getAmount());//设置进度条的最大值
        mRbProgress.setProgress(appinfo.getCurrentInvestmentAmount().getAmount());//设置当前进度


        //投资收益描述
        String tagName = appinfo.getConfig().getTagName();
        mTvEarnDesc.setText(tagName);
    }
}
