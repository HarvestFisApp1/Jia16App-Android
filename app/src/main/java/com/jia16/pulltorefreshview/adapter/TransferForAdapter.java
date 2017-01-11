package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.TransferFor;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.TransferForHolder;

import java.util.ArrayList;

/**
 * 转让中的界面的listview的数据适配器
 */
public class TransferForAdapter extends BasicAdapter<TransferFor> {


    public TransferForAdapter(ArrayList<TransferFor> list) {
        super(list);

    }

    @Override
    public BaseHolder<TransferFor> getHolder(int position) {
        return new TransferForHolder();
    }


}
