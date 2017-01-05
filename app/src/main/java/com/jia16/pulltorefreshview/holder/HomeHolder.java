package com.jia16.pulltorefreshview.holder;

/**
 * 主界面的holder
 */

import android.view.View;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.Advices;
import com.jia16.pulltorefreshview.holder.BaseHolder;

public class HomeHolder extends BaseHolder<Advices> {

    private TextView mTvNewsTitle;//公告的标题
    private TextView mTvNewsData;//公告的日期

    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_news_advices_item,null);

        mTvNewsTitle = (TextView) view.findViewById(R.id.tv_news_title);
        mTvNewsData = (TextView) view.findViewById(R.id.tv_news_date);
        return view;
    }

    //绑定数据的操作
    public void bindData(Advices appinfo) {

        mTvNewsTitle.setText(appinfo.getTitle());
        mTvNewsData.setText(appinfo.getDate());

    }
}
