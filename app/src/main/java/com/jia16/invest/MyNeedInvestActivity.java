package com.jia16.invest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.util.PopupWindowUtils;


/**
 * 我要投资界面，----投资详情
 */
public class MyNeedInvestActivity extends BaseActivity {

    private TextView mTitleText;//投资标的名称
    private ImageView mBtnBack; //返回键按钮
    private TextView mTvTitleRight;//标题栏右边的子标题
    private TextView mTvRepaymentRule;//还款方式

    /**
     * 开启这个activity的界面传递过来的数据
     */
    private String title;       //标的名称
    private String rightTitle;//标的类型
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_need_invest);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        rightTitle = intent.getStringExtra("rightTitle");

        //初始化数据
        initViews();

        //绑定数据
        initDate();

    }

    /**
     * 初始化
     */
    private void initViews() {
        mTitleText = ((TextView) findViewById(R.id.title_text));
        mBtnBack = (ImageView) findViewById(R.id.btn_back);
        mTvTitleRight = (TextView) findViewById(R.id.tv_title_right);
        mTvRepaymentRule = (TextView) findViewById(R.id.tv_repayment_rule);
        mTvRepaymentRule.setOnClickListener(this);
    }

    /**
     * 绑定数据
     */
    private void initDate() {
        mTitleText.setText(title);
        mTvTitleRight.setText(rightTitle);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvTitleRight.setVisibility(View.VISIBLE);
        mTvTitleRight.setTextColor(getResources().getColor(R.color.white));
        mTvTitleRight.setBackgroundColor(Color.TRANSPARENT);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_repayment_rule://还款方式
                //弹出弹框，显示还款方式
                // 一个自定义的布局，作为显示的内容
                View contentView = LayoutInflater.from(MyNeedInvestActivity.this).inflate(
                        R.layout.repayment_rule_popwindow, null);
                //弹出使用规则的弹框
                popupWindow = PopupWindowUtils.showPopupWindow(contentView,38);

                ImageView mIvButton = (ImageView) contentView.findViewById(R.id.iv_button);

                mIvButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            popupWindow =null;
                        }
                    }
                });
                //显示popupWindow弹窗
                popupWindow.showAsDropDown(contentView);
            break;

            default:
                break;
        }
        super.onClick(view);
    }
}
