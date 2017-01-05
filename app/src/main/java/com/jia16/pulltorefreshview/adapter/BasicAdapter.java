package com.jia16.pulltorefreshview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.jia16.pulltorefreshview.holder.BaseHolder;

import java.util.ArrayList;

/**
 * ListView的数据适配器的基类
 */
public abstract class BasicAdapter<T> extends BaseAdapter {

    public ArrayList<T> list;

    public BasicAdapter(ArrayList<T> list) {    //通过构造方法传入一个集合
        super();
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder<T> holder=null;
        if(convertView==null){
            holder=getHolder(position);
        }else{
            holder= (BaseHolder) convertView.getTag();
        }
        //绑定数据
        holder.bindData(list.get(position));

        //增加动画

        return holder.getHolderView();
    }

    public abstract BaseHolder<T> getHolder(int position);
}
