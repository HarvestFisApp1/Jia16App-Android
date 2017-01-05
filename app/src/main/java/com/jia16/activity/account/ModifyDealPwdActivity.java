package com.jia16.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.UserInfo;

/**
 * 修改交易密码 界面
 */
public class ModifyDealPwdActivity extends BaseActivity {


    private TextView mTvUserName;//用于显示用户的真实的姓名
    private LinearLayout mllRememberDealPwd;//我记得原交易密码线性布局
    private LinearLayout mllForgetDealPwd;//我忘记原交易密码了的线性布局
    private UserInfo userInfo;
    private String username;//用户的真实姓名
    private int userId;     //用户的id

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_deal_pwd);
        initView();
    }


    /**
     * 初始化页面元素
     */
    @Override
    protected void initView() {
        super.initView();
        //获取用户信息对象
        userInfo = BaseApplication.getInstance().getUserInfo();
        if(userInfo!=null){
            //获取用户的真实姓名
            username = userInfo.getCertification().getCertifiedName();
            userId = userInfo.getId();
        }


        ((TextView) findViewById(R.id.title_text)).setText("修改交易密码");

        //用于显示用户的真实的姓名
        mTvUserName = (TextView) findViewById(R.id.tv_username);
        mTvUserName.setText(username);

        //我记得原交易密码线性布局
        mllRememberDealPwd = (LinearLayout) findViewById(R.id.ll_remember_deal_pwd);
        mllRememberDealPwd.setOnClickListener(this);

        //我忘记原交易密码了的线性布局
        mllForgetDealPwd = (LinearLayout) findViewById(R.id.ll_forget_deal_pwd);
        mllForgetDealPwd.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (checkClick(view.getId())) {
            Intent intent;
            switch (view.getId()) {
                case R.id.ll_remember_deal_pwd://我记得原交易密码线性布局
                    intent=new Intent(ModifyDealPwdActivity.this,ModifyRememberPwdActivity.class);
                    intent.putExtra("userid",userId);
                    startActivity(intent);
                    finish();
                break;

                case R.id.ll_forget_deal_pwd://我忘记原交易密码了的线性布局
                    intent=new Intent(ModifyDealPwdActivity.this,ModifyForgetPwdActivity.class);


                    break;

                default:
                    break;
            }
        }
        super.onClick(view);
    }
}
