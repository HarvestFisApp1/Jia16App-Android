package com.jia16.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.LockPwd;
import com.jia16.bean.UserInfo;
import com.jia16.util.AlertUtil;
import com.jia16.util.Constants;
import com.jia16.util.EncrypUtil;
import com.jia16.util.Lg;
import com.jia16.util.LockPatternUtils;
import com.jia16.util.ToastUtil;
import com.jia16.view.LockPatternView;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码设置
 *
 * @author jiaohongyun
 */
public class GesturePwdActivity extends BaseActivity {
    private static final String LOG_TAG = GesturePwdActivity.class.getCanonicalName();

    private static final int ID_EMPTY_MESSAGE = -1;

    private static final String KEY_UI_STAGE = "uiStage";

    private String cookie;//如果传入这个参数 则说明是从登录过来

    private static final String KEY_PATTERN_CHOICE = "chosenPattern";
    /**
     * The patten used during the help screen to show how to draw a pattern.
     */
    private final List<LockPatternView.Cell> mAnimatePattern = new ArrayList<LockPatternView.Cell>();
    //  private Button mFooterRightButton;
    //  private Button mFooterLeftButton;
    protected TextView mHeaderText;

    private TextView mResetPwd;//重新设置解锁图案

    protected List<LockPatternView.Cell> mChosenPattern = null;
    private LockPatternView mLockPatternView;
    private Toast mToast;

    //  private View mPreviewViews[][] = new View[3][3];
    private Stage mUiStage = Stage.Introduction;
//    /**
//     * 从哪个页面过来的
//     */
//    private String from = null;
    /**
     * 登录名
     */
    private String account = null;
    /**
     * 登录密码
     */
    private boolean removePwd = false;

    private String loginPwd = null;
    private boolean isRegister;
    private boolean isSuccess = false;
    private Runnable mClearPatternRunnable = new Runnable() {
        @Override
        public void run() {
            mLockPatternView.clearPattern();
        }
    };
    //    private DbUtils db;
    private LockPwd lockPwd;
    private List<LockPwd> lockPwds = new ArrayList<>();
    private UserInfo userInfo;

    private boolean isSetting = false;//修改手势密码

    private ImageView mBackBtn;

    private boolean setup = false;//是从个人中心的设置过来

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
            Lg.e("第一次绘制密码成功..............");
            if (pattern == null)
                return;
            // Log.i("way", "result = " + pattern.toString());
            if (mUiStage == Stage.NeedToConfirm || mUiStage == Stage.ConfirmWrong) {
                if (mChosenPattern == null)
                    throw new IllegalStateException("null chosen pattern in stage 'need to confirm");
                if (mChosenPattern.equals(pattern)) {
                    Lg.e("再次绘制密码成功..............");
                    updateStage(Stage.ChoiceConfirmed);
                    //modea
                    saveChosenPatternAndFinish();
                } else {
                    updateStage(Stage.ConfirmWrong);
                    Lg.e("再次绘制密码失败，与上次不符合..............");
                    //mode
                    mChosenPattern = null;
                    mLockPatternView.clearPattern();
                    updateStage(Stage.Introduction);
                    ToastUtil.getInstant().show(GesturePwdActivity.this, getString(R.string.tow_pwd_error));
                }
            } else if (mUiStage == Stage.Introduction || mUiStage == Stage.ChoiceTooShort) {
                if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                    updateStage(Stage.ChoiceTooShort);
                } else {
                    mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                    updateStage(Stage.FirstChoiceValid);
                    Lg.e("第一次绘制密码成功..............");
                    updateStage(Stage.NeedToConfirm);
                }
            } else {
                throw new IllegalStateException("Unexpected stage " + mUiStage + " when " + "entering the pattern.");
            }
        }

        @Override
        public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

        }

        private void patternInProgress() {
            mHeaderText.setText(R.string.lockpattern_recording_inprogress);
        }
    };

    private boolean isOpen;//是否为从设置里面设置手势密码


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lg.i(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_pwd_shoushi_setting_new);
        // 初始化演示动画
        mAnimatePattern.add(LockPatternView.Cell.of(0, 0));
        mAnimatePattern.add(LockPatternView.Cell.of(0, 1));
        mAnimatePattern.add(LockPatternView.Cell.of(1, 1));
        mAnimatePattern.add(LockPatternView.Cell.of(2, 1));
        mAnimatePattern.add(LockPatternView.Cell.of(2, 2));

        mLockPatternView = (LockPatternView) this.findViewById(R.id.gesturepwd_create_lockview);
        mHeaderText = (TextView) findViewById(R.id.gesturepwd_create_text);
        mBackBtn = (ImageView) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mResetPwd = (TextView) findViewById(R.id.btn_reset_pwd);
        mResetPwd.setOnClickListener(this);
        mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
        mLockPatternView.setTactileFeedbackEnabled(true);

        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
        updateStage(Stage.Introduction);
