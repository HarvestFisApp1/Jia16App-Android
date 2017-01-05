package com.jia16.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.TextView;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.UserInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangxg on 2016/1/22.
 */
public class CommoUtil {

    private static final Long DAY = 86400000L;//24 * 60 * 60 * 1000  24小时

    /**
     * 获取AppKey
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai =
                    context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Activity aty) {
        TelephonyManager tm = (TelephonyManager) aty.getSystemService(Activity.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = true;//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        if (Build.VERSION.SDK_INT < 20) {
            isScreenOn = pm.isScreenOn();
        } else {
            isScreenOn = pm.isInteractive();
        }
        return isScreenOn;
    }

    /**
     * 判断是否后台运行
     *
     * @param context
     * @return
     */
    public static boolean isRunningPackName(Context context) {
        String packageName = context.getPackageName();
        if (getRunningPackName(context).equals(packageName)) {
            return true;
        }
        return false;
    }

    private static String getRunningPackName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        /** 获取当前正在运行的任务栈列表， 越是靠近当前运行的任务栈会被排在第一位，之后的以此类推 */
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);

        /** 获得当前最顶端的任务栈，即前台任务栈 */
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);

        /** 获取前台任务栈的最顶端 Activity */
        ComponentName topActivity = runningTaskInfo.baseActivity;

        /** 获取应用的包名 */
        String packageName = topActivity.getPackageName();
        return packageName;
    }

    /**
     * 关闭软键盘
     */
    public static void closeKey(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开软键盘
     */
    public static void openKey(Context context, final EditText inputView) {
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                inputView.setFocusable(true);
                inputView.setFocusableInTouchMode(true);
                inputView.requestFocus();
                inputMethodManager.showSoftInput(inputView, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 300);
    }

    /**
     * 写入cookies
     * app启动时，登录成功，写入cookies
     * 退出登录时，清除cookies
     */
//    public static void initCookies(Context context) {
//
//        CookieSyncManager.createInstance(context);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        String token = PreferencesUtil.getToken(context);
//        cookieManager.setCookie(Constants.HOME_PAGE, "token=" + (TextUtils.isEmpty(token) ? "" : token));
//        int id = -1;
//        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
//        if (userInfo != null) {
//
//            id = userInfo.getId();
//        }
//        cookieManager.setCookie(Constants.HOME_PAGE, "id=" + id);
//        CookieSyncManager.getInstance().sync();
//        Lg.d("写入了cookie");
//    }

//    /**
//     * 添加一个cookies
//     *
//     * @param context
//     * @param url     指定的url
//     */
//    public static void addCookies(Context context, String url) {
//
//        CookieSyncManager.createInstance(context);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        UserInfo userInfo = P2PApplication.getInstance().getUserInfo();
//        if (userInfo == null) {
//
//            Intent intent = new Intent(context, LoginActivity.class);
//            context.startActivity(intent);
//        } else {
//
//            String token = PreferencesUtil.getToken(context);
//            cookieManager.setCookie(url, "id=" + userInfo.getId());
//            cookieManager.setCookie(url, "token=" + token);
//            CookieSyncManager.getInstance().sync();
//            Lg.d("target" + url + "写入了id" + userInfo.getId());
//        }
//    }

    /**
     * 移除cookies
     * 退出登录时，清除cookies
     *
     * @param context
     */
    public static void delCookies(Context context) {

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        //移除所有的cookies
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {

                }
            });
        } else {

            cookieManager.removeAllCookie();
        }
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 是否提示用户去完善信息
     */
//    public static void showOrHintTips(final Activity context, UserInfo userInfo) {
//
//        if(userInfo == null || !PreferencesUtil.getBoolean(context, "showed_tips") || context.isFinishing()) {
//
//            return;
//        }
//        boolean isShowedAuth = PreferencesUtil.getBoolean(context, "isShowedAuth");
//        //上次统计的登录未认证用户  24小时后会再统计一次 再将时间重新赋值
//        long lastLogTime = PreferencesUtil.getSharedPreferences(context).getLong("lastLogTime", 0L);
//        if (!userInfo.isMobileVerified() || TextUtils.isEmpty(userInfo.getRealName())) {
//            if (System.currentTimeMillis() - lastLogTime > DAY) {//超过24小时 该统计了
//                MobclickAgent.onEvent(context, "login_unauth_user");
//                Lg.d("MobclickAgent", "login_unauth_user");
//                PreferencesUtil.getSharedPreferences(context).edit().putLong("lastLogTime", System.currentTimeMillis()).commit();
//            }
//            if (!isShowedAuth) {
//                PreferencesUtil.setBoolean(context, "isShowedAuth", true);
//                DMAlertUtil.showTowBtnDialog(context, context.getString(R.string.register_ok_alert), new DMAlertUtil.Config(
//                        context.getString(R.string.goto_auth), context.getString(R.string.not_goto_auth)), new DMAlertUtil.DMDialogListener() {
//                    @Override
//                    public void onRightClick(AlertDialog dlg) {
//                        dlg.cancel();
//                    }
//
//                    @Override
//                    public void onLeftClick(AlertDialog dlg) {
//                        Intent intent = new Intent(context, SecurityInfoActivity.class);
//                        context.startActivity(intent);
//                        dlg.cancel();
//                    }
//                });
//            }
//        }
//    }

//    /**
//     * 弹出一度指引的内容
//     */
//    public static void showGuideOne(final Activity context) {
//
//        //请求 一度指引 图片的内容
//        final NewUserTip newUserTip = P2PApplication.getInstance().getmNewUserTip();
//        if (newUserTip != null) {
//
//            //判断是否显示的规则： 1.在显示的时间内，2.没有显示过
////            long currentTimeMillis = System.currentTimeMillis();
//            if (/*currentTimeMillis >= newUserTip.getStartTime() && currentTimeMillis <= newUserTip.getEndTime() && */PreferencesUtil.getTipsId() != newUserTip.getId()) {
//
//                mHandle.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Intent intent = new Intent(context, GuideOneActivity.class);
//                        context.startActivity(intent);
//                        context.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_from_bottom);
//                        //记录到sp 中，下次不再显示
//                        PreferencesUtil.setTipsId(newUserTip.getId());
//                    }
//                }, 100);
//            }
//        }
//    }

    public static Handler mHandle = new Handler();

    //拨打电话

    /**
     * 拨打电话的对话框
     *
     * @param str
     * @param num
     */
    public static void showPhoneDialog(final Activity activity, String str, final String num) {

        View view = View.inflate(activity, R.layout.dialog_goods_detail, null);
        if(activity.isFinishing()) {
            return;
        }
        final AlertDialog dlg = new AlertDialog.Builder(activity).create();
        dlg.setView(view);
        dlg.show();
        dlg.setCancelable(false);
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_goods_detail);
        TextView tvShowMsg = (TextView) window.findViewById(R.id.alert_content);
        tvShowMsg.setText(str);

        // 点击确定按钮
        TextView btnSubmit = (TextView) window.findViewById(R.id.alert_ok);
        TextView btnCancel = (TextView) window.findViewById(R.id.alert_cancle);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                phone(num, activity);
                dlg.cancel();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }

    /**
     * 拨打电话
     *
     * @param num
     */
    private static void phone(String num, Activity activity) {

        if (num != null) {
            String phone = num.replace("-", "").replace(" ", "").trim();
            // 传入服务， parse（）解析号码
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            // 通知activtity处理传入的call服务
            activity.startActivity(intent);
        } else {
            ToastUtil.getInstant().show(activity, "网络忙，未获取到客服电话");
        }
    }
}
