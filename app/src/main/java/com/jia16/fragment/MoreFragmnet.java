package com.jia16.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.activity.LoginActivity;
import com.jia16.activity.account.MyAccountActivity;
import com.jia16.base.BaseApplication;
import com.jia16.base.BaseFragment;
import com.jia16.bean.UserInfo;
import com.jia16.more.advicescenter.AdvicesCenterActivity;
import com.jia16.more.helpercenter.HelperCenterActivity;
import com.jia16.more.AboutMeActivity;
import com.jia16.more.FeedBackActivity;
import com.jia16.more.invitefriends.InviteFriendsActivity;
import com.jia16.more.mywelfare.MyWelfareActivity;
import com.jia16.util.AlertUtil;
import com.jia16.util.Constants;
import com.jia16.util.DMConstant;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.jia16.web.WebActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更多 - ---导航界面
 */
public class MoreFragmnet extends BaseFragment implements View.OnClickListener {

    private static final int MOREWELFARE = 1002;//是从我的福利跳转到登录
    private static final int INVITEFRIEND = 1003;//是从邀请好友跳转到登录
    private BaseApplication mContext;
    private LayoutInflater inflater;
    /**
     * 九方格子布局
     */
    private GridView mGvView;

    //gridview的名称
    String[] names = new String[]{"消息中心", "邀请好友", "我的福利", "活动中心", "帮助中心", "我要反馈",
            "联系我们", "关于嘉石榴",""};
    //gridview的图标
    int[] icons = new int[]{R.drawable.news, R.drawable.friends, R.drawable.reward,
            R.drawable.activity, R.drawable.help, R.drawable.back
            , R.drawable.call, R.drawable.about,0};
    private TextView mTitleText;

    /**
     * 如果用户还没有登录，那么就显示（请登录后查看个人账户）
     */
    private TextView mTvTextLogin;
    private LinearLayout mllTextLogin;
    /**
     * 点击进入我的账户的线性布局
     */
    private LinearLayout mllUser;
    /**
     * 登录后的返回码
     */
    private int AccountRequestCode = 1001;
    private LinearLayout mllMyContent; //全部内容的布局
    private View noNetWorkLayout;//没有网络时显示
    private View mPatentView;
    private ProgressBar mPbProgress;//安全等级的进度条
    private UserInfo userInfo;
    private TextView mTvMobileDesc;//安全等级的描述


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_more_dddd, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //同步应用程序当前版本的cookie
        synVersionNameCookie(getActivity());

        mPatentView = getView();

        if(BaseApplication.getInstance().isLogined()){
            getCurrentUser();
        }


        //隐藏返回键按钮
        ImageView mBtnBack = (ImageView) mPatentView.findViewById(R.id.btn_back);
        mBtnBack.setVisibility(View.GONE);
        //设置标题
        mTitleText = (TextView) mPatentView.findViewById(R.id.title_text);
        mTitleText.setText("更多");


        //如果用户还没有登录，那么就显示（请登录后查看个人账户）
        mTvTextLogin = (TextView) mPatentView.findViewById(R.id.tv_text_login);
        //加粗字体
        TextPaint paint = mTvTextLogin.getPaint();
        paint.setFakeBoldText(true);
        mllTextLogin = (LinearLayout) mPatentView.findViewById(R.id.ll_text_login);



        //点击进入我的账户的线性布局
        mllUser = (LinearLayout) mPatentView.findViewById(R.id.ll_user);
        mllUser.setOnClickListener(this);

        //全部内容的布局
        mllMyContent = (LinearLayout) mPatentView.findViewById(R.id.ll_my_content);
        noNetWorkLayout = mPatentView.findViewById(R.id.noNetworkLayout);//没有网络时显示
        noNetWorkLayout.setOnClickListener(this);

