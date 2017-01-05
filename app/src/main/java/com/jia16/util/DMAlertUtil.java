package com.jia16.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.view.ShowPwdEditText;

/**
 * 弹出框工具
 *
 * @author jiaohongyun
 * @date 2015年6月8日
 */
public class DMAlertUtil {

    /**
     * 显示只有一个按钮的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showOneBtnDialog(Activity context, String content) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE); //隐藏取消button
        window.findViewById(R.id.alert_space).setVisibility(View.GONE); //隐藏button间的分割线
        View btnOk = window.findViewById(R.id.alert_ok);
        btnOk.setBackgroundResource(R.drawable.selector_dialog_one_button); //改变按钮的背景图
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

    /**
     * 显示只有一个按钮的提示框
     *
     * @param context
     * @param content
     * @param canceledOnTouchOutside Whether the dialog should be canceled when touched outside the window.
     */
    public static AlertDialog showOneBtnDialog(Activity context, String content, boolean canceledOnTouchOutside) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE);
        window.findViewById(R.id.alert_space).setVisibility(View.GONE);
        View btnOk = window.findViewById(R.id.alert_ok);
        btnOk.setBackgroundResource(R.drawable.selector_dialog_one_button); //改变按钮的背景图
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(canceledOnTouchOutside);
        return dlg;
    }

    /**
     * 显示只有一个按钮的提示框
     * 可以设置title，button文字以及content文字
     *
     * @param context
     * @param content
     * @param canceledOnTouchOutside Whether the dialog should be canceled when touched outside the window.
     */
    public static AlertDialog showOneBtnDialog(Activity context, String content, String title,
                                               String btnTitle, boolean canceledOnTouchOutside) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE);
        window.findViewById(R.id.alert_space).setVisibility(View.GONE);

        View btnOk = window.findViewById(R.id.alert_ok);
        btnOk.setBackgroundResource(R.drawable.selector_dialog_one_button); //改变按钮的背景图
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        ((TextView) btnOk).setText(btnTitle);
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(canceledOnTouchOutside);
        return dlg;
    }

    /**
     * 显示只有一个按钮的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showOneBtnDialog(Activity context, String content, final DMDialogListener dmDialogListener) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE);
        window.findViewById(R.id.alert_space).setVisibility(View.GONE);

        View btnOk = window.findViewById(R.id.alert_ok);
        btnOk.setBackgroundResource(R.drawable.selector_dialog_one_button);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onRightClick(dlg);
                } else {
                    dlg.cancel();
                }
            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

    /**
     * 显示app更新的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showUpdateDialog(Activity context, String title, String content, final DMDialogListener dmDialogListener,
                                               final boolean forceUpdate) {
        View view = View.inflate(context, R.layout.dialog_app_update, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_app_update);
        TextView contentView = (TextView) window.findViewById(R.id.tv_content);
        TextView updateTitle = (TextView) window.findViewById(R.id.tv_title);

        setUpdateTitle(contentView, content);
        updateTitle.setText(title);
        if (forceUpdate) {
            window.findViewById(R.id.tv_ignore).setVisibility(View.GONE);
            window.findViewById(R.id.update_devider).setVisibility(View.GONE);
            window.findViewById(R.id.tv_update_now).setBackgroundResource(R.drawable.selector_dialog_force_update_button);
        }

        window.findViewById(R.id.tv_ignore).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onLeftClick(dlg);
                }
                if (!forceUpdate)
                    dlg.cancel();
            }
        });
        window.findViewById(R.id.tv_update_now).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onRightClick(dlg);
                }
                dlg.cancel();

            }
        });
        return dlg;
    }

    private static void setUpdateTitle(TextView contentView, String content) {
        //将中文的逗号替换成英文的
//        content = content.replace(";","\n").replace("。","\n");
        contentView.setText(content);
    }

    /**
     * 显示只有一个按钮的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showOneBtnDialog(Activity context, String content, final DMDialogListener dmDialogListener,
                                               String btnString) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE);
        window.findViewById(R.id.alert_space).setVisibility(View.GONE);
        TextView btnOk = (TextView) window.findViewById(R.id.alert_ok);
        btnOk.setBackgroundResource(R.drawable.selector_dialog_one_button);
        btnOk.setText(btnString);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onRightClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

    /**
     * 显示只有一个按钮的提示框, 根据参数决定点击其他位置dialog是否消失
     *
     * @param context
     * @param content
     */
    public static AlertDialog showOneBtnDialog(Activity context, String content, boolean canceledOnTouchOutside,
                                               final DMDialogListener dmDialogListener) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE);
        window.findViewById(R.id.alert_space).setVisibility(View.GONE);
        View btnOk = window.findViewById(R.id.alert_ok);
        btnOk.setBackgroundResource(R.drawable.selector_dialog_one_button);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onRightClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(canceledOnTouchOutside);
        return dlg;
    }

    /**
     * 显示只有一个按钮的提示框, 根据参数决定点击其他位置dialog是否消失
     *
     * @param context
     * @param content
     */
    public static AlertDialog showOneBtnDialog(Activity context, String content, boolean canceledOnTouchOutside,
                                               boolean cancelable, final DMDialogListener dmDialogListener) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE);
        window.findViewById(R.id.alert_space).setVisibility(View.GONE);

        View btnOk = window.findViewById(R.id.alert_ok);
        btnOk.setBackgroundResource(R.drawable.selector_dialog_one_button);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onRightClick(dlg);
                }
                dlg.cancel();
            }
        });
        dlg.setCancelable(cancelable);
        dlg.setCanceledOnTouchOutside(canceledOnTouchOutside);
        return dlg;
    }


    /**
     * 显示只有一个确定按钮的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showOneBtnConfirmDialog(Activity context, String content,
                                                      final DMDialogShowOneBtnListener dmDialogListener) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE);
        window.findViewById(R.id.alert_space).setVisibility(View.GONE);
        View btnOk = window.findViewById(R.id.alert_ok);
        btnOk.setBackgroundResource(R.drawable.selector_dialog_one_button);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onBtnClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

    /**
     * 显示只有一个确定按钮的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showOneBtnConfirmDialog(Context context, String title, String content,
                                                      final DMDialogShowOneBtnListener dmDialogListener) {
        View view = View.inflate(context, R.layout.push_dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.push_dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        TextView titleView = (TextView) window.findViewById(R.id.alert_title);
        contentView.setText(content);
        titleView.setText(title);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE);
        window.findViewById(R.id.alert_space).setVisibility(View.GONE);
        View btnOK = window.findViewById(R.id.alert_ok);
        btnOK.setBackgroundResource(R.drawable.selector_dialog_one_button);
        btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onBtnClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        window.findViewById(R.id.alert_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dlg.cancel();
            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

    /**
     * 显示有两个按钮的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showTowBtnDialog(Context context, String content, final DMDialogListener dmDialogListener) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.VISIBLE);
        window.findViewById(R.id.alert_space).setVisibility(View.VISIBLE);
        window.findViewById(R.id.alert_cancle).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onLeftClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        window.findViewById(R.id.alert_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onRightClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

    /**
     * 显示有两个按钮的提示框
     *
     * @param context
     * @param content
     * @param config           用来设置按钮文字等
     * @param dmDialogListener
     */
    public static AlertDialog showTowBtnDialog(Activity context, String content, Config config,
                                               final DMDialogListener dmDialogListener) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if(context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.VISIBLE);
        window.findViewById(R.id.alert_space).setVisibility(View.VISIBLE);
        TextView lBtn = (TextView) window.findViewById(R.id.alert_cancle);
        TextView rBtn = (TextView) window.findViewById(R.id.alert_ok);
        lBtn.setText(config.leftText);
        rBtn.setText(config.rightText);
        lBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onLeftClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        rBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onRightClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

    /**
     * 显示输入框
     *
     * @param context
     * @param dmEditTextDialogListener
     */
    public static AlertDialog showEditTextDialog(final Context context, final DMEditTextDialogListener dmEditTextDialogListener) {
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        final Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_trade_pwd);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);// AlertDialog中EditText不能弹出输入法解决方法
        ((TextView) window.findViewById(R.id.alert_title)).setText("交易密码");
        final EditText inputView = (EditText) window.findViewById(R.id.et_pwd);
        CommonUtil.openKey(context, inputView);
        window.findViewById(R.id.alert_cancle).setVisibility(View.VISIBLE);
        window.findViewById(R.id.alert_space).setVisibility(View.VISIBLE);
        window.findViewById(R.id.alert_ok).setEnabled(false);
        inputView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString().trim().length() >= 6) {
                    window.findViewById(R.id.alert_ok).setEnabled(true);
                } else {
                    window.findViewById(R.id.alert_ok).setEnabled(false);
                }
            }
        });
        window.findViewById(R.id.alert_cancle).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmEditTextDialogListener != null) {
                    dmEditTextDialogListener.onLeftClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        window.findViewById(R.id.alert_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmEditTextDialogListener != null) {
                    dmEditTextDialogListener.onRightClick(dlg, inputView.getText().toString().trim());
                } else {
                    dlg.cancel();
                }

            }
        });
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

    public interface DMDialogListener {
        void onLeftClick(final AlertDialog dlg);

        void onRightClick(final AlertDialog dlg);
    }

    public interface DMDialogShowOneBtnListener {
        void onBtnClick(final AlertDialog dlg);
    }

    public interface DMEditTextDialogListener {
        void onLeftClick(final AlertDialog dlg);

        void onRightClick(final AlertDialog dlg, String editText);
    }

    public static class Config {
        public String leftText;

        public String rightText;

        public Config() {

        }

        public Config(String leftText, String rightText) {
            this.leftText = leftText;
            this.rightText = rightText;
        }
    }

    /**
     * 新投资动线 输入交易密码 dialog
     */
    public static AlertDialog showOneBtnDialog(Context context, String title, int centerLayoutId) {

        //弹出输入密码对话框
        View view = View.inflate(context, R.layout.dialog_one_btn_base, null);
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setView(view);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_one_btn_base);
        FrameLayout centerContent = (FrameLayout) window.findViewById(R.id.content);
        centerContent.addView(View.inflate(context, centerLayoutId, null));
        TextView tradingTv = (TextView) window.findViewById(R.id.alert_title);
        window.findViewById(R.id.alert_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tradingTv.setText(title);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    /**
     * 设置交易密码
     *
     * @param context
     * @param listener
     * @return
     */

    public static AlertDialog settingDialog(final Activity context, final SubmitListener listener) {

        final AlertDialog settingDealPwdDialog = DMAlertUtil.showOneBtnDialog(context, "请设置交易密码", R.layout.dialog_setting_pwd_content);
        final Window window = settingDealPwdDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        final ShowPwdEditText showPwdEdittet = (ShowPwdEditText) window.findViewById(R.id.show_pwd_edittext);

        window.findViewById(R.id.alert_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                settingDealPwdDialog.cancel();
                showPwdEdittet.closeKeyBoard();
            }
        });
        //点击确定
        window.findViewById(R.id.submit_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                BaseActivity baseActivity = (BaseActivity) context;
                if (baseActivity.checkClick(v.getId())) {

                    if (listener != null) {
                        listener.submit(showPwdEdittet.getText());
                    }
                }
            }
        });
        return settingDealPwdDialog;
    }


    public interface SubmitListener {

        void submit(String pwd);
    }

    public interface SubmitListenerWithDialog {
        void submit(String pwd, AlertDialog dialog);
    }

