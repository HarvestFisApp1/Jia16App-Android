package com.jia16.assets;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.util.AlertUtil;
import com.jia16.util.AmountUtil;
import com.jia16.util.Lg;
import com.jia16.util.PopupWindowUtils;
import com.jia16.util.TimeUtils;
import com.jia16.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 我的资产--申请转让--补充界面
 */
public class ApplyTransferNextActivity extends BaseActivity {

    /**
     * 申请转让界面传递过来的数据
     */
    private double transferPrincipal;//转让本金
    private double dueEarn;//应得收益
    private String annualRate;//挂牌收益率
    private double TransferChargeed;//转让的手续费
    private String title;//标的名称
    private int investsId;//投资的id
    private int subjectId;//subjectid

    private double sumTransferAmount=0;

    /**
     * 初始化布局
     */
    private TextView mTvTransferPrincipal;//转让本金
    private TextView mTvDueEarn;//应得收益
    private TextView mTvSumAmount;//获得的总的金额
    private RadioButton mRbCheck1;//涨的按钮
    private RadioButton mRbCheck2;//降的按钮
    private EditText mEtAmount;//涨降价金额的输入框
    private TextView mTvTransferEarn;//挂牌收益率
    private TextView mTvTransferCharge;//手续费
    private TextView mTvPraticalAmount;//实际到账金额
    private TextView mTvTransferDate;//转让生效日期
    private TextView mTvCaculateMode;//计算方式
    private Button mBtTransferNext;//下一步按钮

    //转让天数的按钮
    private RadioButton mRbDateCheck1;
    private RadioButton mRbDateCheck2;
    private RadioButton mRbDateCheck3;
    private RadioButton mRbDateCheck4;
    private RadioButton mRbDateCheck5;

