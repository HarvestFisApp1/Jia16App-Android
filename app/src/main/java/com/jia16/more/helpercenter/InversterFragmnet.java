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
 *更多 ------帮助中心 的投资指南界面
 */
public class InversterFragmnet extends BaseFragment implements View.OnClickListener{

    private BaseApplication mContext;
    private LayoutInflater inflater;

    private LinearLayout mllHelperOperation;//操作流程
    private LinearLayout mllHelperRegister;//注册认证
    private LinearLayout mllHelperBindingCard; //绑卡认证
    private LinearLayout mllHelperRecharge;//充值取现
    private LinearLayout mllHelperShorcut;//快捷支付
    private LinearLayout mllHelperChangecard; //换卡流程
    private LinearLayout mllHelperProduct;//产品介绍
    private LinearLayout mllhelperInvester;//产品投资
    private LinearLayout mllHelperCost; //费用规则
    private LinearLayout mllHelperBank;//银行充值限额表
    private LinearLayout mllHelperTransfer;//转让指引

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_helper_inverster, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View mPatentView = getView();
        //操作流程
        mllHelperOperation = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_operation);
        mllHelperOperation.setOnClickListener(this);

        //注册认证
        mllHelperRegister = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_register);
        mllHelperRegister.setOnClickListener(this);

        //绑卡认证
        mllHelperBindingCard = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_binding_card);
        mllHelperBindingCard.setOnClickListener(this);

        //充值取现
        mllHelperRecharge = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_recharge);
        mllHelperRecharge.setOnClickListener(this);

        //快捷支付
        mllHelperShorcut = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_shortcut);
        mllHelperShorcut.setOnClickListener(this);

        //换卡流程
        mllHelperChangecard = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_changecard);
        mllHelperChangecard.setOnClickListener(this);

        //产品介绍
        mllHelperProduct = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_product);
        mllHelperProduct.setOnClickListener(this);

        //产品投资
        mllhelperInvester = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_product_invester);
        mllhelperInvester.setOnClickListener(this);

        //费用规则
        mllHelperCost = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_cost);
        mllHelperCost.setOnClickListener(this);

        //银行充值限额表
        mllHelperBank = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_bank);
        mllHelperBank.setOnClickListener(this);

        //转让指引
        mllHelperTransfer = (LinearLayout) mPatentView.findViewById(R.id.ll_helper_transfer);
        mllHelperTransfer.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(checkClick(view.getId())){
            Intent intent;
            switch (view.getId()){
                case R.id.ll_helper_operation://操作流程
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_OPERATION);
                    intent.putExtra("title","操作流程");
                    startActivity(intent);
                break;

                case R.id.ll_helper_register://注册认证
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_REGISTER);
                    intent.putExtra("title","注册认证");
                    startActivity(intent);
                    break;

                case R.id.ll_helper_binding_card://绑卡认证
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_BINDING_CARD);
                    intent.putExtra("title","绑卡认证");
                    startActivity(intent);
                break;

                case R.id.ll_helper_recharge://充值取现
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_RECHARGE);
                    intent.putExtra("title","充值取现");
                    startActivity(intent);
                    break;

                case R.id.ll_helper_shortcut://快捷支付
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_SHORTCUT);
                    intent.putExtra("title","快捷支付");
                    startActivity(intent);
                    break;

                case R.id.ll_helper_changecard://换卡流程
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_CHANGE_CARD);
                    intent.putExtra("title","换卡流程");
                    startActivity(intent);
                    break;


                case R.id.ll_helper_product: //产品介绍
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_PRODUCT);
                    intent.putExtra("title","产品介绍");
                    startActivity(intent);
                    break;


                case R.id.ll_helper_product_invester: //产品投资
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_INVESTER);
                    intent.putExtra("title","产品投资");
                    startActivity(intent);
                    break;

                case R.id.ll_helper_cost: //费用规则
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_COST);
                    intent.putExtra("title","费用规则");
                    startActivity(intent);
                    break;

                case R.id.ll_helper_bank: //银行充值限额表
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_BANK);
                    intent.putExtra("title","银行充值限额表");
                    startActivity(intent);
                    break;

                case R.id.ll_helper_transfer: //转让指引
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.HELPER_TRANSFER);
                    intent.putExtra("title","转让指引");
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    }
}
