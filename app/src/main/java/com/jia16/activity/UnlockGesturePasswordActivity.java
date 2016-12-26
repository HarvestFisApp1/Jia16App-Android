/*
 * 文 件 名:  UnlockGesturePasswordActivity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年1月4日
 */
package com.jia16.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.LockPwd;
import com.jia16.bean.UserInfo;
import com.jia16.util.AlertUtil;
import com.jia16.util.AppManager;
import com.jia16.util.Constants;
import com.jia16.util.EncrypUtil;
import com.jia16.util.Lg;
import com.jia16.util.LockPatternUtils;
import com.jia16.view.LockPatternView;

import java.util.List;

/**
 * 解锁手势密码
 */
public class UnlockGesturePasswordActivity extends BaseActivity implements OnClickListener {
    private LockPatternView mLockPatternView;

    private int mFailedPatternAttemptsSinceLastTimeout = 0;

    private CountDownTimer mCountdownTimer = null;

    private Handler mHandler = new Handler();

    private ImageView mBackBtn;

    private TextView mHeadTextView;

    private TextView mTipTextView;//提示文字

    private Button forgetView;

    private Button otherLogin;

    //    private Animation mShakeAnim;

    private Toast mToast;

    /**
     * 解锁密码
     */
    private LockPwd lockPwd;

    private String lockPwdStr = "";

    /**
     * 是否需要重新设置手势密码
     */
    private boolean isSetting;

    private boolean isClose = false;//是否要关闭手势密码

    private boolean isTemp = false;

    /**
     * 剩余重新输入密码时间
     */
    private long remainTime = 0;

