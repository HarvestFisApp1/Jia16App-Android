package com.jia16.activity.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.Risk;
import com.jia16.bean.UserInfo;
import com.jia16.util.AlertUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 风险评测界面
 */
public class RiskManagerActivity extends BaseActivity {
    //map集合用来存储选择的条目的bianma和值
    HashMap<Integer,Integer> map=new HashMap<>();


    /**
     * 界面1的控件
     */
    private TextView mTvRiskTitle1;
    private TextView mtvRiskDesc1;
    private TextView mtvRiskDesc2;
    private TextView mtvRiskDesc3;
    private TextView mTvNumber1;
    private LinearLayout mllRiskDesc1;
    private LinearLayout mllRiskDesc2;
    private LinearLayout mllRiskDesc3;
    private TextView mtvCheck1;
    private TextView mtvCheck2;
    private TextView mtvCheck3;
    private TextView mNext1;
    private LinearLayout mllVisible1;


    /**
     *界面2的控件
     */
    private TextView mtvRiskTitle2;
    private TextView mtvNumber2;
    private TextView mtvRiskDesc4;
    private TextView mtvRiskDesc5;
    private TextView mtvRiskDesc6;
    private LinearLayout mllRiskDesc4;
    private LinearLayout mllRiskDesc5;
    private LinearLayout mllRiskDesc6;
    private TextView mTvCheck4;
    private TextView mTvCheck5;
    private TextView mTvCheck6;
    private TextView mPre2;
    private TextView mNext2;
    private LinearLayout mllVisible2;

    /**
     *界面3的控件
     */
    private TextView mtvRiskTitle3;
    private TextView mtvNumber3;
    private TextView mtvRiskDesc7;
    private TextView mtvRiskDesc8;
    private TextView mtvRiskDesc9;
    private LinearLayout mllRiskDesc7;
    private LinearLayout mllRiskDesc8;
    private LinearLayout mllRiskDesc9;
    private TextView mTvCheck7;
    private TextView mTvCheck8;
    private TextView mTvCheck9;
    private TextView mPre3;
    private TextView mNext3;
    private LinearLayout mllVisible3;

    /**
     *界面4的控件
     */
    private TextView mtvRiskTitle4;
    private TextView mtvNumber4;
    private TextView mtvRiskDesc10;
    private TextView mtvRiskDesc11;
    private TextView mtvRiskDesc12;
    private LinearLayout mllRiskDesc10;
    private LinearLayout mllRiskDesc11;
    private LinearLayout mllRiskDesc12;
    private TextView mTvCheck10;
    private TextView mTvCheck11;
    private TextView mTvCheck12;
    private TextView mPre4;
    private TextView mNext4;
    private LinearLayout mllVisible4;

    /**
     *界面5的控件
     */
    private TextView mtvRiskTitle5;
    private TextView mtvNumber5;
    private TextView mtvRiskDesc13;
    private TextView mtvRiskDesc14;
    private TextView mtvRiskDesc15;
    private LinearLayout mllRiskDesc13;
    private LinearLayout mllRiskDesc14;
    private LinearLayout mllRiskDesc15;
    private TextView mTvCheck13;
    private TextView mTvCheck14;
    private TextView mTvCheck15;
    private TextView mPre5;
    private LinearLayout mllVisible5;
    private Button mBtRiskSubject;


    private UserInfo userInfo;

    /**
     *风险评测完成页面
     */
    private LinearLayout mtvRiskTitle6;
    private TextView mTvRiskFinish;
    private TextView mTvFinish;
    private String desc1="CONSERVATISM";
    private String desc2="MODERATE";
    private String desc3="RADICALIZATION";
    private Button mBtRiskFinish;
    private String riskCapacityLevelText;
    private Button mBtnRiskRestart;
    private ImageView mIvImageFinish;