//        from = getIntent().getStringExtra("from");
        account = getIntent().getStringExtra("account");
        loginPwd = getIntent().getStringExtra("pwd");
        isRegister = getIntent().getBooleanExtra("isRegister", false);
        cookie = getIntent().getStringExtra("cookie");              //LoginActivity传递过来的cookie
        removePwd = getIntent().getBooleanExtra("removePwd", false);//LoginActivity传递过来的是否清除手势密码
        isSetting = getIntent().getBooleanExtra("isSetting", false);//UnlockGesturePasswordActivity传递过来是否是重置密码
        setup = getIntent().getBooleanExtra("setup", false);//WebViewActivity传递过来，是否是设置手势密码
        isOpen = getIntent().getBooleanExtra("isOpen", false);
        if (cookie != null) {//从登录过来
            mBackBtn.setVisibility(View.GONE);
        }
        if (isOpen || removePwd) {
            mBackBtn.setVisibility(View.VISIBLE);
        }
        if (isSetting) {//修改
            mBackBtn.setVisibility(View.VISIBLE);
            mHeaderText.setText("绘制新解锁图案");
        }
        ((TextView) findViewById(R.id.title_text)).setText(getString(R.string.title_setting_gesture_password));
        //TODO HUANGJUN
//        super.initView();
//        if (from != null && (from.equals("login"))) {
//            //从登录页面进来
//            findViewById(R.id.btn_back).setVisibility(View.GONE);
//            preSave();
//        } else if (from != null && (from.equals("splash"))) {
//            //从闪屏页面过来
//            lockPwd = getIntent().getParcelableExtra("lockPwd");
//        }
//        boolean isResetGesturePwd = getIntent().getBooleanExtra("isResetGesturePwd", false);
//        if (isResetGesturePwd) {
//            ((TextView) findViewById(R.id.title_text)).setText(getString(R.string.title_resetting_gesture_password));
//        } else {
//
//        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_reset_pwd:
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                updateStage(Stage.Introduction);
                mResetPwd.setVisibility(View.INVISIBLE);
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    /**
     * 临时保存到数据库
     */
    private void preSave() {
        userInfo = BaseApplication.getInstance().getUserInfo();
        String str = sharedPreferences.getString(Constants.LOCK_PWD, "");
        lockPwds = new Gson().fromJson(str, new TypeToken<List<LockPwd>>() {
        }.getType());

        if (lockPwds != null && lockPwds.size() > 0) {
            int size = lockPwds.size();
            for (int i = 0; i < size; i++) {
                LockPwd currentPwd = lockPwds.get(i);
                if (currentPwd != null && currentPwd.getUserId() == userInfo.getId()) {
                    lockPwd = currentPwd;
                }
            }
        } else {
            lockPwds = new ArrayList<>();
        }

//        lockPwd = myDb.findFirst(Selector.from(LockPwd.class).where("userId", "=", userInfo.getId()));
        if (lockPwd == null) {
            lockPwd = new LockPwd();
        }
        lockPwd.setPwd("");
        lockPwd.setUserId(userInfo.getId());
        if (account != null) {
            lockPwd.setAccount(account);
        }
        if (loginPwd != null) {
            lockPwd.setLoginPwd(EncrypUtil.encrypt(loginPwd, Constants.ENCRYP_SEND));
        }

        if (lockPwds != null && lockPwds.size() > 0) {
            int size = lockPwds.size();
            for (int i = 0; i < size; i++) {
                LockPwd currentPwd = lockPwds.get(i);
                if (currentPwd != null && currentPwd.getUserId() == userInfo.getId()) {
                    lockPwds.set(i, lockPwd);
                }
            }
        } else {
            if (removePwd) {
                lockPwds.clear();
            }
            lockPwds.add(lockPwd);
        }
        sharedPreferences.edit().putString(Constants.LOCK_PWD, new Gson().toJson(lockPwds)).apply();


//        ThreadsPool.executeOnExecutor(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //将手势密码保存到数据库
//                    myDb.createTableIfNotExist(LockPwd.class);
//                    lockPwd = myDb.findFirst(Selector.from(LockPwd.class).where("userId", "=", userInfo.getId()));
//                    if (lockPwd == null) {
//                        lockPwd = new LockPwd();
//                    }
//                    lockPwd.setPwd("");
//                    lockPwd.setUserId(userInfo.getId());
//                    if (account != null) {
//                        lockPwd.setAccount(account);
//                    }
//                    if (loginPwd != null) {
//                        lockPwd.setLoginPwd(EncrypUtil.encrypt(loginPwd, Constants.ENCRYP_SEND));
//                    }
//                    myDb.saveOrUpdate(lockPwd);
//                } catch (DbException e) {
//                    e.printStackTrace();
//                } finally {
//                    myDb.close();
//                }
//            }
//        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_UI_STAGE, mUiStage.ordinal());
        if (mChosenPattern != null) {
            outState.putString(KEY_PATTERN_CHOICE, LockPatternUtils.patternToString(mChosenPattern));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (cookie != null) {
                ToastUtil.getInstant().show(this, "您还没有设置手势密码，请设置！");
            } else {
                this.finish();
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void updateStage(Stage stage) {
        mUiStage = stage;
        if (stage == Stage.ChoiceTooShort) {
            mHeaderText.setText(getResources().getString(stage.headerMessage, LockPatternUtils.MIN_LOCK_PATTERN_SIZE));
        } else {
            if (isSetting) {
                if ("再次绘制解锁图案".equals(stage.headerMessage)) {
                    mHeaderText.setText("再次绘制新解锁图案");
                } else {
                    mHeaderText.setText(stage.headerMessage);
                }
            } else {
                mHeaderText.setText(stage.headerMessage);
            }


        }

        // same for whether the patten is enabled
        if (stage.patternEnabled) {
            mLockPatternView.enableInput();
        } else {
            mLockPatternView.disableInput();
        }

        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);

        switch (mUiStage) {
            case Introduction:
                mLockPatternView.clearPattern();
                mResetPwd.setVisibility(View.INVISIBLE);
                break;
            case HelpScreen:
                mLockPatternView.setPattern(LockPatternView.DisplayMode.Animate, mAnimatePattern);
                break;
            case ChoiceTooShort:
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case FirstChoiceValid:
                break;
            case NeedToConfirm:
                mLockPatternView.clearPattern();
                mResetPwd.setVisibility(View.VISIBLE);
                //          updatePreviewViews();
                break;
            case ConfirmWrong:
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case ChoiceConfirmed:
                break;
            case VALIDOLDPWD:
                break;
            default:
                break;
        }

    }

    // clear the wrong pattern unless they have started a new one
    // already
    private void postClearPatternRunnable() {
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
    }

    private void saveChosenPatternAndFinish() {
        isSuccess = true;
        //把手势密码转换为string类型
        final String pwd = LockPatternUtils.patternToString(mChosenPattern);
        userInfo = BaseApplication.getInstance().getUserInfo();


        int userId = -1;
        if (userInfo != null) {
            userId = userInfo.getId();
            Lg.e("Gesture~userId==============", userId);
        }
//        if (from != null && (from.equals("splash"))) {
//            userId = lockPwd.getUserId();
//        }
        String str = sharedPreferences.getString(Constants.LOCK_PWD, "");
        lockPwds = new Gson().fromJson(str, new TypeToken<List<LockPwd>>() {
        }.getType());

        if (lockPwds != null && lockPwds.size() > 0) {
            int size = lockPwds.size();
            for (int i = 0; i < size; i++) {
                LockPwd currentPwd = lockPwds.get(i);
                if (currentPwd != null && currentPwd.getUserId() == userId) {
                    lockPwd = currentPwd;
                }
            }
        } else {
            lockPwds = new ArrayList<>();
        }
//                lockPwd = db.findFirst(Selector.from(LockPwd.class).where("userId", "=", userId));
        if (lockPwd == null) {
            lockPwd = new LockPwd();
        }
        lockPwd.setPwd(EncrypUtil.encrypt(pwd, Constants.ENCRYP_SEND));
        lockPwd.setUserId(userId);
        if (account != null) {
            lockPwd.setAccount(account);
        }
        if (loginPwd != null) {
            lockPwd.setLoginPwd(EncrypUtil.encrypt(loginPwd, Constants.ENCRYP_SEND));
        }
        if (lockPwds != null && lockPwds.size() > 0) {
            boolean matched = false;
            int size = lockPwds.size();
            for (int i = 0; i < size; i++) {
                LockPwd currentPwd = lockPwds.get(i);
                if (currentPwd != null && currentPwd.getUserId() == userId) {
                    lockPwds.set(i, lockPwd);
                    matched = true;
                }
            }
            if (!matched) {
                lockPwds.add(lockPwd);
            }
        } else {
            if (removePwd) {
                lockPwds.clear();
            }
            lockPwds.add(lockPwd);
        }
        sharedPreferences.edit().putString(Constants.LOCK_PWD, new Gson().toJson(lockPwds)).apply();
//        SpManager.getInstance(this).put(Constants.LOCK_PWD, new Gson().toJson(lockPwds));
//        db.saveOrUpdate(lockPwd);

        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        sp.edit().putBoolean("hasLocked", true).commit();
        BaseApplication.getInstance().getLockedUserId(this.sharedPreferences);

        //设置成功 保存状态
        setCurrentRemindStatus(userId, "4");
//        sharedPreferences.edit().putString(Constants.LOCK_PWD_REMIND, "4").apply();
        sharedPreferences.edit().putString(Constants.GESTURE_STATUS, "1").apply();
        AlertUtil.showOneBtnDialog(this, "手势密码设置成功,请牢记!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(cookie)) {//登录过来
                    Intent intent = new Intent();
                    intent.putExtra("cookie", cookie);
                    setResult(RESULT_OK, intent);
                } else {
                    if (setup) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                    }
                }
//                else if (removePwd) {
//                    Intent intent = new Intent(GesturePwdActivity.this, WebViewActivity.class);
//                    startActivity(intent);
//                }
                finish();
            }
        });
    }

    /**
     * The states of the left footer button.
     */
    enum LeftButtonMode {
        Cancel(android.R.string.cancel, true), CancelDisabled(android.R.string.cancel, false), Retry(
                R.string.lockpattern_retry_button_text,
                true), RetryDisabled(R.string.lockpattern_retry_button_text, false), Gone(ID_EMPTY_MESSAGE, false);

        final int text;
        final boolean enabled;

        /**
         * @param text    The displayed text for this mode.
         * @param enabled Whether the button should be enabled.
         */
        LeftButtonMode(int text, boolean enabled) {
            this.text = text;
            this.enabled = enabled;
        }
    }

    /**
     * The states of the right button.
     */
    enum RightButtonMode {
        Continue(R.string.lockpattern_continue_button_text, true), ContinueDisabled(R.string.lockpattern_continue_button_text,
                false), Confirm(R.string.lockpattern_confirm_button_text,
                true), ConfirmDisabled(R.string.lockpattern_confirm_button_text, false), Ok(android.R.string.ok, true);

        final int text;
        final boolean enabled;

        /**
         * @param text    The displayed text for this mode.
         * @param enabled Whether the button should be enabled.
         */
        RightButtonMode(int text, boolean enabled) {
            this.text = text;
            this.enabled = enabled;
        }
    }

    /**
     * Keep track internally of where the user is in choosing a pattern.
     */
    protected enum Stage {

        Introduction(R.string.lockpattern_recording_intro_header, LeftButtonMode.Cancel, RightButtonMode.ContinueDisabled,
                ID_EMPTY_MESSAGE, true), HelpScreen(R.string.lockpattern_settings_help_how_to_record, LeftButtonMode.Gone,
                RightButtonMode.Ok, ID_EMPTY_MESSAGE, false), ChoiceTooShort(R.string.lockpattern_recording_incorrect_too_short,
                LeftButtonMode.Retry, RightButtonMode.ContinueDisabled, ID_EMPTY_MESSAGE,
                true), FirstChoiceValid(R.string.lockpattern_pattern_entered_header, LeftButtonMode.Retry,
                RightButtonMode.Continue, ID_EMPTY_MESSAGE, false), NeedToConfirm(R.string.lockpattern_need_to_confirm,
                LeftButtonMode.Cancel, RightButtonMode.ConfirmDisabled, ID_EMPTY_MESSAGE, true), ConfirmWrong(
                R.string.lockpattern_need_to_unlock_wrong, LeftButtonMode.Cancel, RightButtonMode.ConfirmDisabled,
                ID_EMPTY_MESSAGE, true), ChoiceConfirmed(R.string.lockpattern_pattern_confirmed_header,
                LeftButtonMode.Cancel, RightButtonMode.Confirm, ID_EMPTY_MESSAGE,
                false), VALIDOLDPWD(R.string.lockpattern_pattern_confirmed_header, LeftButtonMode.Cancel,
                RightButtonMode.Confirm, -1, true);

        final int headerMessage;
        final LeftButtonMode leftMode;
        final RightButtonMode rightMode;
        final int footerMessage;
        final boolean patternEnabled;

        /**
         * @param headerMessage  The message displayed at the top.
         * @param leftMode       The mode of the left button.
         * @param rightMode      The mode of the right button.
         * @param footerMessage  The footer message.
         * @param patternEnabled Whether the pattern widget is enabled.
         */
        Stage(int headerMessage, LeftButtonMode leftMode, RightButtonMode rightMode, int footerMessage, boolean patternEnabled) {
            this.headerMessage = headerMessage;
            this.leftMode = leftMode;
            this.rightMode = rightMode;
            this.footerMessage = footerMessage;
            this.patternEnabled = patternEnabled;
        }
    }
}
