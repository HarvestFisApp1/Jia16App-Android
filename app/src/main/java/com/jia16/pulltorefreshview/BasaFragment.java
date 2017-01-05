package com.jia16.pulltorefreshview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jia16.R;
import com.jia16.view.LoadingDialog;


/**
 * 7个fragment的基类
 */
public abstract class BasaFragment extends Fragment {

    private LoadingDialog loadingDialog;

    public LoadingPager loadingPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        loadingDialog = new LoadingDialog(getActivity());

        if(loadingPager==null){
            loadingPager=new LoadingPager(getActivity()) {
                @Override
                protected Object loadData() {
                    return requestData();
                }

                @Override
                public View CreateSuccessView() {
                    return getSuccessView();
                }
            };
        }else{
            //需要那loadingview的父view去移除它自己（在studio中也可以不写下面的代码，）
//            CommonUtil.removeSelfFromParent(loadingPager);
//            LogUtil.e(this,"loadingPager已经不为空了:.......");
        }
        return loadingPager;
    }

    //获取每一个子类的successview
    public abstract View getSuccessView();

    //获取每一个子类的数据
    public abstract Object requestData();


    public void showLoadingDialog() {
        loadingDialog.show(getString(R.string.loading));
    }

    public void showLoadingDialog(String content) {
        loadingDialog.show(content);
    }

    public void dimissLoadingDialog() {
        loadingDialog.dismiss();
    }
}
