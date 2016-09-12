package com.jia16.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huangjun on 16/8/18.
 */
public class CommonUtil {
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
     * 关闭软键盘
     */
    public static void closeKey(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
