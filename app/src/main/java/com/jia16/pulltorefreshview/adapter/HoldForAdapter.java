package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.HoldFor;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.HoldForHolder;

import java.util.ArrayList;

/**
 * 持有中的listview的数据适配器
 */
public class HoldForAdapter extends BasicAdapter<HoldFor> {


    public HoldForAdapter(ArrayList<HoldFor> list) {
        super(list);

    }

    @Override
    public BaseHolder<HoldFor> getHolder(int position) {
        return new HoldForHolder();
    }


}
