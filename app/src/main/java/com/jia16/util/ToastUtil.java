package com.jia16.util;

import android.content.Context;
import android.widget.Toast;

import com.jia16.base.AppContext;
import com.jia16.base.BaseApplication;

/**
 * @author jiaohongyun
 * @date 2015年7月21日
 */
public class ToastUtil {
    private static ToastUtil util;
    private Toast toast;

    public static ToastUtil getInstant() {
        if (util == null) {
            util = new ToastUtil();
        }
        return util;
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param text
     */
    public void show(Context context, CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }


    /**
     * 显示Toast
     *
     * @param context
     */
    public void show(Context context, int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resId);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
