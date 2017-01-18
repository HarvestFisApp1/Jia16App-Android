package com.jia16.pulltorefreshview.holder;

/**
 * 使用优惠券的holder
 */

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.Ticket;

public class UserTicketHolder extends BaseHolder<Ticket> {

    private TextView mTvTicketDate;//优惠券的有效期
    private TextView mTvTicketNumber;//优惠券的金额
    private LinearLayout mllImages;

    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_ticket_item_user,null);

        mTvTicketDate = (TextView) view.findViewById(R.id.tv_ticket_date);
        mTvTicketNumber = (TextView) view.findViewById(R.id.tv_Ticket_number);

        mllImages = (LinearLayout) view.findViewById(R.id.ll_images);
        //获取屏幕管理器
        WindowManager wm = (WindowManager) BaseApplication.getInstance()
                .getSystemService(BaseApplication.getInstance().WINDOW_SERVICE);
        //动态获取屏幕的宽度
        int linearlayoutWidth = wm.getDefaultDisplay().getWidth();
        float linearLayoutHeight = (float) (linearlayoutWidth/2.77);
        ViewGroup.LayoutParams layoutParams = mllImages.getLayoutParams();
        layoutParams.height= (int) linearLayoutHeight;
        mllImages.setLayoutParams(layoutParams);
        return view;
    }

    //绑定数据的操作
    public void bindData(Ticket appinfo) {

        mTvTicketDate.setText("有效期至 "+appinfo.getExpireDate());
        mTvTicketNumber.setText(appinfo.getAmount().getAmount()+"");

    }
}