    /**
     * 如果用户已经风险评测过，那么当用户再次点击风险评测时显示的界面
     */
    private LinearLayout mllVisible0;
    private TextView mTvRiskRestarted;
    private TextView mTvRestarted;
    private Button mBtRiskRestarted;
    private ImageView mIvImageed;
    private String risktext;
    private String riskcode;
    private String riskCapacityLevelCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_manager);

        userInfo = BaseApplication.getInstance().getUserInfo();

        //初始化布局
        initView();

    }



    /**
     * 初始化布局
     */
    public void initView() {


        Intent intent = getIntent();
        risktext = intent.getStringExtra("risktext");
        riskcode = intent.getStringExtra("riskcode");

        ((TextView)findViewById(R.id.title_tv)).setText("风险偏好");

        // 设置返回按钮事件
        ImageView mBtnBack = (ImageView) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mtvRiskTitle6.getVisibility()==View.VISIBLE){
                    //如果当前显示的是评测完成界面，那么点击返回键就要携带返回值
                    Intent intent = getIntent();
                    intent.putExtra("newRisk",riskCapacityLevelText);
                    intent.putExtra("newCode",riskCapacityLevelCode);
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    finish();
                }
            }
        });

        //请求服务器，获取评测问题
        getRiskProblem();

        //界面1的控件
        mTvRiskTitle1 = (TextView) findViewById(R.id.tv_risk_title1);
        mTvNumber1 = (TextView) findViewById(R.id.tv_number1);
        mtvRiskDesc1 = (TextView) findViewById(R.id.tv_risk_desc1);
        mtvRiskDesc2 = (TextView) findViewById(R.id.tv_risk_desc2);
        mtvRiskDesc3 = (TextView) findViewById(R.id.tv_risk_desc3);
        mllRiskDesc1 = (LinearLayout) findViewById(R.id.ll_risk_desc1);
        mllRiskDesc1.setOnClickListener(this);
        mllRiskDesc2 = (LinearLayout) findViewById(R.id.ll_risk_desc2);
        mllRiskDesc2.setOnClickListener(this);
        mllRiskDesc3 = (LinearLayout) findViewById(R.id.ll_risk_desc3);
        mllRiskDesc3.setOnClickListener(this);
        mtvCheck1 = (TextView) findViewById(R.id.tv_check1);
        mtvCheck2 = (TextView) findViewById(R.id.tv_check2);
        mtvCheck3 = (TextView) findViewById(R.id.tv_check3);
        mNext1 = (TextView) findViewById(R.id.next1);
        mNext1.setOnClickListener(this);
        mllVisible1 = (LinearLayout) findViewById(R.id.ll_visible1);


        //界面2的控件
        mtvRiskTitle2 = (TextView) findViewById(R.id.tv_risk_title2);
        mtvNumber2 = (TextView) findViewById(R.id.tv_number2);
        mtvRiskDesc4 = (TextView) findViewById(R.id.tv_risk_desc4);
        mtvRiskDesc5 = (TextView) findViewById(R.id.tv_risk_desc5);
        mtvRiskDesc6 = (TextView) findViewById(R.id.tv_risk_desc6);

        mllRiskDesc4 = (LinearLayout) findViewById(R.id.ll_risk_desc4);
        mllRiskDesc4.setOnClickListener(this);
        mllRiskDesc5 = (LinearLayout) findViewById(R.id.ll_risk_desc5);
        mllRiskDesc5.setOnClickListener(this);
        mllRiskDesc6 = (LinearLayout) findViewById(R.id.ll_risk_desc6);
        mllRiskDesc6.setOnClickListener(this);
        mTvCheck4 = (TextView) findViewById(R.id.tv_check4);
        mTvCheck5 = (TextView) findViewById(R.id.tv_check5);
        mTvCheck6 = (TextView) findViewById(R.id.tv_check6);
        mPre2 = (TextView) findViewById(R.id.pre2);
        mPre2.setOnClickListener(this);
        mNext2 = (TextView) findViewById(R.id.next2);
        mNext2.setOnClickListener(this);
        mllVisible2 = (LinearLayout) findViewById(R.id.ll_visible2);


        //界面3的控件
        mtvRiskTitle3 = (TextView) findViewById(R.id.tv_risk_title3);
        mtvNumber3 = (TextView) findViewById(R.id.tv_number3);
        mtvRiskDesc7 = (TextView) findViewById(R.id.tv_risk_desc7);
        mtvRiskDesc8 = (TextView) findViewById(R.id.tv_risk_desc8);
        mtvRiskDesc9 = (TextView) findViewById(R.id.tv_risk_desc9);
        mllRiskDesc7 = (LinearLayout) findViewById(R.id.ll_risk_desc7);
        mllRiskDesc7.setOnClickListener(this);
        mllRiskDesc8 = (LinearLayout) findViewById(R.id.ll_risk_desc8);
        mllRiskDesc8.setOnClickListener(this);
        mllRiskDesc9 = (LinearLayout) findViewById(R.id.ll_risk_desc9);
        mllRiskDesc9.setOnClickListener(this);
        mTvCheck7 = (TextView) findViewById(R.id.tv_check7);
        mTvCheck8 = (TextView) findViewById(R.id.tv_check8);
        mTvCheck9 = (TextView) findViewById(R.id.tv_check9);
        mPre3 = (TextView) findViewById(R.id.pre3);
        mPre3.setOnClickListener(this);
        mNext3 = (TextView) findViewById(R.id.next3);
        mNext3.setOnClickListener(this);
        mllVisible3 = (LinearLayout) findViewById(R.id.ll_visible3);


        //界面4的控件
        mtvRiskTitle4 = (TextView) findViewById(R.id.tv_risk_title4);
        mtvNumber4 = (TextView) findViewById(R.id.tv_number4);
        mtvRiskDesc10 = (TextView) findViewById(R.id.tv_risk_desc10);
        mtvRiskDesc11 = (TextView) findViewById(R.id.tv_risk_desc11);
        mtvRiskDesc12 = (TextView) findViewById(R.id.tv_risk_desc12);
        mllRiskDesc10 = (LinearLayout) findViewById(R.id.ll_risk_desc10);
        mllRiskDesc10.setOnClickListener(this);
        mllRiskDesc11 = (LinearLayout) findViewById(R.id.ll_risk_desc11);
        mllRiskDesc11.setOnClickListener(this);
        mllRiskDesc12 = (LinearLayout) findViewById(R.id.ll_risk_desc12);
        mllRiskDesc12.setOnClickListener(this);
        mTvCheck10 = (TextView) findViewById(R.id.tv_check10);
        mTvCheck11 = (TextView) findViewById(R.id.tv_check11);
        mTvCheck12 = (TextView) findViewById(R.id.tv_check12);
        mPre4 = (TextView) findViewById(R.id.pre4);
        mPre4.setOnClickListener(this);
        mNext4 = (TextView) findViewById(R.id.next4);
        mNext4.setOnClickListener(this);
        mllVisible4 = (LinearLayout) findViewById(R.id.ll_visible4);


        //界面5的控件
        mtvRiskTitle5 = (TextView) findViewById(R.id.tv_risk_title5);
        mtvNumber5 = (TextView) findViewById(R.id.tv_number5);
        mtvRiskDesc13 = (TextView) findViewById(R.id.tv_risk_desc13);
        mtvRiskDesc14 = (TextView) findViewById(R.id.tv_risk_desc14);
        mtvRiskDesc15 = (TextView) findViewById(R.id.tv_risk_desc15);
        mllRiskDesc13 = (LinearLayout) findViewById(R.id.ll_risk_desc13);
        mllRiskDesc13.setOnClickListener(this);
        mllRiskDesc14 = (LinearLayout) findViewById(R.id.ll_risk_desc14);
        mllRiskDesc14.setOnClickListener(this);
        mllRiskDesc15 = (LinearLayout) findViewById(R.id.ll_risk_desc15);
        mllRiskDesc15.setOnClickListener(this);
        mTvCheck13 = (TextView) findViewById(R.id.tv_check13);
        mTvCheck14 = (TextView) findViewById(R.id.tv_check14);
        mTvCheck15 = (TextView) findViewById(R.id.tv_check15);
        mPre5 = (TextView) findViewById(R.id.pre5);
        mPre5.setOnClickListener(this);
        mllVisible5 = (LinearLayout) findViewById(R.id.ll_visible5);
        mBtRiskSubject = (Button) findViewById(R.id.bt_risk_subject);
        mBtRiskSubject.setOnClickListener(this);


        //风险评测完成页面
        mtvRiskTitle6 = (LinearLayout) findViewById(R.id.ll_visible6);
        mTvRiskFinish = (TextView) findViewById(R.id.tv_risk_finish);
        mTvFinish = (TextView) findViewById(R.id.tv_finish);
        mBtRiskFinish = (Button) findViewById(R.id.bt_risk_finish);
        mBtRiskFinish.setOnClickListener(this);
        //重新评测的按钮，当评测完成后就显示，否则就隐藏
        mBtnRiskRestart = (Button) findViewById(R.id.btn_risk_restart);
        mBtnRiskRestart.setOnClickListener(this);
        mIvImageFinish = (ImageView) findViewById(R.id.iv_image_finish);


        //如果用户已经风险评测过，那么当用户再次点击风险评测时显示的界面
        mllVisible0 = (LinearLayout) findViewById(R.id.ll_visible0);
        mTvRiskRestarted = (TextView) findViewById(R.id.tv_risk_restarted);
        mTvRestarted = (TextView) findViewById(R.id.tv_restarted);
        mBtRiskRestarted = (Button) findViewById(R.id.bt_risk_restarted);
        mBtRiskRestarted.setOnClickListener(this);
        mIvImageed = (ImageView) findViewById(R.id.iv_image);


        UserInfo.RiskCapacityBean riskCapacity = userInfo.getRiskCapacity();
        if (riskCapacity != null) {
            //String riskCapacityLevelText = riskCapacity.getRiskCapacityLevelText();
            //表示用户已经进行风险评测，那么就隐藏界面1，显示界面0
            mllVisible0.setVisibility(View.VISIBLE);
            mllVisible1.setVisibility(View.INVISIBLE);
            mTvRiskRestarted.setText("您的投资风格为:"+risktext);

            if(riskcode.equals(desc1)){
                //保守型
                mTvRestarted.setText("较为适合与投资低风险理财产品");
                mIvImageed.setImageResource(R.drawable.conservative);
            }else if(riskcode.equals(desc2)){
                //稳健型
                mTvRestarted.setText("较为适合与投资中等风险理财产品");
                mIvImageed.setImageResource(R.drawable.steadiness);
            }else if(riskcode.equals(desc3)){
                //激进型
                mTvRestarted.setText("较为适合与投资高风险理财产品");
                mIvImageed.setImageResource(R.drawable.radical);
            }

        }else {
            //表示用户还没有进行风险评测，那么就隐藏界面0，显示界面1
            mllVisible0.setVisibility(View.INVISIBLE);
            mllVisible1.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        if(checkClick(view.getId())){
            switch (view.getId()){
                /**
                 * 帧布局第一界面
                 */
                case R.id.ll_risk_desc1://界面1的第1个线性布局
                    mtvCheck1.setVisibility(View.VISIBLE);
                    mtvCheck2.setVisibility(View.INVISIBLE);
                    mtvCheck3.setVisibility(View.INVISIBLE);
                    textviewIsCheck1();
                    break;

                case R.id.ll_risk_desc2://界面1的第2个线性布局
                    mtvCheck1.setVisibility(View.INVISIBLE);
                    mtvCheck2.setVisibility(View.VISIBLE);
                    mtvCheck3.setVisibility(View.INVISIBLE);
                    textviewIsCheck1();
                    break;

                case R.id.ll_risk_desc3://界面1的第3个线性布局
                    mtvCheck1.setVisibility(View.INVISIBLE);
                    mtvCheck2.setVisibility(View.INVISIBLE);
                    mtvCheck3.setVisibility(View.VISIBLE);
                    textviewIsCheck1();
                    break;

                case R.id.next1://界面1的下一步按钮
                    if(mtvCheck1.getVisibility()==View.VISIBLE){
                        map.put(1,1);
                    }else if(mtvCheck2.getVisibility()==View.VISIBLE){
                        map.put(1,2);
                    }else if(mtvCheck3.getVisibility()==View.VISIBLE){
                        map.put(1,3);
                    }
                    mllVisible1.setVisibility(View.INVISIBLE);
                    mllVisible2.setVisibility(View.VISIBLE);

                break;


                /**
                 * 帧布局第二界面
                 */
                case R.id.ll_risk_desc4://界面2的第1个线性布局
                    mTvCheck4.setVisibility(View.VISIBLE);
                    mTvCheck5.setVisibility(View.INVISIBLE);
                    mTvCheck6.setVisibility(View.INVISIBLE);
                    textviewIsCheck2();
                    break;

                case R.id.ll_risk_desc5://界面2的第2个线性布局
                    mTvCheck4.setVisibility(View.INVISIBLE);
                    mTvCheck5.setVisibility(View.VISIBLE);
                    mTvCheck6.setVisibility(View.INVISIBLE);
                    textviewIsCheck2();
                    break;

                case R.id.ll_risk_desc6://界面2的第3个线性布局
                    mTvCheck4.setVisibility(View.INVISIBLE);
                    mTvCheck5.setVisibility(View.INVISIBLE);
                    mTvCheck6.setVisibility(View.VISIBLE);
                    textviewIsCheck2();
                    break;

                case R.id.next2://界面2的下一步按钮
                    if(mTvCheck4.getVisibility()==View.VISIBLE){
                        map.put(2,4);
                    }else if(mTvCheck5.getVisibility()==View.VISIBLE){
                        map.put(2,5);
                    }else if(mTvCheck6.getVisibility()==View.VISIBLE){
                        map.put(2,6);
                    }
                    mllVisible2.setVisibility(View.INVISIBLE);
                    mllVisible3.setVisibility(View.VISIBLE);
                    break;

                case R.id.pre2://界面2的上一步按钮
                    mllVisible1.setVisibility(View.VISIBLE);
                    mllVisible2.setVisibility(View.INVISIBLE);
//                    for (Integer in : map.keySet()) {
//                         //map.keySet()返回的是所有key的值
//                        int str = map.get(1);//得到每个key多对用value的值
//                    }
                    break;



                /**
                 * 帧布局第三界面
                 */
                case R.id.ll_risk_desc7://界面3的第1个线性布局
                    mTvCheck7.setVisibility(View.VISIBLE);
                    mTvCheck8.setVisibility(View.INVISIBLE);
                    mTvCheck9.setVisibility(View.INVISIBLE);
                    textviewIsCheck3();
                    break;

                case R.id.ll_risk_desc8://界面3的第2个线性布局
                    mTvCheck7.setVisibility(View.INVISIBLE);
                    mTvCheck8.setVisibility(View.VISIBLE);
                    mTvCheck9.setVisibility(View.INVISIBLE);
                    textviewIsCheck3();
                    break;

                case R.id.ll_risk_desc9://界面3的第3个线性布局
                    mTvCheck7.setVisibility(View.INVISIBLE);
                    mTvCheck8.setVisibility(View.INVISIBLE);
                    mTvCheck9.setVisibility(View.VISIBLE);
                    textviewIsCheck3();
                    break;

                case R.id.next3://界面3的下一步按钮
                    if(mTvCheck7.getVisibility()==View.VISIBLE){
                        map.put(3,7);
                    }else if(mTvCheck8.getVisibility()==View.VISIBLE){
                        map.put(3,8);
                    }else if(mTvCheck9.getVisibility()==View.VISIBLE){
                        map.put(3,9);
                    }
                    mllVisible3.setVisibility(View.INVISIBLE);
                   mllVisible4.setVisibility(View.VISIBLE);
                    break;

                case R.id.pre3://界面3的上一步按钮
                    mllVisible2.setVisibility(View.VISIBLE);
                    mllVisible3.setVisibility(View.INVISIBLE);
//                    for (Integer in : map.keySet()) {
//                         //map.keySet()返回的是所有key的值
//                        int str = map.get(1);//得到每个key多对用value的值
//                    }
                    break;



                /**
                 * 帧布局第四界面
                 */
                case R.id.ll_risk_desc10://界面4的第1个线性布局
                    mTvCheck10.setVisibility(View.VISIBLE);
                    mTvCheck11.setVisibility(View.INVISIBLE);
                    mTvCheck12.setVisibility(View.INVISIBLE);
                    textviewIsCheck4();
                    break;

                case R.id.ll_risk_desc11://界面4的第2个线性布局
                    mTvCheck10.setVisibility(View.INVISIBLE);
                    mTvCheck11.setVisibility(View.VISIBLE);
                    mTvCheck12.setVisibility(View.INVISIBLE);
                    textviewIsCheck4();
                    break;

                case R.id.ll_risk_desc12://界面4的第3个线性布局
                    mTvCheck10.setVisibility(View.INVISIBLE);
                    mTvCheck11.setVisibility(View.INVISIBLE);
                    mTvCheck12.setVisibility(View.VISIBLE);
                    textviewIsCheck4();
                    break;

                case R.id.next4://界面4的下一步按钮
                    if(mTvCheck10.getVisibility()==View.VISIBLE){
                        map.put(4,10);
                    }else if(mTvCheck11.getVisibility()==View.VISIBLE){
                        map.put(4,11);
                    }else if(mTvCheck12.getVisibility()==View.VISIBLE){
                        map.put(4,12);
                    }
                    mllVisible4.setVisibility(View.INVISIBLE);
                    mllVisible5.setVisibility(View.VISIBLE);
                    break;

                case R.id.pre4://界面4的上一步按钮
                    mllVisible3.setVisibility(View.VISIBLE);
                    mllVisible4.setVisibility(View.INVISIBLE);
//                    for (Integer in : map.keySet()) {
//                         //map.keySet()返回的是所有key的值
//                        int str = map.get(1);//得到每个key多对用value的值
//                    }
                    break;



                /**
                 * 帧布局第五界面
                 */
                case R.id.ll_risk_desc13://界面5的第1个线性布局
                    mTvCheck13.setVisibility(View.VISIBLE);
                    mTvCheck14.setVisibility(View.INVISIBLE);
                    mTvCheck15.setVisibility(View.INVISIBLE);
                    textviewIsCheck5();
                    break;

                case R.id.ll_risk_desc14://界面5的第2个线性布局
                    mTvCheck13.setVisibility(View.INVISIBLE);
                    mTvCheck14.setVisibility(View.VISIBLE);
                    mTvCheck15.setVisibility(View.INVISIBLE);
                    textviewIsCheck5();
                    break;

                case R.id.ll_risk_desc15://界面5的第3个线性布局
                    mTvCheck13.setVisibility(View.INVISIBLE);
                    mTvCheck14.setVisibility(View.INVISIBLE);
                    mTvCheck15.setVisibility(View.VISIBLE);
                    textviewIsCheck5();
                    break;

                case R.id.bt_risk_subject://界面5的提交按钮
                    if(mTvCheck13.getVisibility()==View.VISIBLE){
                        map.put(5,13);
                    }else if(mTvCheck14.getVisibility()==View.VISIBLE){
                        map.put(5,14);
                    }else if(mTvCheck15.getVisibility()==View.VISIBLE){
                        map.put(5,15);
                    }
                    mllVisible5.setVisibility(View.INVISIBLE);

                    //请求网络，提交数据,获取用户的风险类型
                    riskSubject(map);
                    break;

                case R.id.pre5://界面5的上一步按钮
                    mllVisible4.setVisibility(View.VISIBLE);
                    mllVisible5.setVisibility(View.INVISIBLE);
//                    for (Integer in : map.keySet()) {
//                         //map.keySet()返回的是所有key的值
//                        int str = map.get(1);//得到每个key多对用value的值
//                    }
                    break;

                case R.id.bt_risk_finish://完成评测按钮
                    Intent intent = getIntent();
                    intent.putExtra("newRisk",riskCapacityLevelText);
                    intent.putExtra("newCode",riskCapacityLevelCode);
                    setResult(RESULT_OK,intent);
                    finish();
                break;


                case R.id.btn_risk_restart://重新评测按钮
                    //重新评测，那么就清除map集合，
                    map.clear();
                    Lg.e("map_size",map.size());
                    //重新评测,清除线性布局的选中状态
                    mtvCheck1.setVisibility(View.INVISIBLE);
                    mtvCheck2.setVisibility(View.INVISIBLE);
                    mtvCheck3.setVisibility(View.INVISIBLE);
                    mTvCheck4.setVisibility(View.INVISIBLE);
                    mTvCheck5.setVisibility(View.INVISIBLE);
                    mTvCheck6.setVisibility(View.INVISIBLE);
                    mTvCheck7.setVisibility(View.INVISIBLE);
                    mTvCheck8.setVisibility(View.INVISIBLE);
                    mTvCheck9.setVisibility(View.INVISIBLE);
                    mTvCheck10.setVisibility(View.INVISIBLE);
                    mTvCheck11.setVisibility(View.INVISIBLE);
                    mTvCheck12.setVisibility(View.INVISIBLE);
                    mTvCheck13.setVisibility(View.INVISIBLE);
                    mTvCheck14.setVisibility(View.INVISIBLE);
                    mTvCheck15.setVisibility(View.INVISIBLE);
                    //重新评测,那么就显示评测界面1
                    mllVisible1.setVisibility(View.VISIBLE);
                    mtvRiskTitle6.setVisibility(View.INVISIBLE);
                    //重新评测,那么就隐藏重新评测按钮，将提交按钮设置为不可点击
                    mBtnRiskRestart.setVisibility(View.GONE);

                    mBtRiskSubject.setBackgroundColor(Color.parseColor("#666666"));
                    mBtRiskSubject.setClickable(false);
                    //隐藏4个界面的下一步按钮下一步的按钮
                    mNext1.setVisibility(View.INVISIBLE);
                    mNext2.setVisibility(View.INVISIBLE);
                    mNext3.setVisibility(View.INVISIBLE);
                    mNext4.setVisibility(View.INVISIBLE);

                    //当用户点击了重新评测按钮，进行重新评测时，但是有没有重新评测完，这时点击返回键时，
                    // 账户界面的显示状态还没有得到更新，所以，当点击重新评测时，发送广播，刷新我的账户界面的显示状态
                    intent=new Intent();
                    intent.setAction("refresh_my_user_risk");
                    intent.addCategory(Intent.ACTION_DEFAULT);
                    intent.putExtra("newRisk",riskCapacityLevelText);
                    intent.putExtra("newCode",riskCapacityLevelCode);
                    sendBroadcast(intent);

                break;


                case R.id.bt_risk_restarted://界面0的重新评测按钮
                    restartRisk();
                break;
                default:
                    break;
            }

        }
        super.onClick(view);
    }

    /**
     * 界面0的重新评测按钮
     */
    private void restartRisk() {
        //点击重新评测，那么就将界面0隐藏，显示界面1
        mllVisible1.setVisibility(View.VISIBLE);
        mllVisible0.setVisibility(View.INVISIBLE);

    }

    /**
     * 请求网络，提交数据,获取用户的风险类型
     * @param map
     */
    private void riskSubject(HashMap<Integer, Integer> map) {
        String url="/ums/questionnaire/answers";
        url=UrlHelper.getUrl(url);

        int str1=0;
        int str2=0;
        int str3=0;
        int str4=0;
        int str5=0;
        for (Integer in : map.keySet()) {
           //map.keySet()返回的是所有key的值
           str1 = map.get(1);//得到每个key多对用value的值
           str2 = map.get(2);//得到每个key多对用value的值
           str3 = map.get(3);//得到每个key多对用value的值
           str4 = map.get(4);//得到每个key多对用value的值
           str5 = map.get(5);//得到每个key多对用value的值

           }
        Lg.e("map....",str1+".."+str2+".."+str3+".."+str4+".."+str5);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("1",str1);
            jsonObject.put("2",str2);
            jsonObject.put("3",str3);
            jsonObject.put("4",str4);
            jsonObject.put("5",str5);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    Lg.e("评测成功,",response);
                        JSONObject jsonObj=response;
                        String riskMessage = (String) jsonObj.opt("riskCapacityLevelText");
                        String riskCapacityLevelcode = (String) jsonObj.opt("riskCapacityLevelCode");
                        Lg.e("riskMessage",riskMessage);
                        if(userInfo!=null){
                            UserInfo.RiskCapacityBean riskCapacity = userInfo.getRiskCapacity();
                            if (riskCapacity != null) {
                                //String riskCapacityLevelText = riskCapacity.getRiskCapacityLevelText();
                                //设置风险偏好
                                riskCapacity.setRiskCapacityLevelText(riskMessage);
                                riskCapacity.setRiskCapacityLevelCode(riskCapacityLevelcode);
                            }
                        }

                        //风险偏好设置成功后,显示评测完成界面
                        checkRiskFinish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String errorMsg = new String(error.networkResponse.data);
                    JSONObject json = new JSONObject(errorMsg);
                    if (json != null) {
                        String message = (String) json.opt("message");
                        if (message != null) {
                            AlertUtil.showOneBtnDialog(RiskManagerActivity.this, message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        } else {
                            AlertUtil.showOneBtnDialog(RiskManagerActivity.this, "风险评测失败", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
//                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        BaseApplication.getRequestQueue().add(jsonObjectRequest);

    }

    /**
     * 风险偏好设置成功后,显示评测完成界面
     */
    private void checkRiskFinish() {
        //显示评测完成界面
        mtvRiskTitle6.setVisibility(View.VISIBLE);
        //评测完成界面显示，那么就显示重新评测按钮
        mBtnRiskRestart.setVisibility(View.VISIBLE);

        userInfo = BaseApplication.getInstance().getUserInfo();
        if(userInfo!=null){
            //设置风险偏好
            UserInfo.RiskCapacityBean riskCapacity = userInfo.getRiskCapacity();
            if (riskCapacity != null) {
                riskCapacityLevelText = riskCapacity.getRiskCapacityLevelText();
                //设置风险偏好
                mTvRiskFinish.setText("您的投资风格为:"+ riskCapacityLevelText);
                riskCapacityLevelCode = riskCapacity.getRiskCapacityLevelCode();
               if(riskCapacityLevelCode.equals(desc1)){
                   mTvFinish.setText("较为适合与投资低风险理财产品");
                   mIvImageFinish.setImageResource(R.drawable.conservative);
               }else if(riskCapacityLevelCode.equals(desc2)){
                   mTvFinish.setText("较为适合与投资中等风险理财产品");
                   mIvImageFinish.setImageResource(R.drawable.steadiness);
               }else if(riskCapacityLevelCode.equals(desc3)){
                   mTvFinish.setText("较为适合与投资高风险理财产品");
                   mIvImageFinish.setImageResource(R.drawable.radical);
               }

            }
        }

    }


    //请求服务器，获取评测问题
    private void getRiskProblem() {

        String url="/ums/questionnaire";
        url= UrlHelper.getUrl(url);

        JSONObject jsonObject=new JSONObject();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("risk_response",response);
                        JSONObject obj = (JSONObject) response;
                        String  cookie= obj.optString("cookie");

                        String questions = obj.optString("questions");
                        //[{"required":true,"id":1,"title":"您对互联网的使用经验如何：","options":[{"value":1,"label":"生活必需"},{"value":2,"label":"时常会用"},{"value":3,"label":"几乎不会使用"}]},{"required":true,"id":2,"title":"您的家庭年收入或个人年收入：","options":[{"value":4,"label":"家庭年收入30万元以下或个人收入20万以下"},{"value":5,"label":"家庭年收入30万元-50万元或个人收入20万-50万"},{"value":6,"label":"家庭或个人年收入50万元以上"}]},{"required":true,"id":3,"title":"在投资期限越长收益越高的前提下，您在投资理财产品时期望的投资期限是多长：","options":[{"value":7,"label":"半年以内，越短越好"},{"value":8,"label":"6个月-1年"},{"value":9,"label":"1年以上"}]},{"required":true,"id":4,"title":"您投资过金融产品都有哪些类别？","options":[{"value":10,"label":"除银行存款、国债、银行理财产品外，没有投资过其他金融产品"},{"value":11,"label":"购买过股票、债券、公募基金、信托计划、资管计划等产品"},{"value":12,"label":"购买过P2P、互联网理财等产品"}]},{"required":true,"id":5,"title":"您认为您投资中是什么样的风险承担者：","options":[{"value":13,"label":"无法承担风险"},{"value":14,"label":"虽然厌恶风险但愿意承担一些风险"},{"value":15,"label":"风险承受能力较强，比较激进"}]}]
                        Lg.e("questions",questions);

                        ArrayList<Risk> infos=(ArrayList<Risk>)JsonUtil.parseJsonToList(questions,new TypeToken<List<Risk>>(){}.getType());

                        Lg.e("fjaskdfnksajf",infos);

                        for (int i=0;i<infos.size();i++){
                            Lg.e("infos.......",infos.get(i));
                            Lg.e("user----id",infos.get(i).getId());
                            Lg.e("id---options",infos.get(i).getOptions());
                            //Lg.e("id---options",infos.get(i).getOptions().get(i).toString());

                            if(i==0&&infos.get(0).getId()==1){

                                mTvRiskTitle1.setText(infos.get(0).getId()+"."+infos.get(0).getTitle());
                                mTvNumber1.setText(infos.get(0).getId()+"/5");
                                List<Risk.OptionsBean> options = infos.get(0).getOptions();

                                for (int j=0;j<options.size();j++){
                                    mtvRiskDesc1.setText("A."+options.get(0).getLabel());
                                    mtvRiskDesc2.setText("B."+options.get(1).getLabel());
                                    mtvRiskDesc3.setText("C."+options.get(2).getLabel());
                                }
                            }else if(i==1&&infos.get(1).getId()==2){
                                mtvRiskTitle2.setText(infos.get(1).getId()+"."+infos.get(1).getTitle());
                                mtvNumber2.setText(infos.get(1).getId()+"/5");

                                List<Risk.OptionsBean> options = infos.get(1).getOptions();
                                for (int j=0;j<options.size();j++){
                                    mtvRiskDesc4.setText("A."+options.get(0).getLabel());
                                    mtvRiskDesc5.setText("B."+options.get(1).getLabel());
                                    mtvRiskDesc6.setText("C."+options.get(2).getLabel());
                                }
                            }else if(i==2&&infos.get(2).getId()==3){
                                mtvRiskTitle3.setText(infos.get(2).getId()+"."+infos.get(2).getTitle());
                                mtvNumber3.setText(infos.get(2).getId()+"/5");

                                List<Risk.OptionsBean> options = infos.get(2).getOptions();
                                for (int j=0;j<options.size();j++){
                                    mtvRiskDesc7.setText("A."+options.get(0).getLabel());
                                    mtvRiskDesc8.setText("B."+options.get(1).getLabel());
                                    mtvRiskDesc9.setText("C."+options.get(2).getLabel());
                                }

                            }else if(i==3&&infos.get(3).getId()==4){
                                mtvRiskTitle4.setText(infos.get(3).getId()+"."+infos.get(3).getTitle());
                                mtvNumber4.setText(infos.get(3).getId()+"/5");

                                List<Risk.OptionsBean> options = infos.get(3).getOptions();
                                for (int j=0;j<options.size();j++){
                                    mtvRiskDesc10.setText("A."+options.get(0).getLabel());
                                    mtvRiskDesc11.setText("B."+options.get(1).getLabel());
                                    mtvRiskDesc12.setText("C."+options.get(2).getLabel());
                                }
                            }else if(i==4&&infos.get(4).getId()==5){
                                mtvRiskTitle5.setText(infos.get(4).getId()+"."+infos.get(4).getTitle());
                                mtvNumber5.setText(infos.get(4).getId()+"/5");

                                List<Risk.OptionsBean> options = infos.get(4).getOptions();
                                for (int j=0;j<options.size();j++){
                                    mtvRiskDesc13.setText("A."+options.get(0).getLabel());
                                    mtvRiskDesc14.setText("B."+options.get(1).getLabel());
                                    mtvRiskDesc15.setText("C."+options.get(2).getLabel());
                                }
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        BaseApplication.getRequestQueue().add(jsonObjectRequest);
    }


    //界面1的下一步按钮的显示，或隐藏
    private void textviewIsCheck1() {
        if(mtvCheck1.getVisibility()==View.VISIBLE||mtvCheck2.getVisibility()==View.VISIBLE||mtvCheck3.getVisibility()==View.VISIBLE){
            mNext1.setVisibility(View.VISIBLE);
        }else {
            mNext1.setVisibility(View.INVISIBLE);
        }

    }

    //界面2的上一步，下一步按钮的显示，或隐藏
    private void textviewIsCheck2() {
        if(mTvCheck4.getVisibility()==View.VISIBLE||mTvCheck5.getVisibility()==View.VISIBLE||mTvCheck6.getVisibility()==View.VISIBLE){
            mNext2.setVisibility(View.VISIBLE);
        }else {
            mNext2.setVisibility(View.INVISIBLE);
        }

    }

    //界面3的上一步，下一步按钮的显示，或隐藏
    private void textviewIsCheck3() {
        if(mTvCheck7.getVisibility()==View.VISIBLE||mTvCheck8.getVisibility()==View.VISIBLE||mTvCheck9.getVisibility()==View.VISIBLE){
            mNext3.setVisibility(View.VISIBLE);
        }else {
            mNext3.setVisibility(View.INVISIBLE);
        }

    }

    //界面4的上一步，下一步按钮的显示，或隐藏
    private void textviewIsCheck4() {
        if(mTvCheck10.getVisibility()==View.VISIBLE||mTvCheck11.getVisibility()==View.VISIBLE||mTvCheck12.getVisibility()==View.VISIBLE){
            mNext4.setVisibility(View.VISIBLE);
        }else {
            mNext4.setVisibility(View.INVISIBLE);
        }

    }

    //界面5的提交按钮的显示，或隐藏
    private void textviewIsCheck5() {
        if(mTvCheck13.getVisibility()==View.VISIBLE||mTvCheck14.getVisibility()==View.VISIBLE||mTvCheck15.getVisibility()==View.VISIBLE){
            mBtRiskSubject.setClickable(true);
            mBtRiskSubject.setEnabled(true);
            mBtRiskSubject.setBackgroundColor(Color.parseColor("#ff6600"));
        }else {
            mBtRiskSubject.setBackgroundColor(Color.parseColor("#666666"));
            mBtRiskSubject.setClickable(false);
            mBtRiskSubject.setEnabled(false);
        }

    }

}
