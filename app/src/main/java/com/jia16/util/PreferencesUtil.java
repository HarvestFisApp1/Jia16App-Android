package com.jia16.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jia16.base.BaseApplication;

/**
 * Created by huangjun on 16/8/18.
 */
public class PreferencesUtil {


    private static final String USER_INFO_BEAN_TAG = "user_info_bean";

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences("cache", Context.MODE_PRIVATE);
    }

    /**
     * 存放boolean值
     */
    public static void setBoolean(Context ctx, String key, boolean value) {
        getSharedPreferences(ctx).edit().putBoolean(key, value).apply();
    }

    /**
     * 取出boolean值
     */
    public static boolean getBoolean(Context ctx, String key) {
        return getSharedPreferences(ctx).getBoolean(key, false);
    }


    /**
     * 记录找回密码 用户的id
     */
    public static void setUserInfoBean(String str) {

        getSharedPreferences(BaseApplication.getInstance()).edit().putString(USER_INFO_BEAN_TAG, str).apply();
    }

    public static String getUserInfoBean() {

        return getSharedPreferences(BaseApplication.getInstance()).getString(USER_INFO_BEAN_TAG, "");
    }

}
