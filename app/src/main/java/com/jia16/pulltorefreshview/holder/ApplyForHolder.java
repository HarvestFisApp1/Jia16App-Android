package com.jia16.pulltorefreshview.holder;

/**
 * 申请中的holder
 */

import android.view.View;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.ApplyFor;
import com.jia16.util.AmountUtil;
import com.jia16.util.TimeUtils;

public class ApplyForHolder extends BaseHolder<ApplyFor> {

    private TextView mTvAssetsName;//标的名称
    private TextView mTvInvetAmount;//投资金额
    private TextView mTvDeadline;//投资期限
    private TextView mTvInvestDate;//投资日期


    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_apply_for_item,null);
        mTvAssetsName = (TextView) view.findViewById(R.id.tv_assets_name);
        mTvInvetAmount = (TextView) view.findViewById(R.id.tv_invet_amount);
        mTvDeadline = (TextView) view.findViewById(R.id.tv_deadline);
        mTvInvestDate = (TextView) view.findViewById(R.id.tv_invest_date);
        return view;
    }

    //绑定数据的操作
    public void bindData(ApplyFor appinfo) {
        //设置标的名称
        String title = appinfo.getSubject().getTitle();
        mTvAssetsName.setText(title);

        //设置投资金额
        double amount = appinfo.getInvestmentAmount().getAmount();
        mTvInvetAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(amount)));

        //设置投资期限
        int count = appinfo.getSubject().getInstalmentPolicy().getInterval().getCount();
        mTvDeadline.setText(count+"");

        //设置投资时间
        long investAt = appinfo.getInvestAt();
        String time = TimeUtils.getTime(investAt, TimeUtils.DATE_FORMAT_DATE);
        mTvInvestDate.setText(time);
    }
}
