package com.jia16.invest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.util.PopupWindowUtils;


/**
 * 我要投资界面，----投资详情--请选择代金券页面
 */
public class SelectVoucherActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_voucher);

        Intent intent = getIntent();

        //初始化数据
        initViews();

        //绑定数据
        initDate();

    }

    /**
     * 初始化
     */
    private void initViews() {
    }

    /**
     * 绑定数据
     */
    private void initDate() {
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            default:
                break;
        }
        super.onClick(view);
    }
}
