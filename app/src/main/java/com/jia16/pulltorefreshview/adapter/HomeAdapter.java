package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.Advices;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.HomeHolder;

import java.util.ArrayList;

/**
 * Home主界面的listview的数据适配器
 */
public class HomeAdapter extends BasicAdapter<Advices> {


    public HomeAdapter(ArrayList<Advices> list) {
        super(list);

    }

    @Override
    public BaseHolder<Advices> getHolder(int position) {
        return new HomeHolder();
    }


}
