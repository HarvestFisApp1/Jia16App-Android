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
 * 更多 ------帮助中心 的常见问题界面
 */
public class CommonFragmnet extends BaseFragment implements View.OnClickListener{

    private BaseApplication mContext;
    private LayoutInflater inflater;
    private LinearLayout mllHelperMobile;//投资安全性
    private LinearLayout mllHelperCapitalMobile;//资金安全性
    private LinearLayout mllHelperRisk;//风险控制
    private LinearLayout mllHelperInformation;//信息安全性
    private LinearLayout mllHelperPrivacy;//隐私保护
    private LinearLayout mllHelperStatement;//免责声明

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_helper_common, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View mPatentView = getView();
        //投资安全性
        mllHelperMobile = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_invester_mobile);
        mllHelperMobile.setOnClickListener(this);

        //资金安全性
        mllHelperCapitalMobile = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_capital_mobile);
        mllHelperCapitalMobile.setOnClickListener(this);

        //风险控制
        mllHelperRisk = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_risk);
        mllHelperRisk.setOnClickListener(this);

        //信息安全性
        mllHelperInformation = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_information);
        mllHelperInformation.setOnClickListener(this);

        //隐私保护
        mllHelperPrivacy = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_privacy);
        mllHelperPrivacy.setOnClickListener(this);

        //免责声明
        mllHelperStatement = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_statement);
        mllHelperStatement.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(checkClick(view.getId())){
            Intent intent;
            switch (view.getId()){
                case R.id.ll_helper_invester_mobile://投资安全性
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_INVESTER_MOBILE);
                    intent.putExtra("title","投资安全性");
                    startActivity(intent);
                break;

                case R.id.ll_helper_capital_mobile://资金安全性
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_CAPITAL_MOBILE);
                    intent.putExtra("title","资金安全性");
                    startActivity(intent);
                    break;

                case R.id.ll_helper_risk://风险控制
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_RISK);
                    intent.putExtra("title","风险控制");
                    startActivity(intent);
                    break;

                case R.id.ll_helper_information://信息安全性
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_INFORMATION);
                    intent.putExtra("title","信息安全性");
                    startActivity(intent);
                    break;

                case R.id.ll_helper_privacy://隐私保护
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_PRIVACY);
                    intent.putExtra("title","隐私保护");
                    startActivity(intent);
                    break;


                case R.id.ll_helper_statement://免责声明
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_STATEMENT);
                    intent.putExtra("title","免责声明");
                    startActivity(intent);
                    break;

                default:
                    break;
            }


        }
    }
}
