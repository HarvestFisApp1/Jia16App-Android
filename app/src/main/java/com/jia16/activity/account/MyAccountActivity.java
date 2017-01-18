package com.jia16.activity.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.activity.GesturePwdActivity;
import com.jia16.activity.LoginActivity;
import com.jia16.activity.UnlockGesturePasswordActivity;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.LockPwd;
import com.jia16.bean.UserInfo;
import com.jia16.util.AlertUtil;
import com.jia16.util.Constants;
import com.jia16.util.Lg;
import com.jia16.util.ToastUtil;
import com.jia16.util.UrlHelper;
import com.jia16.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的账户----界面
 *
 * @author jiaohongyun
 * @date 2015年5月25日
 */
public class MyAccountActivity extends BaseActivity {


    private static final int RISK_GESTURE_PASSWORD = 1;
    private static final int MODIFICATION = 2;
    private static final int REQUEST_CODE_CHANGE_GUESTURE = 10004;
    private static final int REQUEST_CODE_SET_GUESTURE = 10005;
    private static final int RISKMANAGER = 10006;


    /**
     * 用户名
     */
    private LinearLayout mllCommonAccount;
    private TextView mTvCommonAccount;
    private ImageView mIvUserImage;

    /**
     * 修改手机号一栏
     */
    private LinearLayout mllRevisePhone;
    private TextView mTvRevisePhone;

    /**
     * 风险偏好
     */
    private LinearLayout mllCommonRisk;
    private TextView mTvCommonRisk;

    /**
     * 姓名
     */
    private TextView mTvCommonName;
    private LinearLayout mllcommonName;
    /**
     * 身份证号码
     */
    private TextView mTvCommonCard;
    private LinearLayout mllCommonCard;
    /**
     * 银行卡
     */
    private LinearLayout mllBankCard;
    private TextView mTvCommonBankCard;
    /**
     * 修改银行预留手机
     */
    private LinearLayout mllCommonRiskPhone;
    private TextView mTvCommonRiskPhone;
    /**
     * 修改登录密码
     */
    private LinearLayout mllLoginPassword;
    /**
     * 修改交易密码
     */
    private LinearLayout mllCommonTradePassword;

    /**
     * 关闭或打开手势密码
     */
    private TextView mTvGesterPassword;//关闭或打开手势密码的textview
    private CheckBox mSwitchPword;      //关闭或打开手势密码的开关
    private LinearLayout mGesturePassword;//关闭或打开手势密码的线性布局

    /**
     * 手势密码的三种状态
     */
    private final int STATE_SWITCH_SETTING = 101; //设置手势密码
    private final int STATE_SWITCH_CLOSE = 102;   //关闭手势密码
    private final int STATE_SWITCH_OPEN = 103;    //开启手势密码
    private int mySwitchState = STATE_SWITCH_SETTING;  //默认为需要设置手势密码状态

    /**
     * 修改手势密码
     */
    private LinearLayout mllCommonRiskGesturePassword;
    private UserInfo userInfo;

    /**
     * 退出登录的按钮
     */
    private Button mBtnCommonLoginout;
    private String bankReservedPhone;


