package com.jia16.invest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;


/**
 * 我要投资界面，----投资详情
 */
public class MyNeedInvestActivity extends BaseActivity {

    private TextView mTitleText;//投资标的名称
    private ImageView mBtnBack; //返回键按钮
    private TextView mTvTitleRight;//标题栏右边的子标题

    /**
     * 开启这个activity的界面传递过来的数据
     */
    private String title;       //标的名称
    private String rightTitle;//标的类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_need_invest);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        rightTitle = intent.getStringExtra("rightTitle");

        initViews();
        initDate();

    }

    /**
     * 初始化
     */
    private void initViews() {
        mTitleText = ((TextView) findViewById(R.id.title_text));
        mBtnBack = (ImageView) findViewById(R.id.btn_back);
        mTvTitleRight = (TextView) findViewById(R.id.tv_title_right);
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
}
