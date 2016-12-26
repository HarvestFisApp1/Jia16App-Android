package com.jia16.more.helpercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.base.BaseFragment;
import com.jia16.util.DMConstant;
import com.jia16.web.WebActivity;

/**
 * 更多 ------帮助中心 的安全保障界面
 */
public class MobileFragmnet extends BaseFragment implements View.OnClickListener{

    private BaseApplication mContext;
    private LayoutInflater inflater;
    private LinearLayout mllHelperWebsitProblem;//网站使用问题
    private LinearLayout mllHelperProduct;//产品相关问题

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_helper_mobile, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View mPatentView = getView();
        //网站使用问题
        mllHelperWebsitProblem = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_websit_problem);
        mllHelperWebsitProblem.setOnClickListener(this);

        //产品相关问题
        mllHelperProduct = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_product_problem);
        mllHelperProduct.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(checkClick(view.getId())){
            Intent intent;
           switch (view.getId()){
               case R.id.ll_helper_websit_problem://网站使用问题
                   intent=new Intent(getActivity(), WebActivity.class);
                   intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_WEBSIT);
                   intent.putExtra("title","网站使用问题");
                   startActivity(intent);
               break;

               case R.id.ll_helper_product_problem://产品相关问题
                   intent=new Intent(getActivity(), WebActivity.class);
                   intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_PRODUCT_PROBLEM);
                   intent.putExtra("title","产品相关问题");
                   startActivity(intent);
                   break;

               default:
                   break;
           }
        }
    }
}
