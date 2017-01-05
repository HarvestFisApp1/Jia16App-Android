package com.jia16.pulltorefreshview.adapter;

import com.jia16.bean.TransferMoney;
import com.jia16.pulltorefreshview.holder.BaseHolder;
import com.jia16.pulltorefreshview.holder.TransferMoneyHolder;

import java.util.ArrayList;

/**
 * 我要投资--转让变现界面的listview的数据适配器
 */
public class TransferMoneyAdapter extends BasicAdapter<TransferMoney> {


    public TransferMoneyAdapter(ArrayList<TransferMoney> list) {
        super(list);

    }

    @Override
    public BaseHolder<TransferMoney> getHolder(int position) {
        return new TransferMoneyHolder();
    }


}