//    /**
//     * 输入交易密码/登录密码 对话框
//     */
//    public static AlertDialog showDealOrLoginPwdDialog(final BaseActivity activity, final boolean isLogin, String title, String hint, final SubmitListenerWithDialog listener) {
//        final AlertDialog mJudgeDealPwdDialog = DMAlertUtil.showOneBtnDialog(activity, title,isLogin? R.layout.dialog_trade_password_content_hide_tip : R.layout.dialog_trade_password_content);
//
//        final Window window = mJudgeDealPwdDialog.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        final EditText editTextPwd = (EditText) window.findViewById(R.id.edittext_password);
//        editTextPwd.setHint(TextUtils.isEmpty(hint) ? "" : hint);
//
//        //打开键盘
//        inputBankCardEtFocus(activity, editTextPwd);
//        window.findViewById(R.id.alert_close).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mJudgeDealPwdDialog.cancel();
//                CommonUtil.closeKey(activity);
//            }
//        });
//        //点击忘记交易密码
//        TextView forgetPwd = (TextView) window.findViewById(R.id.textview_forget_pwd);
//        forgetPwd.setText(isLogin ? "忘记登录密码?" : "忘记交易密码?");
//        forgetPwd.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (activity.checkClick(v.getId())) {
//                    mJudgeDealPwdDialog.cancel();
//                    //忘记密码
//                    CommonUtil.closeKey(activity);
//                    if (!isLogin) {
//
//                        judgeLoginPwdDialog(activity,true);
//                    } else {
//                        Intent itFindDealPwd = new Intent(activity, FindPwdActivity.class);
//                        activity.startActivity(itFindDealPwd);
//                    }
//                }
//            }
//        });
//        //点击确定
//        window.findViewById(R.id.submit_btn).setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (activity.checkClick(v.getId())) {
//
//                    String inputStr = editTextPwd.getText().toString();
//                    if (TextUtils.isEmpty(inputStr)) {
//
//                        DMAlertUtil.showOneBtnDialog(activity, isLogin ?"登陆密码不能为空":"交易密码不能为空");  //交易密码不能为空
//                        return;
//                    }
//                    if (listener != null) {
//
//                        listener.submit(inputStr, mJudgeDealPwdDialog);
//                    }
//                }
//            }
//        });
//        return mJudgeDealPwdDialog;
//    }

    /**
     * 输入登录密码，不提示忘记密码
     * @param activity
     * @param isLogin
     * @param title
     * @param hint
     * @param listener
     * @return
     */
    public static AlertDialog showLoginHideForgetPwd(final BaseActivity activity, final boolean isLogin, String title, String hint, final SubmitListenerWithDialog listener) {
        final AlertDialog mJudgeDealPwdDialog = DMAlertUtil.showOneBtnDialog(activity, title, R.layout.dialog_trade_password_content_hide_forgetpwd_tip);

        final Window window = mJudgeDealPwdDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        final EditText editTextPwd = (EditText) window.findViewById(R.id.edittext_password);
        editTextPwd.setHint(TextUtils.isEmpty(hint) ? "" : hint);
        //打开键盘
        inputBankCardEtFocus(activity, editTextPwd);
        window.findViewById(R.id.alert_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mJudgeDealPwdDialog.cancel();
                CommonUtil.closeKey(activity);
            }
        });
        //点击确定
        window.findViewById(R.id.submit_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (activity.checkClick(v.getId())) {

                    String inputStr = editTextPwd.getText().toString();
                    if (TextUtils.isEmpty(inputStr)) {

                        DMAlertUtil.showOneBtnDialog(activity, isLogin ?"登陆密码不能为空":"交易密码不能为空");  //交易密码不能为空
                        return;
                    }
                    if (listener != null) {

                        listener.submit(inputStr, mJudgeDealPwdDialog);
                    }
                }
            }
        });
        return mJudgeDealPwdDialog;
    }

    private static void inputBankCardEtFocus(final Activity activity, final EditText editText) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                CommonUtil.openKey(activity, editText); //打开键盘
            }
        });

    }

