package com.jia16.util;

import android.util.Log;

import com.jia16.base.BaseApplication;


/**
 * <Pre>
 * 本类不适合在超过5次的循环中使用
 * 例如:
 * for (int i = 0; i< 10; i++) {
 * Lg.d("i", i);
 * }
 * 当循环次数过多的时候，Log会打印不出来！
 * </Pre>
 *
 * @author huangjun
 */
public class Lg {
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;
    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;
    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;
    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;
    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;
    static final String TAG = "debug";

    public static void v(Object... msg) {
        print(VERBOSE, msg);
    }

    public static void d(Object... msg) {
        print(DEBUG, msg);
    }

    public static void i(Object... msg) {
        print(INFO, msg);
    }

    public static void w(Object... msg) {
        print(WARN, msg);
    }

    public static void e(Object... msg) {
        print(ERROR, msg);
    }

    public static void mark(String markStr) {
        print(ERROR, markStr, "标记测试代码，上线前必须删除！！！");
    }

    private static void print(int type, Object... msg) {
//        if (!BaseApplication.getInstance().isDebug) {
//            return;
//        }

        StringBuilder str = new StringBuilder();

        if (msg != null) {
            for (Object obj : msg) {
                str.append("★").append(obj);
            }
            if (str.length() > 0) {
                str.deleteCharAt(0);// 移除第一个五角星
            }
        } else {
            str.append("null");
        }
        try {
            StackTraceElement[] sts = Thread.currentThread().getStackTrace();
            StackTraceElement st = null;
            String tag = null;
            if (sts != null && sts.length > 4) {
                st = sts[4];
                if (st != null) {
                    String fileName = st.getFileName();
                    tag = (fileName == null) ? "Unkown" : fileName.replace(".java", "");
                    str.insert(0, "【" + tag + "." + st.getMethodName() + "() line " + st.getLineNumber() + "】\n>>>[")
                            .append("]");
                }
            }

            // use logcat log
            while (str.length() > 0) {
                switch (type) {
                    case VERBOSE:
                        Log.v(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length()))
                                .toString());
                        break;
                    case DEBUG:
                        Log.d(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length()))
                                .toString());
                        break;
                    case INFO:
                        Log.i(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length()))
                                .toString());
                        break;
                    case WARN:
                        Log.w(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length()))
                                .toString());
                        break;
                    case ERROR:
                        Log.e(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length()))
                                .toString());
                        break;
                    default:
                        break;
                }
                str.delete(0, 2000);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void printStackTrace() {
        if (!BaseApplication.getInstance().isDebug) {
            return;
        }
        try {
            StackTraceElement[] sts = Thread.currentThread().getStackTrace();
            for (StackTraceElement stackTraceElement : sts) {
                Log.e("Log_trace", stackTraceElement.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void printException(String msg, Throwable e) {
        if (!BaseApplication.getInstance().isDebug) {
            return;
        }

        Log.e(TAG + "_", msg, e);
    }
}