    private BroadcastReceiver receiver;
    private UserInfo.CertificationBean certification;
    private List<UserInfo.BankCardsBean> bankCards;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);


        //获取用户信息
        getCurrentUser();

        initView();

        initData();
    }

    //已经绑定银行卡
    //{"bankCards":[{"cardNo":"6215593202011953935","id":36605,"accountType":"INVESTMENT","bankStatus":"VALID","links":[{"operation":"\/users\/97071\/bank-cards\/36605\/operation"}],"bankReservedPhone":"152****1667","bankCode":"01020000"}],"phone":"15250201667","lastLoginAt":1480660268275,"isChangeUserName":false,"needDealPassword":false,"type":"CUSTOMER","riskCapacity":{"riskCapacityLevelCode":"MODERATE","riskCapacityLevelText":"稳健型"},"uri":"\/users\/97071","cookie":"JSESSIONID=7272704284847165706; Path=\/","id":97071,"certification":{"certifiedIdentity":"420582198806135016","certifiedName":"尚晶"},"username":"jsl147367890616","createdAt":1473678906238,"active":true,"notifyDepositAgreement":true,"isBind":false}
    //没有绑定银行卡
    //{"bankCards":[],"phone":"15327601739","lastLoginAt":1480576663398,"isChangeUserName":true,"needDealPassword":true,"type":"CUSTOMER","riskCapacity":null,"uri":"\/users\/101375","id":101375,"certification":null,"username":"jsl1480556854691","createdAt":1480556854766,"active":false,"notifyDepositAgreement":true,"isBind":false}
    //绑定银行卡后解绑银行卡
    //{"bankCards":[],"phone":"15335710372","lastLoginAt":1480658263337,"isChangeUserName":false,"needDealPassword":false,"type":"CUSTOMER","riskCapacity":null,"uri":"\/users\/100879","id":100879,"certification":{"certifiedIdentity":"420582198806135016","certifiedName":"尚晶"},"username":"xxd15335710372","createdAt":1479729907516,"active":false,"notifyDepositAgreement":true,"isBind":false}
    private void initData() {
        //设置用户信息
        setUserInfo();

    }

    //设置用户信息
    private void setUserInfo() {
        userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {


            //设置用户名
            mTvCommonAccount.setText(userInfo.getUsername());
            //获取是否可以修改用户名（用户名只能修改一次）
            boolean isChangeUserName = userInfo.isIsChangeUserName();
            if (isChangeUserName) {
                //表示用户还没有修改过用户名,那么就可以去修改
                mIvUserImage.setVisibility(View.VISIBLE);
                mllCommonAccount.setClickable(true);
            } else {
                //表示用户已经修改过用户名,那么就隐藏箭头的图片，同时设置用户名的线性布局不响应点击事件
                mIvUserImage.setVisibility(View.GONE);
                mllCommonAccount.setClickable(false);
            }


            //设置用户登录的 手机号
            String commonPhone = userInfo.getPhone();
            String startSubstring = commonPhone.substring(0, 3);
            String endSubstring = commonPhone.substring(7);
            String newPhone = startSubstring + "****" + endSubstring;
            mTvRevisePhone.setText(newPhone);


            //设置风险偏好
            UserInfo.RiskCapacityBean riskCapacity = userInfo.getRiskCapacity();
            if (riskCapacity != null) {
                String riskCapacityLevelText = riskCapacity.getRiskCapacityLevelText();
                //设置风险偏好
                mTvCommonRisk.setText(riskCapacityLevelText);
            } else {
                mTvCommonRisk.setText("请进行风险评测后，再投资！");
            }


            //姓名
            //String certifiedName = userInfo.getCertification().getCertifiedName();
            certification = userInfo.getCertification();
            if (certification != null) {
                String certifiedName = certification.getCertifiedName();
                //表示已经绑定银行卡
                String subName = certifiedName.substring(0, 1);
                String endName = "";
                for (int i = 0; i < certifiedName.length() - 1; i++) {
                    endName += "*";
                }
                endName = subName + endName;
                mTvCommonName.setText(endName);

                mllcommonName.setVisibility(View.VISIBLE);

            } else {
                //表示用户还没有进行银行卡绑定，那么就隐藏姓名的线性布局
                mllcommonName.setVisibility(View.GONE);
            }


            //身份证号码
            //String certifiedIdentity = userInfo.getCertification().getCertifiedIdentity();
            if (certification != null) {
                //表示已经绑定银行卡
                String certifiedIdentity = certification.getCertifiedIdentity();
                String subStartIdentity = certifiedIdentity.substring(0, 6);
                String subLastIdentity = certifiedIdentity.substring(14);
                String endIdentity = subStartIdentity + "********" + subLastIdentity;
                mTvCommonCard.setText(endIdentity);

                mllCommonCard.setVisibility(View.VISIBLE);
            } else {
                //表示用户还没有进行银行卡绑定，那么久隐藏身份证号码的线性布局
                mllCommonCard.setVisibility(View.GONE);
            }


            //银行卡
            bankCards = userInfo.getBankCards();
            if (bankCards.size() != 0) {
                //表示已经绑定银行卡
                UserInfo.BankCardsBean cardsBean = bankCards.get(0);
                String cardNo = cardsBean.getCardNo();
                String subCardNo = cardNo.substring(cardNo.length() - 4);
                String subStartCardNo = "";
                for (int i = 0; i < cardNo.length() - 4; i++) {
                    subStartCardNo += "*";
                }
                String subLastCardNo = subStartCardNo + subCardNo;
                mTvCommonBankCard.setText(subLastCardNo);

                //表示已经绑定银行卡，那么就不能点击线性布局
                mllBankCard.setClickable(false);

            } else {

                //表示用户还没有进行银行卡绑定，那么就更改银行卡的状态
                mTvCommonBankCard.setText("未绑定 >");

                //表示没有绑定银行卡,那么就可以让线性布局可以点击
                mllBankCard.setClickable(true);
            }


            //修改银行预留手机
            if (bankCards.size() != 0) {
                UserInfo.BankCardsBean bankCardsBean = bankCards.get(0);
                bankReservedPhone = bankCardsBean.getBankReservedPhone();
                //表示已经绑定银行卡
                mTvCommonRiskPhone.setText(bankReservedPhone);
                //显示线性布局
                mllCommonRiskPhone.setVisibility(View.VISIBLE);
            } else {
                //表示用户还没有进行银行卡绑定，那么就将修改银行预留手机的线性布局隐藏
                mllCommonRiskPhone.setVisibility(View.GONE);
            }


            //修改交易密码
            //如果用户都还没有进行银行卡的绑定，那么表示用户还没有必要去设置交易密码,那么久隐藏交易密码的线性布局
            if (bankCards.size() != 0) {
                //表示已经绑定用户卡
                mllCommonTradePassword.setVisibility(View.VISIBLE);
            } else {
                //表示用户还没有进行银行卡绑定，那么就将交易密码的线性布局隐藏
                mllCommonTradePassword.setVisibility(View.GONE);
            }


        } else {

        }


        //0本地未设置, 1本地开启 2 本地关闭
        String lockPwd = sharedPreferences.getString(Constants.LOCK_PWD, "");
        if (!TextUtils.isEmpty(lockPwd)) {
            if ("1".equals(sharedPreferences.getString(Constants.GESTURE_STATUS, "0"))) {
                mySwitchState = STATE_SWITCH_OPEN;//更新textview状态为关闭手势密码
                mSwitchPword.setChecked(true);
                mTvGesterPassword.setText("关闭手势密码");
                mllCommonRiskGesturePassword.setVisibility(View.VISIBLE);
            } else if ("2".equals(sharedPreferences.getString(Constants.GESTURE_STATUS, "0"))) {
                //表示是关闭状态
                mySwitchState = STATE_SWITCH_CLOSE;//更新textview状态为开启手势密码
                mSwitchPword.setChecked(false);
                mTvGesterPassword.setText("开启手势密码");
                mllCommonRiskGesturePassword.setVisibility(View.GONE);
            } else {
                mllCommonRiskGesturePassword.setVisibility(View.GONE);//隐藏修改手势密码的线性布局一栏
                mTvGesterPassword.setText("设置手势密码");
                mSwitchPword.setChecked(false);//表示还没有设置手势面，那么就将开关设置为关闭
                mySwitchState = STATE_SWITCH_SETTING;//更新状态为需要去设置手势密码

                sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "0").apply();
            }
        } else {
            mllCommonRiskGesturePassword.setVisibility(View.GONE);//隐藏修改手势密码的线性布局一栏
            mTvGesterPassword.setText("设置手势密码");
            mSwitchPword.setChecked(false);//表示还没有设置手势面，那么就将开关设置为关闭
            mySwitchState = STATE_SWITCH_SETTING;//更新状态为需要去设置手势密码

            sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "0").apply();
        }
    }


    /**
     * 初始化页面元素
     */
    @Override
    protected void initView() {
        super.initView();

        ((TextView) findViewById(R.id.title_text)).setText("我的账户");

        mTvGesterPassword = (TextView) findViewById(R.id.tv_common_gesture_password);
        mSwitchPword = (CheckBox) findViewById(R.id.common_switch_pword);
        mSwitchPword.setClickable(false);
        mGesturePassword = (LinearLayout) findViewById(R.id.ll_common_gesture_password);
        mGesturePassword.setOnClickListener(this);

        //用户名
        mllCommonAccount = (LinearLayout) findViewById(R.id.ll_common_account);
        mllCommonAccount.setOnClickListener(this);
        mTvCommonAccount = (TextView) findViewById(R.id.tv_common_account);
        mIvUserImage = (ImageView) findViewById(R.id.iv_user_image);


        //修改手机号
        mllRevisePhone = (LinearLayout) findViewById(R.id.ll_common_revise_phone);
        mllRevisePhone.setOnClickListener(this);
        mTvRevisePhone = (TextView) findViewById(R.id.tv_common_revise_phone);

        //风险偏好
        mllCommonRisk = (LinearLayout) findViewById(R.id.ll_common_risk);
        mllCommonRisk.setOnClickListener(this);
        mTvCommonRisk = (TextView) findViewById(R.id.tv_common_risk);

        //姓名
        mllcommonName = (LinearLayout) findViewById(R.id.ll_common_name);
        mTvCommonName = (TextView) findViewById(R.id.tv_common_name);

        //身份证号码
        mllCommonCard = (LinearLayout) findViewById(R.id.ll_common_card);
        mTvCommonCard = (TextView) findViewById(R.id.tv_common_card);

        //银行卡
        mllBankCard = (LinearLayout) findViewById(R.id.ll_bank_card);
        mTvCommonBankCard = (TextView) findViewById(R.id.tv_common_bank_card);

        //修改银行预留手机
        mllCommonRiskPhone = (LinearLayout) findViewById(R.id.ll_common_risk_phone);
        mllCommonRiskPhone.setOnClickListener(this);
        mTvCommonRiskPhone = (TextView) findViewById(R.id.tv_common_risk_phone);

        //修改登录密码
        mllLoginPassword = (LinearLayout) findViewById(R.id.ll_common_login_password);
        mllLoginPassword.setOnClickListener(this);

        //修改交易密码
        mllCommonTradePassword = (LinearLayout) findViewById(R.id.ll_common_trade_password);
        mllCommonTradePassword.setOnClickListener(this);


        //修改手势密码
        mllCommonRiskGesturePassword = (LinearLayout) findViewById(R.id.ll_common_risk_gesture_password);
        mllCommonRiskGesturePassword.setOnClickListener(this);

        //退出登录的按钮
        mBtnCommonLoginout = (Button) findViewById(R.id.btn_common_loginout);
        mBtnCommonLoginout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_common_account://用户名
                intent = new Intent(MyAccountActivity.this, ModificationUserActivity.class);
                intent.putExtra("userName", userInfo.getUsername());
                intent.putExtra("userid", userInfo.getId());
                Lg.e("userid........", userInfo.getId());
                startActivityForResult(intent, MODIFICATION);
                break;

            case R.id.ll_common_risk_gesture_password://修改手势密码

                String lockPwdstr = sharedPreferences.getString(Constants.LOCK_PWD, "");
                if (!TextUtils.isEmpty(lockPwdstr)) {//已经保存过手势密码
                    List<LockPwd> lockPwds = new Gson().fromJson(lockPwdstr, new TypeToken<List<LockPwd>>() {
                    }.getType());
                    if (lockPwds != null && lockPwds.size() > 0) {
                        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                        if (userInfo == null) {
                            userInfo = new UserInfo();
                            userInfo.setId(-1);
                        }
                        for (LockPwd lockPwd : lockPwds) {
                            if (lockPwd != null && lockPwd.getUserId() == userInfo.getId()) {
                                intent = new Intent(MyAccountActivity.this, UnlockGesturePasswordActivity.class);
                                intent.putExtra("lockPwd", lockPwd);
                                intent.putExtra("isSetting", true);
                                startActivityForResult(intent, REQUEST_CODE_CHANGE_GUESTURE);
                                break;
                            }
                        }
                    }
                }
                break;

            case R.id.ll_common_gesture_password://设置，关闭，打开手势密码的线性布局
                switch (mySwitchState) {
                    case STATE_SWITCH_SETTING://去设置手势密码
                        intent = new Intent(MyAccountActivity.this, GesturePwdActivity.class);
                        intent.putExtra("setup", true);
                        startActivityForResult(intent, REQUEST_CODE_SET_GUESTURE);
                        //startActivity(intent);
                        break;

                    case STATE_SWITCH_OPEN://表示已经设置过手势密码，可以去关闭手势密码
                        String lockPwdst = sharedPreferences.getString(Constants.LOCK_PWD, "");
                        if (!TextUtils.isEmpty(lockPwdst)) {//已经保存过手势密码
                            List<LockPwd> lockPwds = new Gson().fromJson(lockPwdst, new TypeToken<List<LockPwd>>() {
                            }.getType());
                            if (lockPwds != null && lockPwds.size() > 0) {
                                UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                                if (userInfo == null) {
                                    userInfo = new UserInfo();
                                    userInfo.setId(-1);
                                }
                                for (LockPwd lockPwd : lockPwds) {
                                    if (lockPwd != null && lockPwd.getUserId() == userInfo.getId()) {
                                        intent = new Intent(MyAccountActivity.this, UnlockGesturePasswordActivity.class);
                                        intent.putExtra("lockPwd", lockPwd);
                                        intent.putExtra("isClose", true);
                                        startActivityForResult(intent, 10003);
                                        break;
                                    }
                                }

                            }
                        }
                        break;

                    case STATE_SWITCH_CLOSE://表示已经设置过手势密码，但手势密码是关闭状态,可以去开启手势密码
                        sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "1").apply();
                        //同步一下cookie
                        synCookies(this);
                        mSwitchPword.setChecked(true);
                        mTvGesterPassword.setText("关闭手势密码");
                        mySwitchState = STATE_SWITCH_OPEN;
                        //当前手势密码是开启状态，那么就显示修改手势密码一栏的线性布局
                        mllCommonRiskGesturePassword.setVisibility(View.VISIBLE);

                        break;
                }
                break;

            case R.id.ll_common_login_password://修改登录密码
                intent = new Intent(MyAccountActivity.this, ModificationLoginPwdActivity.class);
                intent.putExtra("userId", userInfo.getId());
                startActivity(intent);
                break;

            case R.id.btn_common_loginout://退出登录的按钮

                //退出嘉石榴具体操作
                doLoginOut(view);

                break;
            case R.id.ll_common_trade_password://修改交易密码按钮
                intent = new Intent(MyAccountActivity.this, ModifyDealPwdActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_common_revise_phone://修改手机号
                intent = new Intent(MyAccountActivity.this, ModifyPhoneActivity.class);
                intent.putExtra("modify_phone", userInfo.getPhone());
                intent.putExtra("userId", userInfo.getId());
                startActivity(intent);
                break;

            case R.id.ll_common_risk://风险偏好
//                intent = new Intent(MyAccountActivity.this, WebActivity.class);
//                intent.putExtra("linkUrl", "https://app.jia16.com/#!questionnaire&riskCapacity=MODERATE");
//                intent.putExtra("autoTitle", true);
//                intent.putExtra("isTitle", true);//不需要标题栏
//                startActivity(intent);

                intent = new Intent(MyAccountActivity.this, RiskManagerActivity.class);

                UserInfo.RiskCapacityBean riskCapacity = userInfo.getRiskCapacity();
                if (riskCapacity != null) {//表示用户已经进行风险评测，那么就把属性传递过去
                    String riskCapacityLevelText = riskCapacity.getRiskCapacityLevelText();
                    String riskCapacityLevelCode = riskCapacity.getRiskCapacityLevelCode();

                    intent.putExtra("risktext", riskCapacityLevelText);
                    intent.putExtra("riskcode", riskCapacityLevelCode);

                }
                startActivityForResult(intent, RISKMANAGER);

                break;

            case R.id.ll_bank_card://银行卡
                if (certification != null) {
                    //已经绑定银行卡，点击那么就什么也不做
                } else {
                    //表示还没有绑定银行卡，点击那么就要去绑定银行卡
//                    intent=new Intent(MyAccountActivity.this,);
//                    intent.putExtra("");
                }
                break;

            default:
                break;
        }
        super.onClick(view);
    }

    //退出嘉石榴具体操作
    private void doLoginOut(View view) {
        String url = "/ums/users/logout";
        url = UrlHelper.getUrl(url);

        Lg.e("LoginOut_url", url);

        if (checkClick(view.getId())) {

            JSONObject jsonObject = new JSONObject();

            if (Util.isNetworkAvailable(this)) {//检查是否有网
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                                int userId = 0;
                                if (userInfo != null) {
                                    userId = userInfo.getId();
                                }
                                //0 没提醒过 1,用户点了设置 2,用户暂不设置,3用户提示永不提醒,4,设置过了
                                //重新设置用户手势密码状态
                                String status = getCurrentRemindStatus(userId);
                                if (!"3".equals(status)) {
                                    setCurrentRemindStatus(userId, "0");
                                }
                                //清除用户
                                BaseApplication.getInstance().setUserInfo(null);
                                //清除登录状态（cookie信息）
                                removeCookie();

                                ToastUtil.getInstant().show(MyAccountActivity.this, "退出登录成功");

                                //退出登录成功后，那么久跳转到登录页面
                                Intent intent = new Intent(MyAccountActivity.this, LoginActivity.class);
                                //startActivityForResult(intent,LoginRequestCode);
                                startActivity(intent);
                                finish();


                                //清除我要投资界面保存的RadioButton的状态，设置为默认状态
                                BaseApplication.getInstance().isEarn=true;

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
                                    AlertUtil.showOneBtnDialog(MyAccountActivity.this, message, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });
                                }

                            } else {
                                AlertUtil.showOneBtnDialog(MyAccountActivity.this, "退出登录失败", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }) {
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

            } else {
                AlertUtil.showOneBtnDialog(MyAccountActivity.this, "网络或服务器异常", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            //修改用户名
            if (requestCode == MODIFICATION) {
                String newUserName = data.getStringExtra("newUserName");
                Lg.e("newUserName", newUserName);
                mTvCommonAccount.setText(newUserName);//修改完成，重新设置用户名

                //设置是否已经修改过用户名的状态
                userInfo.setIsChangeUserName(false);

                //设置完成后，那么就隐藏箭头的图片，同时设置用户名的线性布局不响应点击事件
                mIvUserImage.setVisibility(View.GONE);
                mllCommonAccount.setClickable(false);

            } else if (requestCode == REQUEST_CODE_SET_GUESTURE) {//去设置手势密码
                mTvGesterPassword.setText("关闭手势密码");
                mySwitchState = STATE_SWITCH_OPEN;
                //设置完成后，打开开关
                mSwitchPword.setChecked(true);
                //当前手势密码是开启状态，那么就显示修改手势密码一栏的线性布局
                mllCommonRiskGesturePassword.setVisibility(View.VISIBLE);

                sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "1").apply();
            } else if (requestCode == 10003) {
                mTvGesterPassword.setText("开启手势密码");
                mSwitchPword.setChecked(false);
                mySwitchState = STATE_SWITCH_CLOSE;
                //当前手势密码是关闭状态，那么就隐藏修改手势密码一栏的线性布局
                mllCommonRiskGesturePassword.setVisibility(View.GONE);

                sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "2").apply();
            } else if (requestCode == RISKMANAGER) {//设置风险评测
                String newRisk = data.getStringExtra("newRisk");
                String newCode = data.getStringExtra("newCode");
                Lg.e("newRisk", newRisk);
                if(newRisk!=null&&newCode!=null){
                    if(userInfo!=null){
                        UserInfo.RiskCapacityBean riskCapacity = userInfo.getRiskCapacity();
                        if(riskCapacity!=null){
                            riskCapacity.setRiskCapacityLevelText(newRisk);
                            riskCapacity.setRiskCapacityLevelCode(newCode);
                        }
                    }
                }
                mTvCommonRisk.setText(newRisk);



            }
        }
    }



    @Override
    protected void onResume() {
        //修改完手机号发过来的广播，更新用户界面手机号码显示
        registerrecver();

        //点击重新评测按钮时发送过来的广播
        registerreceiver2();

        //获取用户信息
        getCurrentUser();
        super.onResume();
    }


    private void registerrecver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("modify.new.phone");
        intentFilter.addCategory(Intent.ACTION_DEFAULT);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //设置完成手机号码后传递过来的手机号码
                        String modify_phone = intent.getStringExtra("modify_phone");
                        if (!TextUtils.isEmpty(modify_phone)) {
                            //重新设置电话号码到用户bean中
                            userInfo.setPhone(modify_phone);

                            String phone = userInfo.getPhone();

                            String startSubstring = phone.substring(0, 3);
                            String endSubstring = phone.substring(7);
                            String newPhone = startSubstring + "****" + endSubstring;
                            mTvRevisePhone.setText(newPhone);
                        }
                    }
                }, 0);
            }
        };

        registerReceiver(receiver, intentFilter);
    }

    //点击重新评测按钮时发送过来的广播
    public void registerreceiver2(){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("refresh_my_user_risk");
        intentFilter.addCategory(Intent.ACTION_DEFAULT);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String newRisk = intent.getStringExtra("newRisk");
                        String newCode = intent.getStringExtra("newCode");
                        if(newCode!=null&&newRisk!=null){
                            if(userInfo!=null){
                                UserInfo.RiskCapacityBean riskCapacity = userInfo.getRiskCapacity();
                                if(riskCapacity!=null){
                                    riskCapacity.setRiskCapacityLevelText(newRisk);
                                    riskCapacity.setRiskCapacityLevelCode(newCode);

                                    //重新设置我的账户界面的风险评测的类型
                                    mTvCommonRisk.setText(newRisk);
                                }
                            }
                        }
                    }
                },300);
            }
        };
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(receiver);

        super.onDestroy();
    }
}