    private TextView mTvTransferAmount;//转让价格
    private String editTextss;//获取editText输入框中输入的涨或者降价的金额
    private double sumAmount;//获取总的金额=本金+收益
    private double practicalAmount;//实际到账金额=转让价格-手续费
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_fransfer_next);

        Intent intent = getIntent();
        transferPrincipal = intent.getDoubleExtra("transferPrincipal", 0);
        dueEarn = intent.getDoubleExtra("dueEarn", 0);
        annualRate = intent.getStringExtra("annualRate");
        String transferCharge = intent.getStringExtra("transferCharge");
        TransferChargeed = Double.parseDouble(transferCharge);
        title = intent.getStringExtra("title");
        investsId = intent.getIntExtra("investsId", 0);
        subjectId = intent.getIntExtra("subjectId", 0);

        //初始化布局
        initView();

        //绑定数据
        initData();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        ((TextView) findViewById(R.id.title_text)).setText("转让标的信息");

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvTransferPrincipal = (TextView) findViewById(R.id.tv_transfere_principal);
        mTvDueEarn = (TextView) findViewById(R.id.tv_due_earn);
        mTvSumAmount = (TextView) findViewById(R.id.tv_sum_amount);
        mRbCheck1 = (RadioButton) findViewById(R.id.rb_check1);
        mRbCheck1.setOnClickListener(this);
        mRbCheck2 = (RadioButton) findViewById(R.id.rb_check2);
        mRbCheck2.setOnClickListener(this);
        mEtAmount = (EditText) findViewById(R.id.et_amount);

        //转让天数的按钮
        mRbDateCheck1 = (RadioButton) findViewById(R.id.rb_date_check1);
        mRbDateCheck2 = (RadioButton) findViewById(R.id.rb_date_check2);
        mRbDateCheck3 = (RadioButton) findViewById(R.id.rb_date_check3);
        mRbDateCheck4 = (RadioButton) findViewById(R.id.rb_date_check4);
        mRbDateCheck5 = (RadioButton) findViewById(R.id.rb_date_check5);
        mRbDateCheck1.setOnClickListener(this);
        mRbDateCheck2.setOnClickListener(this);
        mRbDateCheck3.setOnClickListener(this);
        mRbDateCheck4.setOnClickListener(this);
        mRbDateCheck5.setOnClickListener(this);

        mTvTransferAmount = (TextView) findViewById(R.id.tv_transfer_amount);
        mTvTransferEarn = (TextView) findViewById(R.id.tv_transfer_earn);
        mTvTransferCharge = (TextView) findViewById(R.id.tv_transfer_charge);
        mTvPraticalAmount = (TextView) findViewById(R.id.tv_practical_amount);
        mTvTransferDate = (TextView) findViewById(R.id.tv_transfer_date);
        mTvCaculateMode = (TextView) findViewById(R.id.tv_caculate_mode);
        mTvCaculateMode.setOnClickListener(this);

        mBtTransferNext = (Button) findViewById(R.id.bt_transfer_next);
        mBtTransferNext.setOnClickListener(this);
    }

    /**
     * 绑定数据
     */
    private void initData() {
        mTvTransferPrincipal.setText(AmountUtil.addComma(AmountUtil.DT.format(transferPrincipal)));
        mTvDueEarn.setText(AmountUtil.addComma(AmountUtil.DT.format(dueEarn)));
        //获取总的金额=本金+收益
        sumAmount = transferPrincipal+dueEarn;
        mTvSumAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(sumAmount)));
        //设置转让价格
        mTvTransferAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(sumAmount))+"元");
        //设置editText的监听，动态设置textview显示的文本
        mEtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextss = mEtAmount.getText().toString();
                if(!TextUtils.isEmpty(editTextss)){
                    double investAmount = Double.parseDouble(editTextss);//输入的涨或降的金额
                    //计算应得收益=年化收益*已投资天数*投资金额/360
                    //转让价格=转让本金+应得收益+涨或降的金额

                   if(mRbCheck1.isChecked()){
                       sumTransferAmount= sumAmount +investAmount;//转让价格
                   }else if(mRbCheck2.isChecked()){
                       sumTransferAmount= sumAmount -investAmount;//转让价格
                   }
                    mTvTransferAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(sumTransferAmount))+"元");

                    //实际到账金额=转让价格-手续费
                    practicalAmount = sumTransferAmount - TransferChargeed;
                    mTvPraticalAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(practicalAmount))+"元");
                }else {
                    mTvTransferAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(sumAmount))+"元");

                    //实际到账金额=转让价格-手续费
                    practicalAmount = sumAmount - TransferChargeed;
                    mTvPraticalAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(practicalAmount))+"元");
                }

            }
        });

        //挂牌收益率
        mTvTransferEarn.setText(annualRate+"%");
        //手续费
        mTvTransferCharge.setText(AmountUtil.addComma(AmountUtil.DT.format(TransferChargeed))+"元");
        //实际到账金额=转让价格-手续费
        practicalAmount = sumAmount - TransferChargeed;
        mTvPraticalAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(practicalAmount))+"元");
        //转让生效日期=当前日期 + 一天的时间
        long transferDate = TimeUtils.getCurrentTimeInLong() + 86400000;
        String time = TimeUtils.getTime(transferDate, TimeUtils.DATE_FORMAT_DATE);
        mTvTransferDate.setText(time);

    }

    @Override
    public void onClick(View view) {

        //获取editText输入框中输入的金额
        editTextss = mEtAmount.getText().toString();
        double investAmount;

        long transferDate;
        String time;

        switch (view.getId()){
            case R.id.rb_check1://涨的按钮
                mRbCheck1.setChecked(true);
                mRbCheck2.setChecked(false);

                if(!TextUtils.isEmpty(editTextss)){
                    investAmount = Double.parseDouble(editTextss);
                    //根据涨或降价的按钮，动态更新转让的价格
                    //表示是涨
                    //转让价格=转让本金+应得收益+涨或降的金额
                    sumTransferAmount=sumAmount+investAmount;
                    mTvTransferAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(sumTransferAmount))+"元");


                    //实际到账金额=转让价格-手续费
                    practicalAmount = sumTransferAmount - TransferChargeed;
                    mTvPraticalAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(practicalAmount))+"元");
                }else {
                    //表示输入框没有输入金额
                    mTvTransferAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(sumAmount))+"元");

                    //实际到账金额=转让价格-手续费
                    practicalAmount = sumAmount - TransferChargeed;
                    mTvPraticalAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(practicalAmount))+"元");

                }
            break;

            case R.id.rb_check2://降的按钮

                mRbCheck1.setChecked(false);
                mRbCheck2.setChecked(true);

                if(!TextUtils.isEmpty(editTextss)){
                    investAmount = Double.parseDouble(editTextss);
                    //根据涨或降价的按钮，动态更新转让的价格
                    //表示是降价
                    //转让价格=转让本金+应得收益-降的金额
                    sumTransferAmount=sumAmount-investAmount;
                    mTvTransferAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(sumTransferAmount))+"元");

                    //实际到账金额=转让价格-手续费
                    practicalAmount = sumTransferAmount - TransferChargeed;
                    mTvPraticalAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(practicalAmount))+"元");
                }else {
                    //表示输入框没有输入金额
                    mTvTransferAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(sumAmount))+"元");

                    //表示输入框没有输入金额，实际到账金额=转让价格-手续费
                    practicalAmount = sumAmount - TransferChargeed;
                    mTvPraticalAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(practicalAmount))+"元");
                }


                break;

            case R.id.rb_date_check1://挂牌转让天数1天
                mRbDateCheck1.setChecked(true);
                mRbDateCheck2.setChecked(false);
                mRbDateCheck3.setChecked(false);
                mRbDateCheck4.setChecked(false);
                mRbDateCheck5.setChecked(false);

                //转让生效日期=当前日期 + 一天的时间
                transferDate = TimeUtils.getCurrentTimeInLong() + 86400000;
                time = TimeUtils.getTime(transferDate, TimeUtils.DATE_FORMAT_DATE);
                mTvTransferDate.setText(time);

            break;

            case R.id.rb_date_check2://挂牌转让天数2天
                mRbDateCheck1.setChecked(false);
                mRbDateCheck2.setChecked(true);
                mRbDateCheck3.setChecked(false);
                mRbDateCheck4.setChecked(false);
                mRbDateCheck5.setChecked(false);

                //转让生效日期=当前日期 + 一天的时间
                transferDate = TimeUtils.getCurrentTimeInLong() + 86400000*2;
                time = TimeUtils.getTime(transferDate, TimeUtils.DATE_FORMAT_DATE);
                mTvTransferDate.setText(time);
                break;

            case R.id.rb_date_check3://挂牌转让天数3天
                mRbDateCheck1.setChecked(false);
                mRbDateCheck2.setChecked(false);
                mRbDateCheck3.setChecked(true);
                mRbDateCheck4.setChecked(false);
                mRbDateCheck5.setChecked(false);

                //转让生效日期=当前日期 + 一天的时间
                transferDate = TimeUtils.getCurrentTimeInLong() + 86400000*3;
                time = TimeUtils.getTime(transferDate, TimeUtils.DATE_FORMAT_DATE);
                mTvTransferDate.setText(time);

                break;

            case R.id.rb_date_check4://挂牌转让天数4天
                mRbDateCheck1.setChecked(false);
                mRbDateCheck2.setChecked(false);
                mRbDateCheck3.setChecked(false);
                mRbDateCheck4.setChecked(true);
                mRbDateCheck5.setChecked(false);

                //转让生效日期=当前日期 + 一天的时间
                transferDate = TimeUtils.getCurrentTimeInLong() + 86400000*4;
                time = TimeUtils.getTime(transferDate, TimeUtils.DATE_FORMAT_DATE);
                mTvTransferDate.setText(time);

                break;

            case R.id.rb_date_check5://挂牌转让天数5天
                mRbDateCheck1.setChecked(false);
                mRbDateCheck2.setChecked(false);
                mRbDateCheck3.setChecked(false);
                mRbDateCheck4.setChecked(false);
                mRbDateCheck5.setChecked(true);

                //转让生效日期=当前日期 + 一天的时间
                transferDate = TimeUtils.getCurrentTimeInLong() + 86400000*5;
                time = TimeUtils.getTime(transferDate, TimeUtils.DATE_FORMAT_DATE);
                mTvTransferDate.setText(time);
                break;

            case R.id.tv_caculate_mode://计算方式

                //弹出弹框，显示计算方式
                // 一个自定义的布局，作为显示的内容
                View contentView = LayoutInflater.from(ApplyTransferNextActivity.this).inflate(
                        R.layout.calculate_popwindow, null);
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

            case R.id.bt_transfer_next://下一步按钮
                Intent intent=new Intent(ApplyTransferNextActivity.this,ApplyTransferAffirmActivity.class);
                //标的名称
                intent.putExtra("title",title);
                //投资的id
                intent.putExtra("investsId",investsId);
                //subjectid
                intent.putExtra("subjectId",subjectId);
                //转让本金
                intent.putExtra("transferPrincipal",transferPrincipal);

                String floatAmount = null;
                //涨或者降价的金额
                if(mRbCheck1.isChecked()){
                    //表示是涨，涨价是正（+）
                    if(!TextUtils.isEmpty(editTextss)){//表示输入框中有金额
                        investAmount = Double.parseDouble(editTextss);
                        //根据涨或降价的按钮，动态更新转让的价格
                        //表示是涨
                        floatAmount=investAmount+"";
                    }else {
                        floatAmount=0+"";
                    }

                }else if(mRbCheck2.isChecked()){
                    //表示是降价  降价是负（-）
                    if(!TextUtils.isEmpty(editTextss)){//表示输入框中有金额
                        investAmount = Double.parseDouble(editTextss);
                        //根据涨或降价的按钮，动态更新转让的价格
                        //表示是涨
                        floatAmount="-"+investAmount;
                    }else {
                        floatAmount=0+"";
                    }
                }
                intent.putExtra("floatAmount",floatAmount);

                if(!TextUtils.isEmpty(editTextss)){
                    investAmount = Double.parseDouble(editTextss);

                    if(mRbCheck1.isChecked()){
                        //根据涨或降价的按钮，动态更新转让的价格
                        //表示是降价
                        //转让价格=转让本金+应得收益+降的金额
                        sumTransferAmount=sumAmount+investAmount;

                    }else if(mRbCheck2.isChecked()){
                        //根据涨或降价的按钮，动态更新转让的价格
                        //表示是降价
                        //转让价格=转让本金+应得收益-降的金额
                        sumTransferAmount=sumAmount-investAmount;
                    }

                    //转让价格
                    intent.putExtra("transferAmount",sumTransferAmount);

                    //实际到账金额=转让价格-手续费
                    practicalAmount = sumTransferAmount - TransferChargeed;
                    intent.putExtra("practicalAmount",practicalAmount);
                }else {
                    //表示输入框没有输入金额
                    intent.putExtra("transferAmount",sumAmount);

                    //表示输入框没有输入金额，实际到账金额=转让价格-手续费
                    practicalAmount = sumAmount - TransferChargeed;
                    intent.putExtra("practicalAmount",practicalAmount);
                }

                if(mRbDateCheck1.isChecked()){
                    intent.putExtra("transferDate",1);
                }else if(mRbDateCheck2.isChecked()){
                    intent.putExtra("transferDate",2);
                }else if(mRbDateCheck3.isChecked()){
                    intent.putExtra("transferDate",3);
                }else if(mRbDateCheck4.isChecked()){
                    intent.putExtra("transferDate",4);
                }else if(mRbDateCheck5.isChecked()){
                    intent.putExtra("transferDate",5);
                }

                startActivity(intent);
                finish();

            break;

            default:
                break;
        }
        super.onClick(view);
    }
}
