package com.jia16.pulltorefreshview.holder;

/**
 * 我要投资--转让变现的holder
 */

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.assets.MyTotalAconutActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.TransferMoney;
import com.jia16.invest.MyNeedInvestActivity;
import com.jia16.util.AmountUtil;

public class TransferMoneyHolder extends BaseHolder<TransferMoney> {

    private TextView mAssetsTitle;//标的名称
    private TextView mTvFullSignState;//是否满标状态
    private TextView mTvYearEarn;//年化收益
    private TextView mTvBeginMoney;//转让总额
    private TextView mTvTransferDate;//期限
    private LinearLayout mllTransferContent;


    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_transfer_money,null);
        mAssetsTitle = (TextView) view.findViewById(R.id.assets_title);
        mTvFullSignState = (TextView) view.findViewById(R.id.tv_full_sign_state);
        mTvYearEarn = (TextView) view.findViewById(R.id.tv_year_earn);
        mTvBeginMoney = (TextView) view.findViewById(R.id.tv_begin_money);
        mTvTransferDate = (TextView) view.findViewById(R.id.tv_transfer_date);
        mllTransferContent = (LinearLayout) view.findViewById(R.id.ll_transfer_content);
        return view;
    }

    //绑定数据的操作
    public void bindData(final TransferMoney appinfo) {
        //标的名称
        mAssetsTitle.setText(appinfo.getTitle());

        //是否满标状态
        boolean transferable = appinfo.isTransferable();
        if(transferable){
            //表示还没有满标，那么就隐藏已满标的布局
            mTvFullSignState.setVisibility(View.GONE);
        }else {
            mTvFullSignState.setVisibility(View.VISIBLE);
        }

        //年化收益
        double annualRate = appinfo.getInstalmentPolicy().getAnnualRate();
        String annualrate = AmountUtil.addComma(AmountUtil.DT.format(annualRate * 100));
        mTvYearEarn.setText(annualrate+"%");

        //转让总额
        double amount = appinfo.getInvestmentPolicy().getInvestmentAmount().getAmount();
        String transferAmount = AmountUtil.addComma(AmountUtil.DT.format(amount));
        mTvBeginMoney.setText(transferAmount+"元");

        //期限
        mTvTransferDate.setText(appinfo.getInstalmentPolicy().getInterval().getCount()+"天");

        //条目的点击事件
        mllTransferContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BaseApplication.getInstance(), MyNeedInvestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title",appinfo.getTitle());
                intent.putExtra("rightTitle","转让变现");
                BaseApplication.getInstance().startActivity(intent);
            }
        });
    }
}
