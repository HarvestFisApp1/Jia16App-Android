package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.Ticket;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.TicketHolder;

import java.util.ArrayList;

/**
 * 优惠券的listview的数据适配器
 */
public class TicketAdapter extends BasicAdapter<Ticket> {


    public TicketAdapter(ArrayList<Ticket> list) {
        super(list);

    }

    @Override
    public BaseHolder<Ticket> getHolder(int position) {
        return new TicketHolder();
    }


}
