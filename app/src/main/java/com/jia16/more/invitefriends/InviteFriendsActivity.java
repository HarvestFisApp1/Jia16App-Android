package com.jia16.more.invitefriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.more.helpercenter.MyInvestMentFragmentAdapter;
import com.jia16.util.AlertUtil;
import com.jia16.util.Constants;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更多界面-----邀请好友界面
 */
public class InviteFriendsActivity extends BaseActivity {


    private FrameLayout mInvesterFrame; //邀请好友
    private FrameLayout mCommonFrame;//推荐详情
    private FrameLayout mMobileFrame;//活动规则

    private TextView mInvesterTv;

    private TextView mCommonTv;

    private TextView mMobileTv;

    private View mInvesterLine;

    private View mCommonLine;

    private View mMobileLine;

    private ViewPager mHelperViewPager;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    private int userId;

    private TextView mTvTotalMoney;//累计所赚金额
    private TextView mTvFriendRegister;//邀请好友注册
    private TextView mTvSuccessInvite;//好友成功投资


    /**
     * 添加友盟分享
     */
    private UMShareListener mShareListener;
    public ShareAction mShareAction;
    private BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        mShareListener = new CustomShareListener(this);
        /**
         * 添加友盟分享
         */
        mShareAction = new ShareAction(InviteFriendsActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ)
                .addButton("umeng_sharebutton_custom", "umeng_sharebutton_custom", "umeng_socialize_menu_default", "umeng_socialize_menu_default")
                .setShareboardclickCallback(shareBoardlistener);




        //初始化布局
        initView();
        //绑定数据
        initData();

        registerceiver();
    }

    /**
     * 推荐好友界面发送过来的广播
     */
    private void registerceiver() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("open_shar");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        if(receiver==null){
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //开启分享面板
                            mShareAction.open();
                        }
                    },0);
                }
            };
        }
        registerReceiver(receiver,intentFilter);
    }



    //增加自定义分享按钮
    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media == null) {
                if (snsPlatform.mKeyword.equals("umeng_sharebutton_custom")) {
                    Toast.makeText(InviteFriendsActivity.this, "复制链接成功", Toast.LENGTH_LONG).show();
                    // 得到剪贴板管理器
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText("http://www.baidu.com");
                }

            } else {
                new ShareAction(InviteFriendsActivity.this).setPlatform(share_media)
                        .withText("分享内容")  //设置分享内容
                        .withTitle("分享title")//设置分享title
                        .withTargetUrl("http://www.baidu.com")//设置分享链接
                        //https://app.jia16.com/mjia/dist/page/more/0.0.2/images/news.png
                //Constants.HOME_PAGE+sharImage
                .withMedia(new UMImage(InviteFriendsActivity.this,"https://app.jia16.com/mjia/dist/page/more/0.0.2/images/news.png"))//设置分享的图片
                        .setCallback(mShareListener)
                        .share();
            }
        }
    };



    /**
     * 初始化布局
     */
    public void initView() {

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);

        ((TextView)findViewById(R.id.title_text)).setText("邀请好友");

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHelperViewPager = (ViewPager) findViewById(R.id.helper_view_pager);

        mInvesterFrame = (FrameLayout) findViewById(R.id.invester_frame);
        mCommonFrame = (FrameLayout) findViewById(R.id.common_frame);
        mMobileFrame = (FrameLayout) findViewById(R.id.mobile_frame);

        mInvesterFrame.setOnClickListener(this);
        mCommonFrame.setOnClickListener(this);
        mMobileFrame.setOnClickListener(this);

        mInvesterTv = (TextView) findViewById(R.id.invester_tv);
        mCommonTv = (TextView) findViewById(R.id.common_tv);
        mMobileTv = (TextView) findViewById(R.id.mobile_tv);

        mInvesterLine = findViewById(R.id.invester_line);
        mCommonLine = findViewById(R.id.common_line);
        mMobileLine = findViewById(R.id.mobile_line);

        //初始化邀请好友人数，好友成功投资的人数，所赚取的金额的布局
        mTvTotalMoney = (TextView) findViewById(R.id.tv_total_money);//累计所赚金额
        mTvFriendRegister = (TextView) findViewById(R.id.tv_friend_register);//邀请好友注册
        mTvSuccessInvite = (TextView) findViewById(R.id.tv_success_invest);//好友成功投资



        initViewPager();

        //请求接口获取 邀请好友人数，好友成功投资的人数，所赚取的金额
        getInviteFriendInformation();
    }

    /**
     * 请求接口获取 邀请好友人数，好友成功投资的人数，所赚取的金额
     */
    private void getInviteFriendInformation() {

        String url="/api/users/"+userId+"/recommend-statistics";
        url= UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("response--获取邀请好友的信息",response.toString());
                        String totalInvestRecommended = response.optString("totalInvestRecommended");
                        Lg.e("好友成功投资",totalInvestRecommended);
                        mTvSuccessInvite.setText(totalInvestRecommended+"人");

                        String totalRecommended = response.optString("totalRecommended");
                        Lg.e("邀请好友注册",totalRecommended);
                        mTvFriendRegister.setText(totalRecommended+"人");

                        try {
                            String totalReward = response.optString("totalReward");
                            JSONObject jsonObject=new JSONObject(totalReward);
                            String amount = jsonObject.optString("amount");
                            Lg.e("金额。。。。。。。。。",amount);
                            mTvTotalMoney.setText(amount+"元");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        BaseApplication.getRequestQueue().add(jsonObjectRequest);
    }


    /**
     * 绑定数据
     */
    private void initData() {


    }

    private void initViewPager() {
        fragments.add(new InviteFragment());
        fragments.add(new RecommendDetailFragmnet());
        fragments.add(new ActiviRuleFragment());

        MyInvestMentFragmentAdapter adapter =
                new MyInvestMentFragmentAdapter(fragments, this.getSupportFragmentManager(), mHelperViewPager);
        adapter.setOnExtraPageChangeListener(new MyInvestMentFragmentAdapter.OnExtraPageChangeListener() {
            public void onExtraPageSelected(int i) {
                switchBtnList(i);
            }
        });
    }

    private void switchBtnList(int index) {
        switch (index) {
            case 0:
                mInvesterTv.setTextColor(getResources().getColor(R.color.main_color));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.text_gray));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                break;
            case 1:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.main_color));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));
                break;
            case 2:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.text_gray));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.main_color));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));
                break;

            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invester_frame: //邀请好友
                mHelperViewPager.setCurrentItem(0);
                break;
            case R.id.common_frame: //推荐详情
                mHelperViewPager.setCurrentItem(1);
                break;
            case R.id.mobile_frame: //活动规则
                mHelperViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }



    /**
     * 添加友盟分享的监听
     */
    private static class CustomShareListener implements UMShareListener {

        private WeakReference<InviteFriendsActivity> mActivity;

        private CustomShareListener(InviteFriendsActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.WEIXIN && platform != SHARE_MEDIA.WEIXIN_CIRCLE

                        && platform != SHARE_MEDIA.QQ) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.WEIXIN
                    && platform != SHARE_MEDIA.WEIXIN_CIRCLE
                    && platform != SHARE_MEDIA.QQ) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
