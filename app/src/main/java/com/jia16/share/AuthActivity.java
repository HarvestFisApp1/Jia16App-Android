package com.jia16.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.jia16.R;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.util.ArrayList;

/**
 * Created by wangfei on 16/11/9.
 */
public class AuthActivity extends Activity{
    private ListView listView;
    private AuthAdapter shareAdapter;
    public ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();
    private SHARE_MEDIA[] list = {SHARE_MEDIA.QQ,SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN,
            SHARE_MEDIA.FACEBOOK,SHARE_MEDIA.TWITTER,SHARE_MEDIA.LINKEDIN,SHARE_MEDIA.DOUBAN,SHARE_MEDIA.RENREN,SHARE_MEDIA.KAKAO};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umeng_auth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.umeng_blue));

        }
        listView = (ListView) findViewById(R.id.list);
        initPlatforms();
        shareAdapter  = new AuthAdapter(this,platforms);
        listView.setAdapter(shareAdapter);

        ((TextView)findViewById(R.id.umeng_title)).setText(R.string.umeng_auth_title);
        findViewById(R.id.umeng_back).setVisibility(View.VISIBLE);
        findViewById(R.id.umeng_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initPlatforms(){
        platforms.clear();
        for (SHARE_MEDIA e :list) {
            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())){
                platforms.add(e.toSnsPlatform());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
