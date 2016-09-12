package com.jia16.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.SparseBooleanArray;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jia16.bean.LockPwd;
import com.jia16.bean.UserInfo;
import com.jia16.util.Constants;
import com.jia16.util.PreferencesUtil;

import java.util.List;

/**
 * Created by huangjun on 16/8/15.
 */
public class BaseApplication extends Application {
    /**
     * 应用实例
     */
    private static BaseApplication instance;

    // 不必为每一次HTTP请求都创建一个RequestQueue对象，推荐在application中初始化
    private static RequestQueue requestQueue;

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * 返回 application对象
     *
     * @return
     */
    public static BaseApplication getInstance() {
        return instance;
    }


    public SparseBooleanArray hasLockedUsers = new SparseBooleanArray();
    /**
     * 是否调试模式
     */
    public boolean isDebug = false;


    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        if (userInfo != null) {
            return userInfo;
        } else {
            //将userInfo 从文件中读出来
            String userInfoBeanStr = PreferencesUtil.getUserInfoBean();
            if (!TextUtils.isEmpty(userInfoBeanStr)) {
                return new Gson().fromJson(userInfoBeanStr, UserInfo.class);
            }
            return null;
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            this.userInfo = userInfo;
            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            sharedPreferences.edit().putInt("userId", userInfo.getId()).apply();
            String userInfoStr = new Gson().toJson(userInfo);
            PreferencesUtil.setUserInfoBean(userInfoStr); //将userinfo 序列化到本地
        } else {
            this.userInfo = userInfo;
            PreferencesUtil.setUserInfoBean("");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        instance = this;
        AppContext.init(this);
    }

    /**
     * 查询已经设置过手势密码的所有用户
     */
    public void getLockedUserId(SharedPreferences sharedPreferences) {
        String str = sharedPreferences.getString(Constants.LOCK_PWD, "");
        List<LockPwd> lockPwds = new Gson().fromJson(str, new TypeToken<List<LockPwd>>() {
        }.getType());

        if (lockPwds != null && lockPwds.size() > 0) {
            int size = lockPwds.size();
            for (int i = 0; i < size; i++) {
                if (lockPwds.get(i) != null) {
                    hasLockedUsers.put(lockPwds.get(i).getUserId(), true);
                }
            }
        }

//        db = DbUtils.create(getApplicationContext());
//        try {
//            db.createTableIfNotExist(LockPwd.class);
//            List<LockPwd> list = db.findAll(LockPwd.class);
//            for (LockPwd lockPwd : list) {
//                hasLockedUsers.put(lockPwd.getUserId(), true);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
    }
}
