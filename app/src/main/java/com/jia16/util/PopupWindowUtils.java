package com.jia16.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import com.jia16.base.BaseApplication;

/**
 * PopupWindow ---工具类
 */
public class PopupWindowUtils {

    /**
     * 参数说明 --
     * View--为PopupWindow布局
     * float--为为PopupWindow弹窗左右离屏幕的间距
     */
    public static PopupWindow showPopupWindow(View view,float density) {

        int contentViewPadding = DensityUtil.dip2px(BaseApplication.getInstance(), density);
        view.setPadding(contentViewPadding,0,contentViewPadding,0);
        PopupWindow popupWindow = new PopupWindow(view,
                ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, true);


        popupWindow.setTouchable(true);
        popupWindow.setFocusable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#e0000000"));
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOutsideTouchable(false);


        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        //设置popupWindow显示的位置
        popupWindow.showAtLocation(view, Gravity.CENTER, 0,0);

        // 设置好参数之后再show
       // popupWindow.showAsDropDown(view);
        return popupWindow;
    }
}
