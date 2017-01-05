package com.jia16.pulltorefreshview.holder;

/**
 * 优惠券的holder
 */

import android.view.View;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.Ticket;

public class TicketHolder extends BaseHolder<Ticket> {

    private TextView mTvTicketDate;//优惠券的有效期
    private TextView mTvTicketNumber;//优惠券的金额

    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_ticket_item,null);

        mTvTicketDate = (TextView) view.findViewById(R.id.tv_ticket_date);
        mTvTicketNumber = (TextView) view.findViewById(R.id.tv_Ticket_number);
        return view;
    }

    //绑定数据的操作
    public void bindData(Ticket appinfo) {

        mTvTicketDate.setText("有效期至 "+appinfo.getExpireDate());
        mTvTicketNumber.setText(appinfo.getAmount().getAmount()+"");

    }
}
