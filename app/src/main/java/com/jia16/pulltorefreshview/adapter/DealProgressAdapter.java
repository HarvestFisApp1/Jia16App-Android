package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.Advices;
import com.jia16.bean.DealProcess;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.DealProgressHolder;
import com.jia16.pulltorefreshview.holder.HomeHolder;

import java.util.ArrayList;

/**
 * 我的资产--在途交易界面的listview的数据适配器
 */
public class DealProgressAdapter extends BasicAdapter<DealProcess> {


    public DealProgressAdapter(ArrayList<DealProcess> list) {
        super(list);

    }

    @Override
    public BaseHolder<DealProcess> getHolder(int position) {
        return new DealProgressHolder();
    }


}
