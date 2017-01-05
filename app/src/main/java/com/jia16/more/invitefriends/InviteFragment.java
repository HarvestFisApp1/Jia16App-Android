package com.jia16.more.invitefriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.base.BaseFragment;

/**
 * 更多界面 - ---邀请好友 --邀请好友
 */
public class InviteFragment extends BaseFragment {


    private BaseApplication mContext;
    private LayoutInflater inflater;
    private Button mBtShar;


    /**
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=BaseApplication.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater=inflater;
        return inflater.inflate(R.layout.fragment_invite, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View mPatentView = getView();
        mBtShar = (Button) mPatentView.findViewById(R.id.bt_shar);

        mBtShar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //点击分享按钮，就发送广播，开启分享
                Intent intent=new Intent();
                intent.setAction("open_shar");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                getActivity().sendBroadcast(intent);
            }
        });

    }




}


