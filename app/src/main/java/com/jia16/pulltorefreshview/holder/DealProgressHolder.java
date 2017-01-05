package com.jia16.pulltorefreshview.holder;

/**
 * 主界面的holder
 */

import android.view.View;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.Advices;
import com.jia16.bean.DealProcess;
import com.jia16.util.AmountUtil;
import com.jia16.util.TimeUtils;

public class DealProgressHolder extends BaseHolder<DealProcess> {

    private TextView mTvAlldealState;//资金流水的状态
    private TextView mTvAlldealMoney;//资金流水的金额
    private TextView mTvAlldealData;//日期
    private TextView mTvAlldealDesc;//资金流水的描述
    private TextView mTvShopDesc;   //投资标的的描述

    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_deal_progress,null);
        mTvAlldealState = (TextView) view.findViewById(R.id.tv_alldeal_state);
        mTvAlldealMoney = (TextView) view.findViewById(R.id.tv_alldeal_money);
        mTvAlldealData = (TextView) view.findViewById(R.id.tv_alldeal_data);
        mTvAlldealDesc = (TextView) view.findViewById(R.id.tv_alldeal_desc);
        mTvShopDesc = (TextView) view.findViewById(R.id.tv_shop_desc);
        return view;
    }

    //绑定数据的操作
    public void bindData(DealProcess appinfo) {
        //资金流水的状态
        String type = appinfo.getType();
        if (type.equals("WITHDRAW")) {
            mTvAlldealState.setText("取现");
        } else if (type.equals("UNFROZEN")) {
            mTvAlldealState.setText("解冻");
        } else if (type.equals("FROZEN")) {
            mTvAlldealState.setText("冻结");
        } else if (type.equals("DEPOSIT")) {
            mTvAlldealState.setText("充值");
        } else if (type.equals("TRANSFER_OUT")) {
            mTvAlldealState.setText("转出");
        } else if (type.equals("USE_VOUCHER")) {
            mTvAlldealState.setText("使用代金券");
        } else if (type.equals("TRANSFER_IN")) {
            mTvAlldealState.setText("转入");
        }

        //资金流水的金额
        double amount = appinfo.getAmount().getAmount();
        mTvAlldealMoney.setText(AmountUtil.addComma(AmountUtil.DT.format(amount))+"元");

        //日期
        long createdAt = appinfo.getCreatedAt();
        String time = TimeUtils.getTime(createdAt, TimeUtils.DATE_FORMAT_DATE);
        mTvAlldealData.setText(time);

    }
}
