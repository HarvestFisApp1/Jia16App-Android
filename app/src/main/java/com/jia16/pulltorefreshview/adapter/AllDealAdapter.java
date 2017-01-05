package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.AllDeal;
import com.jia16.bean.Ticket;
import com.jia16.pulltorefreshview.holder.AllDealHolder;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.TicketHolder;

import java.util.ArrayList;

/**
 * 资金流水的listview的数据适配器
 */
public class AllDealAdapter extends BasicAdapter<AllDeal> {


    public AllDealAdapter(ArrayList<AllDeal> list) {
        super(list);

    }

    @Override
    public BaseHolder<AllDeal> getHolder(int position) {
        return new AllDealHolder();
    }


}
