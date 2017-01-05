package com.jia16.pulltorefreshview.holder;

/**
 * 已回款的holder
 */

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.Returnmoney;
import com.jia16.util.AmountUtil;
import com.jia16.util.Constants;
import com.jia16.util.Lg;

import java.text.NumberFormat;

public class ReturnMoneyHolder extends BaseHolder<Returnmoney> {

    private TextView mTvAssetsName;//标的名称
    private TextView mTvInvestMoney;//投资金额
    private TextView mTvPrincipalInterest;//年化收益率
    private TextView mTvExpire;//本息收入
    private Button mBtContract;//查看合同按钮

    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_return_money_item,null);
        mTvAssetsName = (TextView) view.findViewById(R.id.tv_assets_name);
        mTvInvestMoney = (TextView) view.findViewById(R.id.tv_invest_money);
        mTvPrincipalInterest = (TextView) view.findViewById(R.id.tv_principal_interest);
        mTvExpire = (TextView) view.findViewById(R.id.tv_expire);
        mBtContract = (Button) view.findViewById(R.id.bt_contract);
        return view;
    }

    //绑定数据的操作
    public void bindData(Returnmoney appinfo) {
        //标的名称
        mTvAssetsName.setText(appinfo.getSubject().getTitle());
        //投资金额
        double amount = (double) appinfo.getAmount().getAmount();
        mTvInvestMoney.setText(AmountUtil.addComma(AmountUtil.DT.format(amount)));
        //年化收益率
        double annualRate = appinfo.getSubject().getInstalmentPolicy().getAnnualRate();
        NumberFormat num = NumberFormat.getPercentInstance();
        String rates = num.format(annualRate);
        Lg.e("年化收益率........",rates);
        mTvPrincipalInterest.setText(rates);

        //本息收入
        double amount1 = appinfo.getAccountReceivable().getAmount();
        mTvExpire.setText(AmountUtil.addComma(AmountUtil.DT.format(amount1)));

        //获取user的id
        final int userid = appinfo.getSubject().getUser().getId();
        //获取合同的id
        final int contractId = appinfo.getContract().getId();
        //获取投资id
        final int investId = appinfo.getId();
        Lg.e("id.......",userid+"...."+contractId+"..."+investId);

        final String p2psessionid = BaseApplication.getInstance().sharedPreferences.getString("p2psessionid", "");
        Lg.e("p2psessionid........",p2psessionid);
        //查看合同
        mBtContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                String url= Constants.HOME_PAGE+"api/users/"+userid+"/contracts/"+contractId+"/additional-contracts/"+investId+"?p2psessionid="+p2psessionid;
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApplication.getInstance().startActivity(intent);
            }
        });
    }
}