//    /**
//     * 验证登录密码是否正确 对话框
//     * @param hideForgetPwdTip 忘记登录密码
//     */
//    private static void judgeLoginPwdDialog(final BaseActivity activity,boolean hideForgetPwdTip) {
//
//        if(activity.isFinishing()) {
//            return;
//        }
//        AlertDialog alertDialog = showLoginHideForgetPwd(activity, true, "请输入登录密码", "请输入登录密码", new SubmitListenerWithDialog() {
//            @Override
//            public void submit(final String pwd, final AlertDialog dialog) {
//
//                //验证密码
//                ThreadsPool.executeOnExecutor(new Runnable() {
//                    @Override
//                    public void run() {
//                        DbUtils db = DbUtils.create(activity);
//                        SharedPreferences sharedPreferences = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//                        int userId = sharedPreferences.getInt("userId", -1);
//                        LockPwd lockPwd = null;
//                        try {
//                            db.createTableIfNotExist(LockPwd.class);
//                            lockPwd = db.findFirst(Selector.from(LockPwd.class).where("userId", "=", userId));
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                            dialog.cancel();
//                        }
//                        if (lockPwd != null) {
//                            String loginPwd = EncrypUtil.decrypt(DMConstant.StringConstant.ENCRYP_SEND, lockPwd.getLoginPwd());
//                            if (pwd.isEmpty()) {
//                                //登录密码为空
//                                activity.runOnUiThread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        DMAlertUtil.showOneBtnDialog(activity, "密码不能为空！");
//                                    }
//                                });
//
//                            } else if (pwd.equals(loginPwd)) {
//
//                                dialog.cancel();
//                                //跳转到设置交易密码
//                                activity.runOnUiThread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        Intent intent = new Intent(activity, FindTradingPwdActivity.class);
//                                        activity.startActivity(intent);
//                                    }
//                                });
//
//                            } else {
//                                //登录密码不正确
//                                activity.runOnUiThread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        DMAlertUtil.showTowBtnDialog(activity, "登录密码不正确，请重试！", new Config("确定",
//                                                "找回密码"), new DMDialogListener() {
//                                            @Override
//                                            public void onRightClick(AlertDialog dlgg) {
//                                                //忘记密码-->找回密码
//                                                Intent intent = new Intent(activity, FindPwdActivity.class);
//                                                activity.startActivity(intent);
//                                                dlgg.cancel();
//                                            }
//
//                                            @Override
//                                            public void onLeftClick(AlertDialog dlgg) {
//                                                //确定
//                                                dlgg.cancel();
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });
//            }
//        });
//        alertDialog.setCancelable(true);
//        alertDialog.setCanceledOnTouchOutside(true);
//    }