        //九方格子布局
        mGvView = (GridView) mPatentView.findViewById(R.id.gv_view);
        mGvView.setAdapter(new MoreAdapter());
        mGvView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent;
                switch (position){
                    case 0://消息中心
                        intent=new Intent(getActivity(),AdvicesCenterActivity.class);
                        startActivity(intent);
                    break;

                    case 1://邀请好友
                        //打开邀请好友前检查用户是否登录，如果没有，那么久跳转到登录界面
                        if(BaseApplication.getInstance().isLogined()){
                            intent=new Intent(getActivity(),InviteFriendsActivity.class);
                            intent.putExtra("userId",userInfo.getId());
                            startActivity(intent);
                        }else {
                            intent=new Intent(getActivity(),LoginActivity.class);
                            intent.putExtra("isInviteFriend",true);
                            startActivityForResult(intent,INVITEFRIEND);
                        }
                    break;

                    case 2://我的福利
                        //查看我的福利之前，检查用户是否登录，如果没有那么就跳转到登录界面
                        if(BaseApplication.getInstance().isLogined()){
                            //已经登录，跳转到我的福利
                            intent=new Intent(getActivity(), MyWelfareActivity.class);
                            intent.putExtra("userId",userInfo.getId());
                            startActivity(intent);
                        }else {
                            intent=new Intent(getActivity(),LoginActivity.class);
                            intent.putExtra("isMyWelfare",true);
                            startActivityForResult(intent,MOREWELFARE);
                        }
                    break;


                    case 3:
//                        intent=new Intent(getActivity(), WebActivity.class);
//                        intent.putExtra("linkUrl", "https://app.jia16.com/#!activityCenter");
//                        intent.putExtra("title","活动中心");
//                        startActivity(intent);

                    break;


                    case 4://帮助中心
                        intent=new Intent(getActivity(),HelperCenterActivity.class);
                        startActivity(intent);
                    break;

                    case 5://意见反馈
                        intent=new Intent(getActivity(), FeedBackActivity.class);
                        startActivity(intent);
                        break;

                    case 6://联系我们
                    intent=new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("linkUrl", DMConstant.H5Url.MORE_CALL_MY);
                    intent.putExtra("title","联系我们");
                    startActivity(intent);
                    break;

                    case 7://关于嘉石榴about me
                        intent=new Intent(getActivity(), AboutMeActivity.class);
                        startActivity(intent);
                    break;

                    default:
                        break;
                }
            }
        });


        //设置用户是否登录，登录了就获取用户信息
        if(BaseApplication.getInstance().isLogined()){
            if(!isOnRefresh){
                showLoadingDialog();
            }
            getCurrentUser();
        }else {
            mllMyContent.setVisibility(View.VISIBLE);
            noNetWorkLayout.setVisibility(View.GONE);
        }


        //安全等级的进度条
        mPbProgress = (ProgressBar) mPatentView.findViewById(R.id.pb_progress);
        //安全等级的描述
        mTvMobileDesc = (TextView) mPatentView.findViewById(R.id.tv_mobile_desc);


    }

    private boolean isOnRefresh;

    /**
     * 请求当前用户的接口，获取用户的信息
     */
    public void getCurrentUser() {
        String url = UrlHelper.getUrl("/ums/users/current");
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        //请求数据成功，那么久隐藏加载中的布局
                        dimissLoadingDialog();

                        mllMyContent.setVisibility(View.VISIBLE);
                        noNetWorkLayout.setVisibility(View.GONE);

                        Lg.e("getcurrentUser", response.toString());
                        UserInfo userInfo = new Gson().fromJson(response.toString(), new TypeToken<UserInfo>() {
                        }.getType());
                        BaseApplication.getInstance().setUserInfo(userInfo);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);

                dimissLoadingDialog();
                mllMyContent.setVisibility(View.INVISIBLE);
                noNetWorkLayout.setVisibility(View.VISIBLE);

                AlertUtil.showOneBtnDialog(getActivity(), "获取数据失败", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("CSRF-TOKEN", BaseApplication.getInstance().sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", BaseApplication.getInstance().sharedPreferences.getString("Cookie", ""));
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        BaseApplication.getRequestQueue().add(stringRequest);
    }


    @Override
    public void onClick(View view) {
        //防止多次点击
        if (checkClick(view.getId())) {
            Intent intent;
            switch (view.getId()) {
                case R.id.ll_user://点击进入我的账户的线性布局
                    if (!BaseApplication.getInstance().isLogined()) {
                        //表示没有登录，那么就跳转到登录界面
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivityForResult(intent, AccountRequestCode);
                    } else {
                        //表示已经登录，那么就跳转到我的账户界面
                        intent = new Intent(getActivity(), MyAccountActivity.class);
                        startActivity(intent);
                    }
                    break;

                case R.id.noNetworkLayout:
                    mPatentView.findViewById(R.id.net_error_content).setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPatentView.findViewById(R.id.net_error_content).setVisibility(View.VISIBLE);
                        }
                    },300);
                    //重新请求网络，获取数据
                    getCurrentUser();
                break;

                default:
                    break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent;
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == AccountRequestCode) {
                //登录后返回到我的账户界面
                intent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intent);
            }else if(requestCode==MOREWELFARE){
                int userid = data.getIntExtra("userid", 0);
                //是从我的福利跳转到登录，登录成功后就跳转到我的福利界面
                intent=new Intent(getActivity(), MyWelfareActivity.class);
                intent.putExtra("userId",userid);
                Lg.e("userid",userid);
                startActivity(intent);
            }else if(requestCode==INVITEFRIEND){
                int userid = data.getIntExtra("userid", 0);
                //是从邀请好友跳转到登录的，登录成功后就跳转到邀请好友界面
                intent=new Intent(getActivity(),InviteFriendsActivity.class);
                intent.putExtra("userId",userid);
                Lg.e("userid",userid);
                startActivity(intent);
            }
        }
    }


    //gridview的数据适配器
    private class MoreAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            //显示gridview里面的每一个item
            View views = View.inflate(getActivity(), R.layout.item_morefragment, null);
            ImageView mIvImage = (ImageView) views.findViewById(R.id.iv_image);
            TextView mIvDescItem = (TextView) views.findViewById(R.id.iv_desc_item);
            if(position!=8){
                mIvImage.setImageResource(icons[position]);
                mIvDescItem.setText(names[position]);
            }else {
                mIvImage.setVisibility(View.INVISIBLE);
                mIvDescItem.setText(names[position]);
            }
            return views;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {

        //判断用户是否已经登录，那么看是否隐藏与显示布局
        checkIsLogin();

        //显示安全等级的方法
        showProgress();
        super.onResume();
    }

    /**
     * 显示安全等级的方法
     */
    private void showProgress() {
        userInfo = BaseApplication.getInstance().getUserInfo();
        if(userInfo!=null){
            UserInfo.CertificationBean certification = userInfo.getCertification();
            List<UserInfo.BankCardsBean> bankCards = userInfo.getBankCards();
            if(certification==null){
                //表示还没有绑定银行卡那么安全等级为 低，进度条颜色为红色
                mPbProgress.setProgress(33);
                //设置安全等级
                mTvMobileDesc.setText("低");
                //设置进度条颜色
                Drawable drawable = getResources().getDrawable(R.drawable.progress_horizontal_di);
                mPbProgress.setProgressDrawable(drawable);
            }else if(certification!=null&&bankCards.size()==0){
                //表示还没有绑定银行卡那么安全等级为 中，进度条颜色为黄色
                mPbProgress.setProgress(67);
                //设置安全等级
                mTvMobileDesc.setText("中");
                //设置进度条颜色
                Drawable drawable = getResources().getDrawable(R.drawable.progress_horizontal_zhong);
                mPbProgress.setProgressDrawable(drawable);
            }else if(certification!=null&&bankCards.size()!=0){
                //表示还没有绑定银行卡那么安全等级为 中，进度条颜色为黄色
                mPbProgress.setProgress(100);
                //设置安全等级
                mTvMobileDesc.setText("高");
                //设置进度条颜色
                Drawable drawable = getResources().getDrawable(R.drawable.progress_horizontal);
                mPbProgress.setProgressDrawable(drawable);
            }

        }
    }

    /**
     * 判断用户是否已经登录，那么看是否隐藏与显示布局
     */
    private void checkIsLogin() {
        if (!BaseApplication.getInstance().isLogined()) {
            mllTextLogin.setVisibility(View.GONE);
            mTvTextLogin.setText("请登录后查看个人账户");
            mTvTextLogin.setTextSize(18);
        } else {
            mllTextLogin.setVisibility(View.VISIBLE);
            mTvTextLogin.setText("嘉石榴忠实用户");
            mTvTextLogin.setTextSize(15);
        }
    }


}