    private boolean isCancleable;
    private Runnable mClearPatternRunnable = new Runnable() {
        @Override
        public void run() {
            mLockPatternView.clearPattern();
        }
    };
    /**
     * 页面是否在前台
     */
    private boolean visible = true;
    Runnable attemptLockout = new Runnable() {

        @Override
        public void run() {
            mLockPatternView.clearPattern();
            mLockPatternView.setEnabled(false);
            //            AppLog.d("create count");
            if (mCountdownTimer != null) {
                mCountdownTimer.cancel();
            }
            mCountdownTimer = new CountDownTimer(LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    remainTime = millisUntilFinished;
                    int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
                    if (secondsRemaining > 0) {
//                        mTipTextView.setText(secondsRemaining + " 秒后重试");
                    } else {
//                        mTipTextView.setText("请绘制手势密码");
                        //						mHeadTextView.setTextColor(Color.WHITE);
                    }

                }

                @Override
                public void onFinish() {
                    mLockPatternView.setEnabled(true);
                    mFailedPatternAttemptsSinceLastTimeout = 0;

                    if (visible) {
                        //页面在前台时处理
                    } else {
                        //页面不在前台，关闭应用
                        finish();
                    }
                }
            }.start();
        }
    };
    protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
            patternInProgress();
        }

        @Override
        public void onPatternCleared() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
        }

        @Override
        public void onPatternDetected(List<LockPatternView.Cell> pattern) {
            if (pattern == null) {
                return;
            }
            String pwd = LockPatternUtils.patternToString(pattern);
            if (pwd.equals(lockPwdStr)) {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                sharedPreferences.edit().putInt("retry", 5).apply();
                if (isSetting) {//如果是重置
                    showToast("验证成功,请重新设置");
                    Intent intent = new Intent(UnlockGesturePasswordActivity.this, GesturePwdActivity.class);
                    intent.putExtra("isSetting", true);
                    startActivity(intent);
                    finish();
                } else if (isClose) {
                    sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "2").apply();
                    setResult(RESULT_OK);
                    finish();
                } else {

                    showToast("解锁成功");
                    isTemp = getIntent().getBooleanExtra("isTemp", false);// UnlockGesturePasswordActivity 是否是临时弹出，如果应用从后台进入前台，弹出此页面，值为true
                    // 跳转到主界面
                    Lg.e("isTemp", isTemp + "");
                    if (!isTemp) {
                        //TODO HUANGJUN
                        Intent intent = new Intent(UnlockGesturePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //走到这里，表示应用是从后台切换到前台（即点击了home键，应用变为后台运行）
                    }
                    //TODO HUANGJUN
//                if (BaseApplication.getInstance().getUserInfo() == null) {
//                    BaseApplication.getInstance().getAccountinfo();
//                }
                    finish();
                }
            } else {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                    mFailedPatternAttemptsSinceLastTimeout++;
                    retry--;
                    sharedPreferences.edit().putInt("retry", retry).apply();
                    if (retry >= 0) {
                        if (retry == 0) {
                            sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "0").apply();
                            UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                            if (userInfo!=null) {
                                setCurrentRemindStatus(userInfo.getId(), "0");
                            }
//                            sharedPreferences.edit().putString(Constants.LOCK_PWD_REMIND, "0").apply();
                            //清除登录状态
                            removeCookie();
                            //清除当前手势密码
                            clearCurrentGesturePwd();

                            AlertUtil.showOneBtnDialog(UnlockGesturePasswordActivity.this, "手势密码已失效，请重新登录", "重新登录", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //忘记密码，进入登录页面
                                    Intent intent = new Intent(UnlockGesturePasswordActivity.this, LoginActivity.class);
                                    intent.putExtra("removePwd", true);
                                    intent.putExtra("isSetting", isSetting);
                                    startActivityForResult(intent, 100);
                                    if (!isSetting) {
                                        finish();
                                    }
                                }
                            });
                            mLockPatternView.setEnabled(false);
                        }
                        mTipTextView.setText("手势密码错误，还可以再输入" + retry + "次");
                    }

                } else {
                    mTipTextView.setText("至少连接4个点，请重新绘制");
                }

                if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
                    mHandler.postDelayed(attemptLockout, 2000);
                } else {
                    mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
                }
            }
        }

        @Override
        public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

        }

        private void patternInProgress() {
        }
    };

    private int retry;


    private void showToast(CharSequence message) {
        if (null == mToast) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isClose || isSetting) {
                finish();
            } else {
                if (remainTime > 0) {
                    //计时器倒计时中，回到桌面，不关闭应用
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addCategory(Intent.CATEGORY_HOME);
                    startActivity(i);
                } else {
                    //关闭应用
                    if (isCancleable) {
                        finish();
                        AppManager.getAppManager().finishActivity(UnlockGesturePasswordActivity.class);
                    } else {
                        Intent i = new Intent(Intent.ACTION_MAIN);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addCategory(Intent.CATEGORY_HOME);
                        startActivity(i);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesturepassword_unlock);
        retry = sharedPreferences.getInt("retry", LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT);
        mLockPatternView = (LockPatternView) this.findViewById(R.id.gesturepwd_unlock_lockview);
        mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
        mLockPatternView.setTactileFeedbackEnabled(true);
        mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
        mBackBtn = (ImageView) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        String displayStr = "";
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userInfo.getUsername())) {
                String userName = userInfo.getUsername();
                Lg.e("userName", userName);
                int length = userName.length();
                if (length > 2) {
                    String xx = "";
                    String first = userName.substring(0, 1);
                    String last = userName.substring(length - 1, length);
                    for (int i = 0; i < length - 2; i++) {
                        xx += "*";
                    }
                    displayStr = first + xx + last;
                } else {
                    displayStr = userName;
                }
            } else {
                String phone = userInfo.getPhone();
                Lg.e("phone", phone);
                if (phone != null && phone.length() == 11) {
                    int length = phone.length();
                    String first = phone.substring(0, 3);

                    String last = phone.substring(length - 4, length);
                    displayStr = first + "****" + last;
                } else {
                    displayStr = phone;
                }
            }
        }
        mHeadTextView.setText(displayStr);
        mTipTextView = (TextView) findViewById(R.id.gesturepwd_unlock_failtip);
        //        mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
        otherLogin = (Button) findViewById(R.id.gesturepwd_unlock_other_user);
        forgetView = (Button) findViewById(R.id.gesturepwd_unlock_forget);
        forgetView.setClickable(true);
        otherLogin.setClickable(true);
        forgetView.setOnClickListener(this);
        otherLogin.setOnClickListener(this);

        lockPwd = (LockPwd) getIntent().getParcelableExtra("lockPwd");
        if (null != lockPwd) {
            lockPwdStr = EncrypUtil.decrypt(Constants.ENCRYP_SEND, lockPwd.getPwd());
        }
        isSetting = getIntent().getBooleanExtra("isSetting", false);
        isClose = getIntent().getBooleanExtra("isClose", false);
        findViewById(R.id.btn_back).setVisibility(View.GONE);
        if (isSetting) {
            ((TextView) findViewById(R.id.title_text)).setText("修改手势密码");
            mHeadTextView.setText("绘制原解锁图案");
            otherLogin.setVisibility(View.INVISIBLE);
            mBackBtn.setVisibility(View.VISIBLE);
        } else if (isClose) {
            ((TextView) findViewById(R.id.title_text)).setText("关闭手势密码");
            mHeadTextView.setText("绘制解锁图案");
            otherLogin.setVisibility(View.INVISIBLE);
            mBackBtn.setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.title_text)).setText(getString(R.string.title_unlock_gesture_password));
        }
        isCancleable = getIntent().getBooleanExtra("isCancleable", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCountdownTimer != null) {
            mCountdownTimer.onTick(0);
        }
    }

    /**
     * {@inheritDoc}
     */

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
        }
        AppManager.getAppManager().finishActivity(UnlockGesturePasswordActivity.class);
    }

    @Override
    public void onClick(View arg0) {
        if (R.id.btn_back == arg0.getId()) {
            if (isClose) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
            }
            finish();
        } else if (R.id.gesturepwd_unlock_forget == arg0.getId()) {
            AlertUtil.showTwoBtnDialog(this, "忘记手势密码需要重新登录", new AlertUtil.Config("取消", "重新登录"), new AlertUtil.DialogListener() {
                @Override
                public void onLeftClick(AlertDialog dlg) {
                    dlg.cancel();
                }

                @Override
                public void onRightClick(AlertDialog dlg) {
                    //忘记密码，进入登录页面
                    Intent intent = new Intent(UnlockGesturePasswordActivity.this, LoginActivity.class);
                    intent.putExtra("removePwd", true);
                    intent.putExtra("isSetting", isSetting);
                    clearCurrentGesturePwd();
                    removeCookie();
                    startActivityForResult(intent, 100);
                    if (!isSetting) {
                        finish();
                    }
                    dlg.cancel();
                }

                @Override
                public void onTopClick(AlertDialog dlg) {

                }

                @Override
                public void onCenterClick(AlertDialog dlg) {

                }

                @Override
                public void onBottomClick(AlertDialog dlg) {

                }
            });
        } else if (R.id.gesturepwd_unlock_other_user == arg0.getId()) {
            //使用其它号码，进入登录页面
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("changeUser", true);
            startActivityForResult(intent, 100);
            intent.putExtra("isSetting", isSetting);
            if (!isSetting) {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getStringExtra("targetUrl") != null) {
                if (isSetting) {
                    Intent intent = new Intent();
                    intent.putExtra("targetUrl", data.getStringExtra("targetUrl"));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("targetUrl", data.getStringExtra("targetUrl"));
                    startActivity(intent);
                    finish();
                }
            } else {
                if (data != null && data.getStringExtra("cookie") != null) {
                    Intent intent = getIntent();
                    intent.putExtra("cookie", data.getStringExtra("cookie"));
                    intent.putExtra("targetUrl", Constants.HOME_PAGE);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                //TODO HUANGJUN
//            MainActivity.index = 0;
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        visible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        visible = false;
    }
}
