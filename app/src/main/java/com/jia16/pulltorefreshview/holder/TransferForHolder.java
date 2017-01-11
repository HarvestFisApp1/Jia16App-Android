package com.jia16.pulltorefreshview.holder;

/**
 * 转让中的holder
 */

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.activity.LoadingActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.TransferFor;
import com.jia16.util.AlertUtil;
import com.jia16.util.AmountUtil;
import com.jia16.util.Lg;
import com.jia16.util.PopupWindowUtils;

public class TransferForHolder extends BaseHolder<TransferFor> {

    private TextView mTvAssetsName;//标的名称
    private TextView mTvTransferMoney;//转让价格
    private TextView mTvTransferDate;//转让期限
    private TextView mTvResidueData;//剩余转让期限
    private TextView mTvCancelTransfer;//撤销转让按钮
    private PopupWindow popupWindow;


    //初始化holderView
    public View initHolderView() {
        View view=View.inflate(BaseApplication.getInstance(), R.layout.item_transfer_for,null);
        mTvAssetsName = (TextView) view.findViewById(R.id.tv_assets_name);
        mTvTransferMoney = (TextView) view.findViewById(R.id.tv_transfer_money);
        mTvTransferDate = (TextView) view.findViewById(R.id.tv_transfer_date);
        mTvResidueData = (TextView) view.findViewById(R.id.tv_residue_data);
        mTvCancelTransfer = (TextView) view.findViewById(R.id.tv_cencel_transfer);
        return view;
    }

    //绑定数据的操作
    public void bindData(TransferFor appinfo) {
        //标的名称
        mTvAssetsName.setText(appinfo.getTitle());

        //转让价格
        double transferAmount = appinfo.getTransferAmount().getAmount();
        mTvTransferMoney.setText(AmountUtil.addComma(AmountUtil.DT.format(transferAmount)));

        //转让期限
        int count = appinfo.getInstalmentPolicy().getInterval().getCount();
        mTvTransferDate.setText(count+"");

        //剩余转让期限
        mTvResidueData.setText(appinfo.getRemainingTransferDays()+"");

        //获取转让中标的id
        final int transferId = appinfo.getId();

        //撤销转让按钮---  /api/users/97071/subjects/12972/pass
        //{"timestamp":1484123557714} 当前时间的毫秒值
        mTvCancelTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //发送广播，到我的资产界面（assetsFragmnet),显示PopupWindow弹窗，提示是否是撤销转让
                Intent intent=new Intent();
                intent.setAction("show_transfer_popupWindow");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("transferId",transferId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApplication.getInstance().sendBroadcast(intent);
            }
        });

    }
}
