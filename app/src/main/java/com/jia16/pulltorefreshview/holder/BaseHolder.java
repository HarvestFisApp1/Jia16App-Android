package com.jia16.pulltorefreshview.holder;

import android.view.View;

/**
 * holder的基类
 */
public abstract class BaseHolder<T> {
    public View holderView;// 注意一开始就将convertView转移到holder当中,使用变量去表示

    public BaseHolder() {
        holderView=initHolderView();

        holderView.setTag(this);

    }

    //初始化holderView
    public abstract View initHolderView();

    //绑定数据的操作
    public abstract void bindData(T data);

    //获取holderView
    public View getHolderView(){
        return holderView;
    }
}
