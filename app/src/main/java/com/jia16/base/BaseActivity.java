package com.jia16.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.bean.LockPwd;
import com.jia16.bean.UserInfo;
import com.jia16.util.AppManager;
import com.jia16.util.Constants;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseActivity extends FragmentActivity implements View.OnClickListener {

    public SharedPreferences sharedPreferences;// 存储对像
    private Activity activity;
    /**
     * 保存上一次点击时间
     */
    private SparseArray<Long> lastClickTimes;
    private CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        initViews(this);
        lastClickTimes = new SparseArray<>();
    }

    private void initViews(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(Constants.STORE_NAME,
                Context.MODE_PRIVATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        lastClickTimes = null;
    }

    @Override
    public void onClick(View view) {

    }


    /**
     * 检查是否可执行点击操作
     * 防重复点击
     *
     * @return 返回true则可执行
     */
    public boolean checkClick(int id) {
        Long lastTime = lastClickTimes.get(id);
        Long thisTime = System.currentTimeMillis();
        lastClickTimes.put(id, thisTime);
        if (lastTime != null && thisTime - lastTime < 800) {
            // 快速双击，第二次不处理
            return false;
        } else {
            return true;
        }
    }

    protected void removeCookie() {
        sharedPreferences.edit().remove("_csrf").apply();
        sharedPreferences.edit().remove("Cookie").apply();
        sharedPreferences.edit().remove("cookie").apply();

        //shangjing修改
        sharedPreferences.edit().remove("p2psessionid").apply();

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }


    /***
     * 保存当前用户的remindstatus
     *
     * @param id
     * @param status
     */
    protected void setCurrentRemindStatus(int id, String status) {
        String lockReminds = sharedPreferences.getString(Constants.LOCK_PWD_REMINDS, "");
        HashMap<Integer, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(lockReminds)) {
            map = new Gson().fromJson(lockReminds, new TypeToken<HashMap<Integer, String>>() {
            }.getType());
        }
        map.put(id, status);
        sharedPreferences.edit().putString(Constants.LOCK_PWD_REMINDS, new Gson().toJson(map)).apply();
    }


    /***
     * 根据当前登录的用户id获取remindstatus
     *
     * @param id
     * @return
     */
    protected String getCurrentRemindStatus(int id) {
        String lockReminds = sharedPreferences.getString(Constants.LOCK_PWD_REMINDS, "");
        HashMap<Integer, String> map;
        if (!TextUtils.isEmpty(lockReminds)) {
            map = new Gson().fromJson(lockReminds, new TypeToken<HashMap<Integer, String>>() {
            }.getType());
        } else {
            return "0";
        }
        if (map == null) {
            return "0";
        } else {
            if (map.get(id) == null) {
                return "0";
            } else {
                return map.get(id);
            }
        }
    }

    protected void clearCurrentGesturePwd() {
        String lockPwdstr = sharedPreferences.getString(Constants.LOCK_PWD, "");
        List<LockPwd> lockPwds = new ArrayList<>();
        if (!TextUtils.isEmpty(lockPwdstr)) {//已经保存过手势密码
            lockPwds = new Gson().fromJson(lockPwdstr, new TypeToken<List<LockPwd>>() {
            }.getType());
            if (lockPwds != null && lockPwds.size() > 0) {
                UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                if (userInfo != null) {//如果用户都未登录 那么不展示手势密码解锁
                    for (Iterator<LockPwd> it = lockPwds.iterator(); it.hasNext(); ) {
                        LockPwd lockPwd = it.next();
                        if (lockPwd != null && lockPwd.getUserId() == userInfo.getId()) {
                            it.remove();
                            break;
                        }
                    }
                }
            }
        }
        sharedPreferences.edit().putString(Constants.LOCK_PWD, new Gson().toJson(lockPwds)).apply();
    }



    public void getCurrentUser() {
        String url = UrlHelper.getUrl("/ums/users/current");
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("getcurrentUser", response.toString());
                        UserInfo userInfo = new Gson().fromJson(response.toString(), new TypeToken<UserInfo>() {
                        }.getType());
                        BaseApplication.getInstance().setUserInfo(userInfo);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
//                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };
        BaseApplication.getRequestQueue().add(stringRequest);
    }



    String versionName;

    //获取app当前版本的方法
    public String getVersionName(){
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(),0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    //同步应用程序当前版本的cookie
    public void synVersionNameCookie(Context context){
        String app_channel = getAppMetaData(activity, "UMENG_CHANNEL");
        Lg.e("app_channel",app_channel);
        CookieSyncManager.createInstance(context);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(Constants.HOME_PAGE,"versionNo="+getVersionName());
        cookieManager.setCookie(Constants.HOME_PAGE,"app_channel="+app_channel);
        CookieSyncManager.getInstance().sync();
    }




    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     * 获取友盟的渠道号
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }


    /**
     * 当软键盘没有关闭时，关闭软键盘
     */
    public void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }




}
