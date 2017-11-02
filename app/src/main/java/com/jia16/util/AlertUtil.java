package com.jia16.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseActivity;

/**
 * Created by huangjun on 16/8/18.
 */
public class AlertUtil {


    /**
     * 显示只有一个按钮的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showOneBtnDialog(Activity context, String content, final View.OnClickListener listener) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if (context.isFinishing()) {
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
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
        return dlg;
    }


    /**
     * 显示只有一个按钮的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showOneBtnDialog(Activity context, String content,String btnText, final View.OnClickListener listener) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if (context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_alert);
        TextView contentView = (TextView) window.findViewById(R.id.alert_content);
        contentView.setText(content);
        window.findViewById(R.id.alert_cancle).setVisibility(View.GONE); //隐藏取消button
        window.findViewById(R.id.alert_space).setVisibility(View.GONE); //隐藏button间的分割线
        TextView btnOk = (TextView) window.findViewById(R.id.alert_ok);
        btnOk.setText(btnText);
        btnOk.setBackgroundResource(R.drawable.selector_dialog_one_button); //改变按钮的背景图
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
        return dlg;
    }


    public interface OnOkBtnClickListener {
        void onclick(AlertDialog dialog);
    }

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
        window.findViewById(R.id.alert_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tradingTv.setText(title);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    public static AlertDialog showThreeBtnDialog(Context context, final DialogListener mDialogListener) {

        //弹出输入密码对话框
        View view = View.inflate(context, R.layout.dialog_three_btn, null);
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setView(view);
        dialog.show();
        Window window = dialog.getWindow();
//        TextView titleTv = (TextView) window.findViewById(R.id.alert_title);
//        titleTv.setText(title);
        TextView mBtn1 = (TextView) window.findViewById(R.id.button1);
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogListener != null) {
                    mDialogListener.onTopClick(dialog);
                } else {
                    dialog.cancel();
                }
            }
        });
        TextView mBtn2 = (TextView) window.findViewById(R.id.button2);
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogListener != null) {
                    mDialogListener.onCenterClick(dialog);
                } else {
                    dialog.cancel();
                }
            }
        });
        TextView mBtn3 = (TextView) window.findViewById(R.id.button3);
        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogListener != null) {
                    mDialogListener.onBottomClick(dialog);
                } else {
                    dialog.cancel();
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    /**
     * 输入交易密码/登录密码 对话框
     */
    public static AlertDialog showDealOrLoginPwdDialog(final BaseActivity activity, final boolean isLogin, String title, String hint, final SubmitListenerWithDialog listener) {
        final AlertDialog mJudgeDealPwdDialog = AlertUtil.showOneBtnDialog(activity, title, R.layout.dialog_trade_password_content);

        final Window window = mJudgeDealPwdDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        final EditText editTextPwd = (EditText) window.findViewById(R.id.edittext_password);
        final ImageView clearBtn = (ImageView) window.findViewById(R.id.btn_clear_pwd);
        final CheckBox mSwitch = (CheckBox) window.findViewById(R.id.switch_password);
        final Button mSubmitButton = (Button) window.findViewById(R.id.submit_btn);
        mSubmitButton.setEnabled(false);
        editTextPwd.setHint(TextUtils.isEmpty(hint) ? "" : hint);
        editTextPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    mSubmitButton.setEnabled(true);
                } else {
                    mSubmitButton.setEnabled(false);
                }
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPwd.setText("");
            }
        });
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置EditText文本为可见的
                    editTextPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                    editTextPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isChecked = !isChecked;
                editTextPwd.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = editTextPwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        //打开键盘
        inputBankCardEtFocus(activity, editTextPwd);
        window.findViewById(R.id.alert_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mJudgeDealPwdDialog.cancel();
                CommonUtil.closeKey(activity);
            }
        });
        //点击忘记交易密码
        TextView forgetPwd = (TextView) window.findViewById(R.id.textview_forget_pwd);
        forgetPwd.setText(isLogin ? "忘记登录密码?" : "忘记交易密码?");
        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity.checkClick(v.getId())) {
//                    mJudgeDealPwdDialog.cancel();
                    //忘记密码
                    CommonUtil.closeKey(activity);
                    ToastUtil.getInstant().show(activity, "忘记交易密码请在“我的账户”页找回");
                }
            }
        });

        //点击确定
        mSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (activity.checkClick(v.getId())) {

                    String inputStr = editTextPwd.getText().toString();
                    if (TextUtils.isEmpty(inputStr)) {
                        AlertUtil.showOneBtnDialog(activity, isLogin ? "登录密码不能为空" : "交易密码不能为空", null);  //交易密码不能为空
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


    /**
     * 显示有两个按钮的提示框
     *
     * @param context
     * @param content
     * @param config          用来设置按钮文字等
     * @param mDialogListener
     */
    public static AlertDialog showTwoBtnDialog(Activity context, String content, Config config,
                                               final DialogListener mDialogListener) {
        View view = View.inflate(context, R.layout.dialog_alert, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if (context.isFinishing()) {
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
        lBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogListener != null) {
                    mDialogListener.onLeftClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogListener != null) {
                    mDialogListener.onRightClick(dlg);
                } else {
                    dlg.cancel();
                }

            }
        });
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
        return dlg;
    }


    /**
     * 显示app更新的提示框
     *
     * @param context
     * @param content
     */
    public static AlertDialog showUpdateDialog(Activity context, String title, String content, final DialogListener dmDialogListener,
                                               final boolean forceUpdate) {
        View view = View.inflate(context, R.layout.dialog_app_update, null);
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setView(view);
        if (context.isFinishing()) {
            return null;
        }
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_app_update);
        TextView contentView = (TextView) window.findViewById(R.id.tv_content);
        TextView updateTitle = (TextView) window.findViewById(R.id.tv_title);
        contentView.setText(content);
        updateTitle.setText(title);
        if (forceUpdate) {
            window.findViewById(R.id.tv_ignore).setVisibility(View.GONE);
            window.findViewById(R.id.update_devider).setVisibility(View.GONE);
            window.findViewById(R.id.tv_update_now).setBackgroundResource(R.drawable.selector_dialog_force_update_button);
        }

        window.findViewById(R.id.tv_ignore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmDialogListener != null) {
                    dmDialogListener.onLeftClick(dlg);
                }
                if (!forceUpdate)
                    dlg.cancel();
            }
        });
        window.findViewById(R.id.tv_update_now).setOnClickListener(new View.OnClickListener() {
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

//    /**
//     * 显示app更新的提示框
//     *
//     * @param context
//     * @param content
//     */
//    public static AlertDialog showUpdateDialog(Activity context, String title, String content, String image, final DialogListener dmDialogListener,
//                                               final boolean forceUpdate) {
//        View view = View.inflate(context, R.layout.dialog_app_update, null);
//        final AlertDialog dlg = new AlertDialog.Builder(context).create();
//        dlg.setView(view);
//        if (context.isFinishing()) {
//            return null;
//        }
//        dlg.show();
//        final Window window = dlg.getWindow();
//        // 实例化一个ColorDrawable颜色为半透明//#19e64dff  #c0000000   #88000000
//        ColorDrawable dw = new ColorDrawable(Color.parseColor("#00000000"));
//        window.setBackgroundDrawable(dw);
//        window.setContentView(R.layout.dialog_app_update);
//        //TextView contentView = (TextView) window.findViewById(R.id.tv_content);
//        //TextView updateTitle = (TextView) window.findViewById(R.id.tv_title);
//        //contentView.setText(content);
//        //updateTitle.setText(title);
//
//        ImageView mIvImage = (ImageView) window.findViewById(R.id.iv_image);
//
//        ImageLoader.getInstance().displayImage(image, mIvImage, ImageLoadOptions.fadeIn_options_nocache, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                //加载完成,显示立即更新按钮,显示关闭图片
//                window.findViewById(R.id.tv_update_now).setVisibility(View.VISIBLE);
//
//                if (forceUpdate) {
//                    window.findViewById(R.id.tv_ignore).setVisibility(View.GONE);
//                    //window.findViewById(R.id.update_devider).setVisibility(View.GONE);
//                    //window.findViewById(R.id.tv_update_now).setBackgroundResource(R.drawable.selector_dialog_force_update_button);
//                }else {
//                    window.findViewById(R.id.tv_ignore).setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//
//            }
//        });
//
//
//
//        window.findViewById(R.id.tv_ignore).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dmDialogListener != null) {
//                    dmDialogListener.onLeftClick(dlg);
//                }
//                if (!forceUpdate)
//                    dlg.cancel();
//            }
//        });
//        window.findViewById(R.id.tv_update_now).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dmDialogListener != null) {
//                    dmDialogListener.onRightClick(dlg);
//                }
//                dlg.cancel();
//
//            }
//        });
//        return dlg;
//    }



    public interface DialogListener {
        void onLeftClick(final AlertDialog dlg);

        void onRightClick(final AlertDialog dlg);

        void onTopClick(final AlertDialog dlg);

        void onCenterClick(final AlertDialog dlg);

        void onBottomClick(final AlertDialog dlg);
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

    public interface SubmitListenerWithDialog {
        void submit(String pwd, AlertDialog dialog);
    }

    public static class Config {

        public Config(String leftText, String rightText) {
            this.leftText = leftText;
            this.rightText = rightText;
        }

        public String leftText;
        public String rightText;
    }
}
