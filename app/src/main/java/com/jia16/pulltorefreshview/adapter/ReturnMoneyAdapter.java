package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.Returnmoney;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.HomeHolder;
import com.jia16.pulltorefreshview.holder.ReturnMoneyHolder;

import java.util.ArrayList;

/**
 * 已回款的listview的数据适配器
 */
public class ReturnMoneyAdapter extends BasicAdapter<Returnmoney> {


    public ReturnMoneyAdapter(ArrayList<Returnmoney> list) {
        super(list);

    }

    @Override
    public BaseHolder<Returnmoney> getHolder(int position) {
        return new ReturnMoneyHolder();
    }


}
