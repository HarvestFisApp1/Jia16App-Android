package com.jia16.pulltorefreshview.holder;

/**
 * 资金流水--全部交易的holder
 */

import android.view.View;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.AllDeal;
import com.jia16.util.AmountUtil;
import com.jia16.util.TimeUtils;

public class AllDealHolder extends BaseHolder<AllDeal> {

    private TextView mTvAlldealState;//资金流水的状态
    private TextView mTvAlldealMoney;//资金流水的金额
    private TextView mTvAlldealData;//日期
    private TextView mTvAlldealDesc;//资金流水的描述
    private TextView mTvShopDesc;   //投资标的的描述

    //初始化holderView
    public View initHolderView() {
        View view = View.inflate(BaseApplication.getInstance(), R.layout.item_alldeal_item, null);
        mTvAlldealState = (TextView) view.findViewById(R.id.tv_alldeal_state);
        mTvAlldealMoney = (TextView) view.findViewById(R.id.tv_alldeal_money);
        mTvAlldealData = (TextView) view.findViewById(R.id.tv_alldeal_data);
        mTvAlldealDesc = (TextView) view.findViewById(R.id.tv_alldeal_desc);
        mTvShopDesc = (TextView) view.findViewById(R.id.tv_shop_desc);
        return view;
    }

    //绑定数据的操作
    public void bindData(AllDeal appinfo) {

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
//        String amount = appinfo.getAmount().getAmount();
//        if (amount.contains(".")) {
//            mTvAlldealMoney.setText(amount + "元");
//        } else {
//            mTvAlldealMoney.setText(amount + ".00元");
//        }
        String amount = appinfo.getAmount().getAmount();
        double amounts = Double.parseDouble(amount);
        mTvAlldealMoney.setText(AmountUtil.addComma(AmountUtil.DT.format(amounts)));


        //日期
        long createdAt = appinfo.getCreatedAt();
        String time = TimeUtils.getTime(createdAt, TimeUtils.DATE_FORMAT_DATE);
        mTvAlldealData.setText(time);



        //资金流水的描述
        String remark = (String) appinfo.getItems().get(0).getRemark();

        if (type.equals("WITHDRAW")) {//取现
            mTvShopDesc.setVisibility(View.GONE);
            if (amount.contains(".")) {
                mTvAlldealDesc.setText("线上取现 " + amount + "元");
            } else {
                mTvAlldealDesc.setText("线上取现 " + amount + ".00元");
            }
        } else if (type.equals("USE_VOUCHER")) {//使用代金券
            mTvAlldealDesc.setText("代金券 " + amount + "元");
        }else if(type.equals("DEPOSIT")){//充值
            mTvShopDesc.setVisibility(View.GONE);
            if(remark!=null){
                mTvAlldealDesc.setText( amount + "元");
            }else {
                mTvAlldealDesc.setText("充值 "+ amount + "元");
            }
        }else if(type.equals("UNFROZEN")){//解冻
            //投资时，扣除的实际金额(代金券)
            String amount1 = appinfo.getItems().get(0).getAmount().getAmount();
            if(remark!=null){
                mTvShopDesc.setVisibility(View.VISIBLE);
                mTvShopDesc.setText(remark+" ");
                mTvAlldealDesc.setText("投资成功放款解冻 "+ amount1 + "元");
            }else {
                mTvShopDesc.setVisibility(View.GONE);
                mTvAlldealDesc.setText("取现成功解冻 "+ amount1 + "元");
            }
        }else if (type.equals("TRANSFER_IN")) {//转入
            //投资时，扣除的实际金额(代金券)
            String amount1 = appinfo.getItems().get(0).getAmount().getAmount();
            if(remark!=null){
                mTvShopDesc.setVisibility(View.VISIBLE);
                mTvShopDesc.setText(remark+" ");
                mTvAlldealDesc.setText("本金 "+ amount1 + "元");
            }else {
                mTvShopDesc.setVisibility(View.GONE);
                mTvAlldealDesc.setText("本金 "+ amount1 + "元");
            }
        } else if (type.equals("TRANSFER_OUT")) {//转出
            //投资时，扣除的实际金额(代金券)
            String amount1 = appinfo.getItems().get(0).getAmount().getAmount();
            if(remark!=null){
                mTvShopDesc.setVisibility(View.VISIBLE);
                mTvShopDesc.setText(remark+" ");
                mTvAlldealDesc.setText("放款 "+ amount1 + "元");
            }else {
                mTvShopDesc.setVisibility(View.GONE);
                mTvAlldealDesc.setText("放款 "+ amount1 + "元");
            }
        }else if (type.equals("FROZEN")) {//冻结
            //投资时，扣除的实际金额(代金券)
            String amount1 = appinfo.getItems().get(0).getAmount().getAmount();
            if(remark!=null){
                mTvShopDesc.setVisibility(View.VISIBLE);
                mTvShopDesc.setText(remark+" ");
                mTvAlldealDesc.setText("资金投资冻结 "+ amount1 + "元");
            }else {
                mTvShopDesc.setVisibility(View.GONE);
                mTvAlldealDesc.setText("取现冻结 "+ amount1 + "元");
            }
        }
    }
}
