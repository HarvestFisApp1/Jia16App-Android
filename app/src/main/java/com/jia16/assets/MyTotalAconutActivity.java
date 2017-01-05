package com.jia16.assets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.util.AmountUtil;
import com.jia16.util.DensityUtil;
import com.jia16.util.Lg;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * 我的资产--总资产界面
 */
public class MyTotalAconutActivity extends BaseActivity {


    private static final int CHART_SPACING = 1;//图标间隔 2dp
    private PieChartView chart;
    private PieChartData mChartData;
    private SliceValue sliceValueAvailable;//可用资产
    private SliceValue sliceValueInvest;//投资中
    private SliceValue sliceValueFreeze;//冻结资金
    private double mtotal_acount;//资产界面传递过来的总资产
    private double available_acount;//可用资产
    private double invest_acount;//投资中
    private double freeze_acount;//冻结资金
    private TextView mTvAvailable;//可用金额显示
    private TextView mTvInvester;//投资中的金额显示
    private TextView mTvFreeze;//冻结资金显示
    private ImageView mTvOvalMain;
    private ImageView mIvOvalRed;
    private ImageView mIvOvalYellow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_total_acount);

        Intent intent = getIntent();
        mtotal_acount = intent.getDoubleExtra("mtotal_acount", 0);
        available_acount = intent.getDoubleExtra("available_acount", 0);
        invest_acount = intent.getDoubleExtra("invest_acount", 0);
        freeze_acount = intent.getDoubleExtra("freeze_acount", 0);

        Lg.e("传递过来的数据。。。。", mtotal_acount, available_acount, invest_acount, freeze_acount);
        //初始化布局
        initView();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        ((TextView) findViewById(R.id.title_text)).setText("账户详情");

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvAvailable = (TextView) findViewById(R.id.tv_available);
        mTvAvailable.setText(AmountUtil.addComma(AmountUtil.DT.format(available_acount)));

        mTvInvester = (TextView) findViewById(R.id.tv_invest);
        mTvInvester.setText(AmountUtil.addComma(AmountUtil.DT.format(invest_acount)));

        mTvFreeze = (TextView) findViewById(R.id.tv_freeze);
        mTvFreeze.setText(AmountUtil.addComma(AmountUtil.DT.format(freeze_acount)));


        mTvOvalMain = (ImageView) findViewById(R.id.iv_oval_main);
        mIvOvalRed = (ImageView) findViewById(R.id.iv_oval_red);
        mIvOvalYellow = (ImageView) findViewById(R.id.iv_oval_yellow);

        chart = (PieChartView) findViewById(R.id.chart);
        chart.setChartRotation(-90, false);
        chart.setChartRotationEnabled(false);
        initChart();

        setChartData();

    }

    private void setChartData() {

        sliceValueAvailable.setTarget((float) available_acount);
        sliceValueInvest.setTarget((float) invest_acount);
        sliceValueFreeze.setTarget((float) freeze_acount);
        chart.startDataAnimation(2000);
    }

    private void initChart() {
        chart.animationDataUpdate(0.8f);
        mChartData = new PieChartData();
        mChartData.setHasCenterCircle(true);
        mChartData.setCenterCircleScale(0.8f);
        List<SliceValue> mData = new ArrayList<>();

        //可用资产
        sliceValueAvailable = new SliceValue();
        sliceValueAvailable.setColor(getColor(R.color.main_color));
        sliceValueAvailable.setTarget(0);

        //投资中
        sliceValueInvest = new SliceValue();
        sliceValueInvest.setColor(getColor(R.color.progress_di));
        sliceValueInvest.setTarget(0);

        //冻结资金
        sliceValueFreeze = new SliceValue();
        sliceValueFreeze.setColor(getColor(R.color.progress_zhong));
        sliceValueFreeze.setTarget(0);


        mData.add(sliceValueAvailable);
        mData.add(sliceValueInvest);
        mData.add(sliceValueFreeze);
        mChartData.setValues(mData);

        mChartData.setSlicesSpacing(DensityUtil.dip2px(this, CHART_SPACING));
        //mChartData.setCenterText1(AmountUtil.addComma(AmountUtil.DT.format(mtotal_acount)));

        mChartData.setCenterText1("总资产(元)");
        mChartData.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.text_size_16)));


        mChartData.setCenterText2(AmountUtil.addComma(AmountUtil.DT.format(mtotal_acount)));
        mChartData.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.text_size_14)));
        mChartData.setCenterText1Color(getColor(R.color.main_color));
        mChartData.setCenterText2Color(getColor(R.color.main_color));

        //如果用户还没有去投资，就设置默认的背景颜色为灰色
        if(mtotal_acount==0){
            mData.clear();
            SliceValue slicevalus = new SliceValue();
            slicevalus.setColor(getColor(R.color.text_gray));
            slicevalus.setTarget((float) 100);
            mData.add(slicevalus);
            mChartData.setValues(mData);

            //将总资产的文字，设置为灰色
            mChartData.setCenterText1Color(getColor(R.color.text_gray));
            mChartData.setCenterText2Color(getColor(R.color.text_gray));

            //更改小圆点的颜色为灰色
            //mTvOvalMain.setImageDrawable(getDrawable(R.drawable.shape_oval_solid_main));
            mTvOvalMain.setImageResource(R.drawable.shape_oval_no_dot);
            mIvOvalRed.setImageResource(R.drawable.shape_oval_no_dot);
            mIvOvalYellow.setImageResource(R.drawable.shape_oval_no_dot);
        }


        chart.setPieChartData(mChartData);
    }


    private int getColor(int colorRes) {
        return getResources().getColor(colorRes);
    }
}
