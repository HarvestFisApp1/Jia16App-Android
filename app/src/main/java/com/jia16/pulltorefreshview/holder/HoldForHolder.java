package com.jia16.pulltorefreshview.holder;

/**
 * 持有中的holder
 */

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.assets.ApplyTransferActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.HoldFor;
import com.jia16.util.AmountUtil;
import com.jia16.util.Constants;
import com.jia16.util.Lg;
import com.jia16.util.TimeUtils;

public class HoldForHolder extends BaseHolder<HoldFor> {

    private TextView mTvAssetsName;//标的名称
    private TextView mTvInvestMoney;//投资金额
    private TextView mTvPrincipalInterest;//应收本息
    private TextView mTvDesc1;//应收本息的文字布局
    private TextView mTvExpire;//到期日期
    private TextView mTvDesc2;//到期日期的文字布局
    private Button mBtContract;//查看合同按钮
    private View viewLine;//分隔线
    private Button mBtTransfer;//申请转让的按钮

    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_return_money_item,null);
        mTvAssetsName = (TextView) view.findViewById(R.id.tv_assets_name);
        mTvInvestMoney = (TextView) view.findViewById(R.id.tv_invest_money);
        mTvPrincipalInterest = (TextView) view.findViewById(R.id.tv_principal_interest);
        mTvDesc1 = (TextView) view.findViewById(R.id.tv_desc1);
        mTvExpire = (TextView) view.findViewById(R.id.tv_expire);
        mTvDesc2 = (TextView) view.findViewById(R.id.tv_desc2);
        mBtContract = (Button) view.findViewById(R.id.bt_contract);
        viewLine = view.findViewById(R.id.view_line);
        mBtTransfer = (Button) view.findViewById(R.id.bt_transfer);
        return view;
    }

    //绑定数据的操作
    public void bindData(HoldFor appinfo) {
        //设置text文本
        mTvDesc1.setText("应收本息(元)");
        mTvDesc2.setText("到期日");
        //显示分隔线
        viewLine.setVisibility(View.VISIBLE);
        //显示申请转让按钮
        mBtTransfer.setVisibility(View.VISIBLE);

        //设置标的名称
        String title = appinfo.getSubject().getTitle();
        mTvAssetsName.setText(title);

        //设置投资金额
        double investAmount = appinfo.getAmount().getAmount();
        mTvInvestMoney.setText(AmountUtil.addComma(AmountUtil.DT.format(investAmount)));

        //应收本息
        double receivableAmount = appinfo.getAccountReceivable().getAmount();
        mTvPrincipalInterest.setText(AmountUtil.addComma(AmountUtil.DT.format(receivableAmount)));

        //设置到期日期
        long effectDate = appinfo.getNextSettlementDate();
        String time = TimeUtils.getTime(effectDate, TimeUtils.DATE_FORMAT_DATE);
        mTvExpire.setText(time);

        //获取user的id
        final int usersID = appinfo.getSubject().getUser().getId();
        //获取合同的id
        final int centractId = appinfo.getContract().getId();
        //获取投资id
        final int investsId = appinfo.getId();

        //获取subject，申请转让的id
        final int subjectId = appinfo.getSubject().getId();

        //获取用户的p2psessionid
        final String p2psessionid = BaseApplication.getInstance().sharedPreferences.getString("p2psessionid", "");
        //查看合同按钮
        mBtContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction("android.intent.action.VIEW");
                String url= Constants.HOME_PAGE+"api/users/"+usersID+"/contracts/"+centractId+"/additional-contracts/"+investsId+"?p2psessionid="+p2psessionid;
                Uri content_url=Uri.parse(url);
                intent.setData(content_url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApplication.getInstance().startActivity(intent);
            }
        });


        //申请转让按钮
        mBtTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BaseApplication.getInstance(), ApplyTransferActivity.class);
                intent.putExtra("usersID",usersID); //传递用户id
                intent.putExtra("subjectId",subjectId);//传递subject，申请转让的id
                intent.putExtra("investsId",investsId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApplication.getInstance().startActivity(intent);
            }
        });
    }
}
