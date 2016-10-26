package com.jia16.util;

import com.jia16.base.BaseApplication;

import java.util.HashMap;

/**
 * Created by huangjun on 16/8/16.
 */
public class UrlHelper {
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

//    //线上测试
    public static final String HTTP_SERVER = "114.55.156.98";

    public static final String HTTPS_SERVER = "app.jia16.com";


//http://114.55.156.253/test.html

//    public static final String HTTP_SERVER = "114.55.156.253";
//
//    public static final String HTTPS_SERVER = "114.55.156.253";



//    public static final String HTTPS_SERVER = "10.139.98.226";

//    public static final String HTTPS_SERVER = "test2.jia16.com";

    private static final HashMap<String, String> urlMaps = new HashMap<String, String>();

    /*
     * 通过path构造完整的url请求地址 如果path以http或者https开头 则直接返回path的值。
     * <p/>
     * <p/>
     * 如果是release状态，则使用线上域名 如果是debug状态，则使用打开程序是选择的域名。
     * <p/>
     * release状态和debug状态 是由AppContext.isDebug()的值决定
     * <p/>
     * path：url 会被缓存到hasmap中
     *
     * @param path
     * @return 接口请求地址：
     */
    public static String getUrl(String path) {

        if (path.startsWith(HTTP) || path.startsWith(HTTPS)) {
            if (!BaseApplication.getInstance().isDebug && !path.contains(".jia16.com")) {
                throw new IllegalArgumentException("非debug模式，不允许使用完整接口路径，以防测试地址被发布到正式版本中:" + path);
            } else {
                return path;
            }
        }

        if (urlMaps.containsKey(path)) {
            String url = urlMaps.get(path);
            Lg.v("使用缓存的地址", url);
            return url;
        }

        StringBuilder sb = new StringBuilder();

        if (BaseApplication.getInstance().isDebug) {
            sb.append(HTTP);
            sb.append(HTTP_SERVER);
        } else {
            sb.append(HTTPS);
            sb.append(HTTPS_SERVER);
        }

        if (!path.startsWith("/")) {
            sb.append("/");
        }
        sb.append(path);
        String url = sb.toString();
        urlMaps.put(path, url);

        return url;
    }

    public static void clearUrlCache() {
        urlMaps.clear();
    }
}
