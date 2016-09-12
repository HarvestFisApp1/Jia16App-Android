package com.jia16.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.jia16.R;
import com.jia16.activity.WebViewActivity;
import com.jia16.util.Lg;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * app更新 下载服务
 */
public class DownloadService extends Service {
    /**
     * 下载成功
     */
    public static final int SUCC_DOWNLOAD = 2;
    public static final int FAIL_DOWNLOAD = 4;
    public static final int PROGRESS_DOWNLOAD = 3;
    public static final int INSTALL_REQUEST_CODE = 100;

    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private File mTempFile = null;
    private boolean mCancelUpdate = false;
    private MyHandler mHandler;
    private int mDownloadPrecent = 0;
    private RemoteViews mRemotevView;
    private int mNotificationId = 1234;
    private static final String TAG = "DownloadService";
    /**
     * 是否强制更新
     */
    private boolean forceFlag;
    public static String BROADCAST_CLOSE = "exit_app";

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "onStart");
        super.onStart(intent, startId);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.icon = android.R.drawable.stat_sys_download;

        String versionName = intent.getStringExtra("versionName");
        forceFlag = intent.getBooleanExtra("forceFlag", false);
        mNotification.tickerText = getString(R.string.app_name) + (versionName == null ? "" : versionName) + "版本更新";
        mNotification.icon = R.mipmap.ic_launcher;
        mNotification.when = System.currentTimeMillis();
        mNotification.defaults = Notification.DEFAULT_LIGHTS;
        // 设置任务栏中下载进程显示的views
        mRemotevView = new RemoteViews(getPackageName(), R.layout.common_remote_view_layout);

        mRemotevView.setTextViewText(R.id.tv_notify_title, versionName + "版本更新");
        mNotification.contentView = mRemotevView;

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, WebViewActivity.class), 0);
        //设置在nority列表里的该norifycation得显示情况
        mNotification.setLatestEventInfo(this, "", "", contentIntent);

        mNotificationManager.notify(mNotificationId, mNotification);

        mHandler = new MyHandler(Looper.myLooper(), this);

        Message message = mHandler.obtainMessage(PROGRESS_DOWNLOAD, 0);
        mHandler.sendMessage(message);

        downFile(intent.getStringExtra("url"));
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        stopSelf();
        mCancelUpdate = true;
        Log.d(TAG, "onDestroy");
        if (mNotificationManager != null) {
            mNotificationManager.cancel(mNotificationId);
        }
    }


    private void downFile(final String url) {
        Log.d(TAG, "downFile");
        ThreadsPool.executeOnExecutor(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                BufferedInputStream bufferedInputStream = null;
                FileOutputStream fileOutputStream = null;
                BufferedOutputStream bufferedOutputStream = null;
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);
                    Lg.d("下载地址", url);
                    HttpResponse response = httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    inputStream = entity.getContent();
                    if (inputStream != null) {

                        File rootFile = new File(
                                Environment.getExternalStorageDirectory(),
                                "/jia16");
                        if (!rootFile.exists() && !rootFile.isDirectory())
                            rootFile.mkdirs();

                        mTempFile = new File(
                                Environment.getExternalStorageDirectory(),
                                "/jia16/jia16.apk");
                        //清除旧版本
                        if (mTempFile.exists())
                            mTempFile.delete();

                        mTempFile.createNewFile();
                        bufferedInputStream = new BufferedInputStream(inputStream);
                        fileOutputStream = new FileOutputStream(mTempFile);
                        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        int read = -1;
                        long count = 0;
                        int percent = 0;
                        byte[] buffer = new byte[1024];
                        while ((read = bufferedInputStream.read(buffer)) != -1 && !mCancelUpdate) {
                            bufferedOutputStream.write(buffer, 0, read);
                            count += read;
                            percent = (int) (((double) count / length) * 100);
                            //每增加5%就更新进度一次
                            Lg.e("下载进度", percent);
                            if (percent - mDownloadPrecent >= 5) {
                                mDownloadPrecent = percent;
                                Message message = mHandler.obtainMessage(PROGRESS_DOWNLOAD,
                                        percent);
                                mHandler.sendMessage(message);
                            }
                        }
                        closeStream(inputStream, bufferedInputStream, fileOutputStream, bufferedOutputStream);
                    }
                    if (!mCancelUpdate) {
                        //没有取消
                        Message message = mHandler.obtainMessage(SUCC_DOWNLOAD, mTempFile);
                        mHandler.sendMessage(message);
                    } else {
                        mTempFile.delete();
                    }
                } catch (Exception e) {
                    Message message = mHandler.obtainMessage(FAIL_DOWNLOAD, "下载更新文件失败");
                    mHandler.sendMessage(message);
                    if (inputStream != null && bufferedInputStream != null && fileOutputStream != null && bufferedOutputStream != null)
                        closeStream(inputStream, bufferedInputStream, fileOutputStream, bufferedOutputStream);
                }
            }
        });

    }

    private void closeStream(InputStream is, BufferedInputStream bufferedInputStream, FileOutputStream fileOutputStream, BufferedOutputStream bufferedOutputStream) {
        try {
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            is.close();
            bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void install(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
        if (forceFlag) {

            Intent intentClose = new Intent(BROADCAST_CLOSE);
            intentClose.addCategory(Intent.CATEGORY_DEFAULT);
            sendBroadcast(intentClose);
        }
    }

    class MyHandler extends Handler {
        private Context context;

        public MyHandler(Looper looper, Context c) {
            super(looper);
            this.context = c;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(context, msg.obj.toString(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        break;
                    case SUCC_DOWNLOAD:

                        mDownloadPrecent = 0;
                        mNotificationManager.cancel(mNotificationId);
                        install((File) msg.obj, context);

                        stopSelf();
                        break;
                    case PROGRESS_DOWNLOAD:

                        mRemotevView.setTextViewText(R.id.tv_notify_progress, "已下载"
                                + mDownloadPrecent + "%");
                        mRemotevView.setProgressBar(R.id.pb_notify, 100,
                                mDownloadPrecent, false);
                        mNotification.contentView = mRemotevView;
                        mNotificationManager.notify(mNotificationId, mNotification);
                        break;
                    case FAIL_DOWNLOAD:
                        mNotificationManager.cancel(mNotificationId);
                        stopSelf();
                        break;
                }
            }
        }
    }

}
