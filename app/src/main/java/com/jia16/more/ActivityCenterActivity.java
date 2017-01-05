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
import com.jia16.util.ImageLoadOptions;
import com.jia16.web.WebActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 更多 --活动中心 界面
 */
public class ActivityCenterActivity extends BaseActivity {


    private ImageView mIvImage1;//好友推荐的图片
    private ImageView mIvImage2;//转账充值的图片
    private LinearLayout mllDesc1;//好友推荐的线性布局
    private LinearLayout mllDesc2;//转账充值的现象布局


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_center);

        //初始化布局
        initView();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        ((TextView)findViewById(R.id.title_text)).setText("活动中心");

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvImage1 = (ImageView) findViewById(R.id.iv_image1);
        ImageLoader.getInstance().displayImage(DMConstant.H5Url.MORE_ACTIVITY,mIvImage1, ImageLoadOptions.fadeIn_options);

        mIvImage2 = (ImageView) findViewById(R.id.iv_image2);
        ImageLoader.getInstance().displayImage(DMConstant.H5Url.MORE_ACTIVITY_TRANSFER,mIvImage2, ImageLoadOptions.fadeIn_options);

        mllDesc1 = (LinearLayout) findViewById(R.id.ll_desc1);
        mllDesc1.setOnClickListener(this);

        mllDesc2 = (LinearLayout) findViewById(R.id.ll_desc2);
        mllDesc2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(checkClick(view.getId())){
            Intent intent;
            switch (view.getId()){
                case R.id.ll_desc1://好友推荐的线性布局
                    intent=new Intent(ActivityCenterActivity.this, WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.MORE_ACTIVITY_INTRODUCE);
                    intent.putExtra("title","好友推荐");
                    startActivity(intent);
                break;

                case R.id.ll_desc2://转账充值的现象布局
                    intent=new Intent(ActivityCenterActivity.this, WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.MORE_ACTIVITY_TRANSFER_INTRODUCE);
                    intent.putExtra("title","转账充值");
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
        super.onClick(view);
    }



}


