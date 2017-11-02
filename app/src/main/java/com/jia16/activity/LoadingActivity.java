package com.jia16.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.LockPwd;
import com.jia16.bean.UserInfo;
import com.jia16.bean.Version;
import com.jia16.service.DownloadService;
import com.jia16.util.AlertUtil;
import com.jia16.util.Constants;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by huangjun on 16/8/17.
 */
public class LoadingActivity extends BaseActivity {

    boolean isFirstRun = true;

    /**
     * 获取应用版本号
     */
    public int getVerCode() {
        int versionCode = 0;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(),
                    0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

//        if(BaseApplication.getInstance().isLogined()){
//            getCurrentUser();
//        }

        if (isFirstRun) {
            //第一次安装使用显示欢迎、指引页
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            checkUpdate();
        }
//        initGesture();


        //shangjing修改
        //卓金服H5页面唤醒嘉石榴app获取参数
        Uri uridata = this.getIntent().getData();
        if (uridata != null) {
            String url = Constants.HOME_PAGE + uridata.toString().substring(8);
            Lg.e("uridata.........", uridata);//jia16://#!newdetail&userid=15&subjectid=17&canVoucher=n&type=P2P_LOAN]
            Lg.e("url......", url);//https://app.jia16.com/#!newdetail&userid=15&subjectid=17&canVoucher=n&type=P2P_LOAN
            //将数据存储到全局变量中
            BaseApplication.getInstance().urlData = url;
        } else {
            BaseApplication.getInstance().urlData = null;
        }

    }

    private void checkUpdate() {
        String url = "/apk/android_version.json";
        Lg.e("url====", UrlHelper.getUrl(url));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlHelper.getUrl(url), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Version version = new Gson().fromJson(response, Version.class);
                if (version != null) {
                    final String versionName = version.getVersionName();
                    String content = version.getDescr();
                    final String url = version.getUrl();
                    String ifUpdate = version.getIfUpdate();

//                    final String url = "http://apk.hiapk.com/web/api.do?qt=8051&id=723";
                    if (version.getVersionId() > getVerCode()) {//当前版本有更新
                        AlertDialog dialog = AlertUtil.showUpdateDialog(LoadingActivity.this, versionName == null ? "" : versionName + "新版本更新", content == null ? "" : content, new AlertUtil.DialogListener() {
                            @Override
                            public void onLeftClick(AlertDialog dlg) {
                                initGesture();
                            }

                            @Override
                            public void onRightClick(AlertDialog dlg) {
                                startUpdateService(LoadingActivity.this, url, versionName, false);
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
                        }, "yes".equals(ifUpdate) ? true : false);
                        dialog.setCanceledOnTouchOutside(false);
                    } else {
                        initGesture();
                    }
                } else {
                    initGesture();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                initGesture();
            }
        });
        BaseApplication.getRequestQueue().add(stringRequest);
    }

    private static Intent updateIntent;

    /**
     * 开启下载服务
     *
     * @param url
     */
    private static void startUpdateService(Activity context, String url, String versionName, boolean forceFlag) {

        updateIntent = new Intent(context, DownloadService.class);
        updateIntent.putExtra("url", url);
        updateIntent.putExtra("forceFlag", forceFlag);
        //标题内容
        updateIntent.putExtra("versionName", versionName);
        context.startService(updateIntent);
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String lockPwdstr = sharedPreferences.getString(Constants.LOCK_PWD, "");
            if (!TextUtils.isEmpty(lockPwdstr)) {//已经保存过手势密码
                List<LockPwd> lockPwds = new Gson().fromJson(lockPwdstr, new TypeToken<List<LockPwd>>() {
                }.getType());
                if (lockPwds != null && lockPwds.size() > 0) {
                    boolean matched = false;
                    UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
                    if (userInfo != null) {//如果用户都未登录 那么不展示手势密码解锁
                        for (LockPwd lockPwd : lockPwds) {
                            if (lockPwd != null && lockPwd.getUserId() == userInfo.getId()) {
                                matched = true;
                                if ("2".equals(sharedPreferences.getString(Constants.GESTURE_STATUS, ""))) {
                                    intentToHome();
                                } else {
                                    Intent intent = new Intent(LoadingActivity.this, UnlockGesturePasswordActivity.class);
                                    intent.putExtra("lockPwd", lockPwd);
                                    startActivity(intent);
                                    finish();
                                }
                                break;
                            }
                        }
                    } else {
                        intentToHome();
                    }
                    if (!matched) {//当前用户未设置手势密码
                        intentToHome();
                    }
                } else {
                    intentToHome();
                }
            } else {

                intentToHome();
            }
        }
    };


    private void initGesture() {
        handler.postDelayed(runnable, 1000);
    }

    private void intentToHome() {//清除cookie
        removeCookie();
        startActivity(new Intent(LoadingActivity.this, WebViewActivity.class));
        finish();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uridata = this.getIntent().getData();
        if (uridata != null) {
            String url = Constants.HOME_PAGE + uridata.toString().substring(8);
            BaseApplication.getInstance().urlData = url;
        } else {
            BaseApplication.getInstance().urlData = null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.setDebugMode(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
