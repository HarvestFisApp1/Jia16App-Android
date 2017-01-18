package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.Ticket;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.TicketHolder;
import com.jia16.pulltorefreshview.holder.UserTicketHolder;

import java.util.ArrayList;

/**
 * 使用的优惠券的listview的数据适配器
 */
public class UserTicketAdapter extends BasicAdapter<Ticket> {


    public UserTicketAdapter(ArrayList<Ticket> list) {
        super(list);

    }

    @Override
    public BaseHolder<Ticket> getHolder(int position) {
        return new UserTicketHolder();
    }


}
