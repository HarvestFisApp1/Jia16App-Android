package com.jia16.pulltorefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jia16.R;
import com.jia16.base.BaseApplication;

/**
 * 负责管理主界面加载页面数据的逻辑
 */
public abstract class LoadingPager extends FrameLayout {

    public View loadingView;      //加载中的view

    public View errorView;

    public View successView;

    //定义页面加载的3种状态的常量
    enum PageState {
        STATE_LOADING,  //加载中的状态
        STATE_ERROR,    //加载失败的状态
        STATE_SUCCESS   //加载成功的状态
    }

    public PageState mState = PageState.STATE_LOADING;//表示当前界面的状态,默认是加载中的状态

    public LoadingPager(Context context) {
        super(context);
        initLoadingPager();
    }

    public LoadingPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLoadingPager();
    }

    public LoadingPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLoadingPager();
    }

    /**
     * 天然的往loadingPage中添加3个view
     */

    public void initLoadingPager() {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        //依次添加3个view到framelayout
        if (loadingView == null) {
            loadingView = View.inflate(getContext(), R.layout.page_loading, null);
        }
        addView(loadingView, params);//添加布局到framLyout


        if (errorView == null) {
            errorView = View.inflate(getContext(), R.layout.page_error, null);
            //找到error界面中的 加载失败,点击重试 按钮
            Button btn_reload= (Button) errorView.findViewById(R.id.btn_reload);
            //给按钮设置监听事件
            btn_reload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.重新将当前页面的加载状态更改为state_loading
                    mState=PageState.STATE_LOADING;
                    //2.调用showPager();方法来根据当前的加载状态显示对应的页面
                    showPager();
                    //3.调用loadDataAndRefreshPage()方法，重新请求数据,刷新界面
                    loadDataAndRefreshPage();
                }
            });
        }
        addView(errorView, params);

        if (successView == null) {
            successView = CreateSuccessView();//需要添加不固定的view
        }

        if (successView == null) {
            throw new IllegalArgumentException("the method CreateSuccessView() can not return null");
        } else {
            addView(successView, params);
        }

        //显示默认的loadingview
        showPager();

        //请求数据，然后根据返回来的数据来属性page
        loadDataAndRefreshPage();
    }
    //根据当前的mState来显示对应的view

    private void showPager() {
        //1 隐藏三个view布局
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        successView.setVisibility(View.INVISIBLE);
        //2.根据不同的状态，来显示

        switch (mState) {
            case STATE_LOADING:
                loadingView.setVisibility(View.VISIBLE);
                break;

            case STATE_ERROR:
                errorView.setVisibility(View.VISIBLE);
                break;

            case STATE_SUCCESS:
                successView.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 请求数据，然后根据返回来的数据来属性page
     * @return
     */
    public void loadDataAndRefreshPage(){

        //由于请求服务器是一个耗时操作，开启一个子线程
        new Thread(){
            @Override
            public void run() {

                //SystemClock.sleep(100);
                //请求数据
                Object data=loadData();
                //判断data是否为空，如果为空为error，如果不为空为success
                if(data==null){
                    mState=PageState.STATE_ERROR;
                }else{
                    mState=PageState.STATE_SUCCESS;
                }

                //由于刷新界面时需要在主线程中执行的
//                CommonUtil.runOnUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //根据新的mstate，来更新page
//                        showPager();
//                    }
//                });
                BaseApplication.mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showPager();
                    }
                });

            }
        }.start();

    }

    //去服务器请求数据，由于我不关心具体的数据类型，只需要判断数据是否为空
    protected abstract Object loadData();


    //获取successview，由于每一个successView都不一样，所以由每个界面自己去实现
    public abstract View CreateSuccessView();
}





