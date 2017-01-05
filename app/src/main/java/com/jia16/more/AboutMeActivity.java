package com.jia16.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.util.DMConstant;
import com.jia16.web.WebActivity;

/**
 * 更多 --关于嘉石榴 界面
 */
public class AboutMeActivity extends BaseActivity {


    private LinearLayout mllAboutMe;//关于我们
    private LinearLayout mllAboutIntroduce;//股东介绍
    private LinearLayout mllAboutTeam;//高管团队
    private LinearLayout mllAboutEvent;//大事记
    private LinearLayout mllAboutMy;//联系我们
    private TextView mTvVersionCode;//显示嘉石榴的版本

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        //初始化布局
        initView();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        ((TextView)findViewById(R.id.title_text)).setText("关于嘉石榴");

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //关于我们
        mllAboutMe = (LinearLayout) findViewById(R.id.ll_about_me);
        mllAboutMe.setOnClickListener(this);

        //股东介绍
        mllAboutIntroduce = (LinearLayout) findViewById(R.id.ll_about_introduce);
        mllAboutIntroduce.setOnClickListener(this);

        //高管团队
        mllAboutTeam = (LinearLayout) findViewById(R.id.ll_about_team);
        mllAboutTeam.setOnClickListener(this);

        //大事记
        mllAboutEvent = (LinearLayout) findViewById(R.id.ll_about_event);
        mllAboutEvent.setOnClickListener(this);

        //联系我们
        mllAboutMy = (LinearLayout) findViewById(R.id.ll_about_my);
        mllAboutMy.setOnClickListener(this);

        //显示嘉石榴的版本
        mTvVersionCode = (TextView) findViewById(R.id.tv_version_code);
        mTvVersionCode.setText("嘉石榴 "+getVersionName());

    }

    @Override
    public void onClick(View view) {
        if(checkClick(view.getId())){
            Intent intent;
            switch (view.getId()){
                case R.id.ll_about_me://关于我们
                    intent=new Intent(AboutMeActivity.this, WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.MORE_ABOUT_ME);
                    intent.putExtra("title","关于我们");
                    startActivity(intent);
                break;

                case R.id.ll_about_introduce://股东介绍
                    intent=new Intent(AboutMeActivity.this, WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.MORE_ABOUT_INTRODUCE);
                    intent.putExtra("title","股东介绍");
                    startActivity(intent);
                    break;

                case R.id.ll_about_team://高管团队
                    intent=new Intent(AboutMeActivity.this, WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.MORE_ABOUT_TEAM);
                    intent.putExtra("title","高管团队");
                    startActivity(intent);
                    break;

                case R.id.ll_about_event://大事记
                    intent=new Intent(AboutMeActivity.this, WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.MORE_ABOUT_EVENT);
                    intent.putExtra("title","大事记");
                    startActivity(intent);
                    break;

                case R.id.ll_about_my://联系我们
                    intent=new Intent(AboutMeActivity.this, WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.MORE_ABOUT_MY);
                    intent.putExtra("title","联系我们");
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }


        super.onClick(view);
    }


}
