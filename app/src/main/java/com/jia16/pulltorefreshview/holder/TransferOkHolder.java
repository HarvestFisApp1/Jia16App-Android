package com.jia16.pulltorefreshview.holder;

/**
 * 的holder
 */

import android.view.View;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.Advices;
import com.jia16.bean.TransferOk;
import com.jia16.util.AmountUtil;
import com.jia16.util.TimeUtils;

public class TransferOkHolder extends BaseHolder<TransferOk> {

    private TextView mTvAssetsName;//转让标的名称
    private TextView mTvTransferMoney;//转让本金
    private TextView mTvDealAmount;//成交价格
    private TextView mTvTransferData;//转让时间


    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_invest_transfer_ok,null);
        mTvAssetsName = (TextView) view.findViewById(R.id.tv_assets_name);
        mTvTransferMoney = (TextView) view.findViewById(R.id.tv_transfer_money);
        mTvDealAmount = (TextView) view.findViewById(R.id.tv_deal_amount);
        mTvTransferData = (TextView) view.findViewById(R.id.tv_transfer_data);
        return view;
    }

    //绑定数据的操作
    public void bindData(TransferOk appinfo) {
        //转让标的名称
        mTvAssetsName.setText(appinfo.getTitle());

        //转让本金
        String transferrMoney = AmountUtil.addComma(AmountUtil.DT.format(appinfo.getAmount().getAmount()));
        mTvTransferMoney.setText(transferrMoney);

        //成交价格
        String transferAmount = AmountUtil.addComma(AmountUtil.DT.format(appinfo.getTransferAmount().getAmount()));
        mTvDealAmount.setText(transferAmount);

        //转让时间
        String time = TimeUtils.getTime(appinfo.getContract().getEffectDate(), TimeUtils.DATE_FORMAT_DATE);
        mTvTransferData.setText(time);

    }
}