//    /**
//     * 验证登录密码是否正确 对话框
//     */
//    private static void judgeLoginPwdDialog(final BaseActivity activity) {
//
//        if(activity.isFinishing()) {
//            return;
//        }
//        AlertDialog alertDialog = showDealOrLoginPwdDialog(activity, true, "请输入登录密码", "请输入登录密码", new SubmitListenerWithDialog() {
//            @Override
//            public void submit(final String pwd, final AlertDialog dialog) {
//
//                //验证密码
//                ThreadsPool.executeOnExecutor(new Runnable() {
//                    @Override
//                    public void run() {
//                        DbUtils db = DbUtils.create(activity);
//                        SharedPreferences sharedPreferences = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//                        int userId = sharedPreferences.getInt("userId", -1);
//                        LockPwd lockPwd = null;
//                        try {
//                            db.createTableIfNotExist(LockPwd.class);
//                            lockPwd = db.findFirst(Selector.from(LockPwd.class).where("userId", "=", userId));
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                            dialog.cancel();
//                        }
//                        if (lockPwd != null) {
//                            String loginPwd = EncrypUtil.decrypt(DMConstant.StringConstant.ENCRYP_SEND, lockPwd.getLoginPwd());
//                            if (pwd.isEmpty()) {
//                                //登录密码为空
//                                activity.runOnUiThread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        DMAlertUtil.showOneBtnDialog(activity, "密码不能为空！");
//                                    }
//                                });
//
//                            } else if (pwd.equals(loginPwd)) {
//
//                                dialog.cancel();
//                                //跳转到设置交易密码
//                                activity.runOnUiThread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        Intent intent = new Intent(activity, FindTradingPwdActivity.class);
//                                        activity.startActivity(intent);
//                                    }
//                                });
//
//                            } else {
//                                //登录密码不正确
//                                activity.runOnUiThread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        DMAlertUtil.showTowBtnDialog(activity, "登录密码不正确，请重试！", new Config("确定",
//                                                "找回密码"), new DMDialogListener() {
//                                            @Override
//                                            public void onRightClick(AlertDialog dlgg) {
//                                                //忘记密码-->找回密码
//                                                Intent intent = new Intent(activity, FindPwdActivity.class);
//                                                activity.startActivity(intent);
//                                                dlgg.cancel();
//                                            }
//
//                                            @Override
//                                            public void onLeftClick(AlertDialog dlgg) {
//                                                //确定
//                                                dlgg.cancel();
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });
//            }
//        });
//        alertDialog.setCancelable(true);
//        alertDialog.setCanceledOnTouchOutside(true);
//    }
}
