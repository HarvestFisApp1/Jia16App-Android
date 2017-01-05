package com.jia16.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.jia16.R;
import com.jia16.util.Constants;
import com.jia16.util.Lg;
import com.jia16.view.LoadingDialog;

/**
 * @author jiaohongyun
 * @date 2015年5月27日
 */
public class BaseFragment extends Fragment {
	private LoadingDialog loadingDialog;

	/**
	 * 保存上一次点击时间
	 */
	private SparseArray<Long> lastClickTimes;

	//public HttpTask mHttpTask;


	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		loadingDialog = new LoadingDialog(getActivity());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lastClickTimes = new SparseArray<Long>();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		lastClickTimes = null;
//		if (mHttpTask != null) {
//			mHttpTask.setCanceled(true);
//		}
	}

	/**
	 * 检查是否可执行点击操作
	 * 防重复点击
	 *
	 * @return 返回true则可执行
	 */
	protected boolean checkClick(int id) {
		Long lastTime = lastClickTimes.get(id);
		Long thisTime = System.currentTimeMillis();
		lastClickTimes.put(id, thisTime);
		if (lastTime != null && thisTime - lastTime < 800) {
			//快速双击，第二次不处理
			//            Toast.makeText(this, "亲，你点的太快了，请慢点儿！", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

//	//获取webview网页title的方法
//	public void getWebviewTitle(String url, Intent intent) {
//		if(url.contains("title")){
//			String[] strings = url.split("&");
//			for(int i=0;i<strings.length;i++){
//				if(strings[i].contains("title")){
//					String titles = strings[i].split("=")[1];
//					intent.putExtra("title",titles);
//				}
//			}
//		}else {
//			intent.putExtra("autoTitle",true);
//		}
//	}


	public void showLoadingDialog() {
		loadingDialog.show(getString(R.string.loading));
	}

	public void showLoadingDialog(String content) {
		loadingDialog.show(content);
	}

	public void dimissLoadingDialog() {
		loadingDialog.dismiss();
	}





	String versionName;

	//获取app当前版本的方法
	public String getVersionName(){
		try {
			versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionName;

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}



	private CookieManager cookieManager;

	//同步应用程序当前版本的cookie
	public void synVersionNameCookie(Context context){
		String app_channel = getAppMetaData(getActivity(), "UMENG_CHANNEL");
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



}
