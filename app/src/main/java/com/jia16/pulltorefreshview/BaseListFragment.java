package com.jia16.pulltorefreshview;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jia16.R;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;

import java.util.ArrayList;

/**
 * 下拉刷新的fragment的基类
 */
public abstract class BaseListFragment<T> extends BasaFragment implements AdapterView.OnItemClickListener {

    public PullToRefreshListView refreshListView;//下拉刷新的listView(引入的库)

    public ListView listView;  //主界面展示的listview

    public BasicAdapter<T> adapter;    //listview的数据适配器


    public ArrayList<T> list=new ArrayList<T>();
    @Override
    public View getSuccessView() {
        //下拉刷新的listView
        refreshListView= (PullToRefreshListView) View.inflate(getActivity(), R.layout.ptr_refresh,null);
        //设置listview刷新的模式的方法
        setRefreshMode();

        //2.设置刷新的监听器
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            //下拉刷新或上拉加载都会调用此方法
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                //下拉刷新,重新请求服务器，加载第0页的数据
                loadingPager.loadDataAndRefreshPage();
            }
        });

        //PullToRefreshListView其实是一个线性布局，里面包裹了有listview
        //3.获取PullToRefreshListView里面包裹的listView
        listView = refreshListView.getRefreshableView();

        //设置listview的分割线和listview的状态选择器的方法
        setListView();

        //调用添加头布局的方法
        addHeadView();

        adapter=getAdapter();  //需要动态的去获取一个adapter
        listView.setAdapter(adapter);

        //给listview条目设置点击事件
        listView.setOnItemClickListener(this);

        return refreshListView;
    }

    //条目的点击事件,空实现，让子类自己去实现
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //每一个listview都需要数据适配器，子类必须实现
    public abstract BasicAdapter<T> getAdapter();


    //检查如果是下拉刷新，就清空集合
    public void checkPullFromStart() {
        //如果当时的模式是下拉刷新中
        if(refreshListView.getCurrentMode()== PullToRefreshBase.Mode.PULL_FROM_START) {
            //下拉刷新,但是在重新请求服务器之前要清除集合中的数据
            list.clear();
        }
    }

    /**
     * 设置下拉刷新的listview的模式，如果子类的模式不一样，可以重写
     */
    public void setRefreshMode(){
        //1.设置下拉刷新的listview的模式,为即可以上拉，也可以下拉的模式
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);//默认为既可以下拉，也可以下拉
    }
    /**
     * 设置listview的divider和selector
     * 如果不需要取消分割线和listview的状态选择器，子类可以自己实现
     */
    public void setListView(){
        //取消listview的分隔线
        listView.setDividerHeight(0);

        //给listview设置selector（状态选择器）为透明的
        listView.setSelection(android.R.color.transparent);
    }

    //添加一个头布局的方法,子类如果需要头布局，就自己实现
    public void addHeadView() {
    }

    @Override
    public Object requestData() {
        return null;
    }
}
