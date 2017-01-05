package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.ApplyFor;
import com.jia16.pulltorefreshview.holder.ApplyForHolder;
import com.jia16.pulltorefreshview.holder.BaseHolder;

import java.util.ArrayList;

/**
 * 申请中的listview的数据适配器
 */
public class ApplyForAdapter extends BasicAdapter<ApplyFor> {


    public ApplyForAdapter(ArrayList<ApplyFor> list) {
        super(list);

    }

    @Override
    public BaseHolder<ApplyFor> getHolder(int position) {
        return new ApplyForHolder();
    }


}
