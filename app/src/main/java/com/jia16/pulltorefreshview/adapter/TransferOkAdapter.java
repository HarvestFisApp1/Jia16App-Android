package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.Advices;
import com.jia16.bean.TransferOk;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.HomeHolder;
import com.jia16.pulltorefreshview.holder.TransferOkHolder;

import java.util.ArrayList;

/**
 * Home主界面的listview的数据适配器
 */
public class TransferOkAdapter extends BasicAdapter<TransferOk> {


    public TransferOkAdapter(ArrayList<TransferOk> list) {
        super(list);

    }

    @Override
    public BaseHolder<TransferOk> getHolder(int position) {
        return new TransferOkHolder();
    }


}
