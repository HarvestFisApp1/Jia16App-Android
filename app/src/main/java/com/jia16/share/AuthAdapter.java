package com.jia16.share;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jia16.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wangfei on 16/11/9.
 */
public class AuthAdapter extends BaseAdapter {
    private ArrayList<SnsPlatform> list;
    private Context context;
    public AuthAdapter(Context context, ArrayList<SnsPlatform> list){
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView ==null){
            convertView =  LayoutInflater.from(context).inflate(R.layout.app_authadapter, null);
        }
        final boolean isauth = UMShareAPI.get(context).isAuthorize((Activity) context,list.get(position).mPlatform);
        ImageView img = (ImageView) convertView.findViewById(R.id.adapter_image);
        img.setImageResource(ResContainer.getResourceId(context,"drawable",list.get(position).mIcon));
        TextView tv = (TextView)convertView.findViewById(R.id.name);
        tv.setText(ResContainer.getResourceId(context,"string",list.get(position).mShowWord));
         TextView authBtn = (TextView) convertView.findViewById(R.id.auth_button);
        if (isauth){
            authBtn.setText("删除授权");
        }else {
            authBtn.setText("授权");
        }
       authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isauth){

                    UMShareAPI.get(context).deleteOauth((Activity) context,list.get(position).mPlatform,authListener);

                }else {
                    UMShareAPI.get(context).doOauthVerify((Activity) context,list.get(position).mPlatform,authListener);

                }
            }
        });
        if (position == list.size()-1){
            convertView.findViewById(R.id.divider).setVisibility(View.GONE);
        }else {
            convertView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        }
//
        return convertView;
    }
    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(context,"成功了",Toast.LENGTH_LONG).show();
            notifyDataSetChanged();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(context,"失败："+t.getMessage(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(context,"取消了",Toast.LENGTH_LONG).show();

        }
    };
}

