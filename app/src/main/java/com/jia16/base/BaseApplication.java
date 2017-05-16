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
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

/**
 * Created by huangjun on 16/8/15.
 */
public class BaseApplication extends Application {

    public String urlData;//卓金服H5页面唤醒嘉石榴app获取参数,存储变量的成员变量


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


        UMShareAPI.get(this);
        Config.isJumptoAppStore = true;
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

    }



    //各个平台的配置，建议放在全局Application或者程序入口
//    {
//        //微信 wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
//        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
//        //豆瓣RENREN平台目前只能在服务器端配置
//        //新浪微博
//        //PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
//        //易信
//        //PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
////        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
////        PlatformConfig.setAlipay("2015111700822536");
////        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
////        PlatformConfig.setPinterest("1439206");
////        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
////        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
//
//    }


        //各个平台的配置，建议放在全局Application或者程序入口
    {

        PlatformConfig.setWeixin("wx7208eaaf0f57f34d", "08034277e141e640cadbc6e26842f96a");
        //PlatformConfig.setWeixin("wxcea464b68f53bf23", "89e23e59521457eab7c294529e31c673");
        //PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        //PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setQQZone("1105662722", "CeFHk1L2OFiYxsE5");

    }
}
