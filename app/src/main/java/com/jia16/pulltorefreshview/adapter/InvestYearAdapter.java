package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.InvestConstant;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.InvestYearHolder;

import java.util.ArrayList;

/**
 * Home主界面的listview的数据适配器
 */
public class InvestYearAdapter extends BasicAdapter<InvestConstant> {


    public InvestYearAdapter(ArrayList<InvestConstant> list) {
        super(list);

    }

    @Override
    public BaseHolder<InvestConstant> getHolder(int position) {
        return new InvestYearHolder();
    }


}
