package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.Advices;
import com.jia16.bean.Recommends;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.HomeHolder;
import com.jia16.pulltorefreshview.holder.RecommendHolder;

import java.util.ArrayList;

/**
 * 推荐详情的listview的数据适配器
 */
public class RecommendAdapter extends BasicAdapter<Recommends> {


    public RecommendAdapter(ArrayList<Recommends> list) {
        super(list);

    }

    @Override
    public BaseHolder<Recommends> getHolder(int position) {
        return new RecommendHolder();
    }


}
