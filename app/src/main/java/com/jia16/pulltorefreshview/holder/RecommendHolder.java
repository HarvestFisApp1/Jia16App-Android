package com.jia16.pulltorefreshview.holder;

/**
 * 推荐详情的holder
 */

import android.view.View;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.Recommends;


public class RecommendHolder extends BaseHolder<Recommends> {


    private TextView mTvRecommendName;//推荐好友姓名
    private TextView mTvInviteMoney;   //好友投资金额
    private TextView mTvFriendPhone;//好友电话
    private TextView mTvDeduct;     //提成金额
    private TextView mTvAwardState;//现金奖励发放状态
    private TextView mTvAwardMoney;//现金奖励金额

    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_recommend_detail_item,null);
        mTvRecommendName = (TextView) view.findViewById(R.id.tv_recommend_name);
        mTvInviteMoney = (TextView) view.findViewById(R.id.tv_invite_money);
        mTvFriendPhone = (TextView) view.findViewById(R.id.tv_friend_phone);
        mTvDeduct = (TextView) view.findViewById(R.id.tv_deduct);
        mTvAwardState = (TextView) view.findViewById(R.id.tv_award_state);
        mTvAwardMoney = (TextView) view.findViewById(R.id.tv_award_money);
        return view;
    }

    //绑定数据的操作
    public void bindData(Recommends appinfo) {
        //推荐好友姓名
        mTvRecommendName.setText(appinfo.getRecommendedRealName());
        //好友投资金额
        int amount = appinfo.getRecommendedTotalInvest().getAmount();
        if(amount==0){
            //表示奖励还没有下发，那么就隐藏好友投资金额的布局
            mTvInviteMoney.setVisibility(View.INVISIBLE);
        }else if(1000<=amount && amount<=10000){
            mTvInviteMoney.setText("1000元-10000元");
        }else if(10001<=amount && amount<=50000){
            mTvInviteMoney.setText("10001元-50000元");
        }else if(50001<=amount && amount<=100000){
            mTvInviteMoney.setText("50001元-100000元");
        }else if(amount>=100000){
            mTvInviteMoney.setText("100000元以上");
        }
        //好友电话
        mTvFriendPhone.setText(appinfo.getRecommendedPhone());

        //提成金额
        int deductMoney = appinfo.getParentCashCommission().getAmount();
        if(deductMoney==0){
            //表示奖励还没有下发，那么就隐藏提成金额布局
            mTvDeduct.setVisibility(View.INVISIBLE);
        }else {
            mTvDeduct.setText(deductMoney+".00元");
        }


        //现金奖励发放状态
        if(deductMoney>0){//表示奖励已经下发
            //那么就更换边框背景颜色,更改字体颜色
            mTvAwardState.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.main_color));
            //mTvAwardState.setBackground(BaseApplication.getInstance().getDrawable(R.drawable.vertify_code_shap));
            //设置背景颜色
            mTvAwardState.setBackgroundResource(R.drawable.shape_recommend_state_ok);
        }else {
            mTvAwardState.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.recommend_friends));
            mTvAwardState.setBackgroundResource(R.drawable.shape_recommend_state);
        }

        //现金奖励金额
        int amount1 = appinfo.getParentCashReward().getAmount();
        if(amount1==0){
            //表示奖励还没有下发，那么就隐藏现金奖励金额布局
            mTvAwardMoney.setVisibility(View.INVISIBLE);
        }else {
            mTvAwardMoney.setText(amount1+".00元");
        }

    }
}
