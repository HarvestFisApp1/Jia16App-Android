package com.jia16.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;

/**
 * 应用程序上下文，
 *
 * @author cailiming
 */
public class AppContext {
    public static final String MAIN_APP_PACKAGE = "com.jia16";
    public static final String FRAMEWORK_VERSION = "v1.0";
    private static final Object sLock = new Object();

    public static BaseApplication APP;
    private static boolean sIsDebug = true;


    /**
     * {@link BaseApplication} Only called in Application onCreate
     *
     * @param app
     */
    /* package */
    static void init(BaseApplication app) {

        APP = app;

    }


    /**
     * 是否处于debug模式，debug模式可以选择环境，打印日志
     *
     * @return
     */
    public static boolean isDebug() {
        return sIsDebug;
    }


    /**
     * 设备信息， 包括屏幕分辨率等参数
     *
     * @return
     */
//	public static DeviceInfo getDeviceInfo() {
//		if (sDeviceInfo == null) {
//			synchronized (sLock) {
//				if (sDeviceInfo == null) {
//					sDeviceInfo = new DeviceInfo();
//				}
//			}
//		}
//		return sDeviceInfo;
//	}


    /**
     * @param eventKey
     * @param eventValue 只支持基本数据类型和可序列化对象
     */
    public static void sendLocalEvent(String eventKey, Object eventValue) {
        Intent intent = new Intent(eventKey);
        if (eventValue instanceof Serializable) {
            intent.putExtra(eventKey, (Serializable) eventValue);
        } else if (eventValue instanceof String) {
            intent.putExtra(eventKey, (String) eventValue);
        } else if (eventValue instanceof Long) {
            intent.putExtra(eventKey, (Long) eventValue);
        } else if (eventValue instanceof Integer) {
            intent.putExtra(eventKey, (Integer) eventValue);
        } else if (eventValue instanceof Double) {
            intent.putExtra(eventKey, (Double) eventValue);
        } else if (eventValue instanceof Float) {
            intent.putExtra(eventKey, (Float) eventValue);
        } else if (eventValue instanceof Float) {
            intent.putExtra(eventKey, (Float) eventValue);
        } else if (eventValue instanceof Bundle) {
            intent.putExtras((Bundle) eventValue);
        } else {
            intent.putExtra(eventKey, eventValue == null ? null : eventValue.toString());
        }

        LocalBroadcastManager.getInstance(APP).sendBroadcast(intent);
    }

    public static void regiser(Activity activity, BroadcastReceiver receiver, IntentFilter filter) {
        try {
            LocalBroadcastManager.getInstance(APP).registerReceiver(receiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unRegiser(Activity activity, BroadcastReceiver receiver) {
        try {
            LocalBroadcastManager.getInstance(APP).unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        APP.startActivity(intent);
    }

}
