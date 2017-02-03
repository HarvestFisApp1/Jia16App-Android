package com.jia16.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.activity.LoginActivity;
import com.jia16.base.BaseApplication;
import com.jia16.base.BaseFragment;
import com.jia16.bean.Bunner;
import com.jia16.bean.UserInfo;
import com.jia16.home.HomeItemFragmnet;
import com.jia16.more.helpercenter.HelperCenterActivity;
import com.jia16.more.helpercenter.MyInvestMentFragmentAdapter;
import com.jia16.more.invitefriends.InviteFriendsActivity;
import com.jia16.more.mywelfare.MyWelfareActivity;
import com.jia16.util.DensityUtil;
import com.jia16.util.ImageLoadOptions;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.jia16.util.Util;
import com.jia16.web.WebActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 精心优选 - ---导航主界面
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static final int MOREWELFARE = 1002;//是从我的福利跳转到登录
    private static final int INVITEFRIEND = 1003;//是从邀请好友跳转到登录
    private BaseApplication mContext;
    private LayoutInflater inflater;
    private LinearLayout mllMyWelfare;//跳转我的福利
    private LinearLayout mllInviteFriend;//跳转邀请好友
    private LinearLayout mllMobile;//跳转安全保障
    private ViewPager mViewPager;//图片展播的viewpager
    private LinearLayout mllOvalWhite;//白色圆圈的线性布局
    private ImageView mIvOvalRed;//小红点

    private LinearLayout mllMyContent; //全部内容的布局
    private View noNetWorkLayout;//没有网络时显示

    private ArrayList<ImageView> mViewpagerList;

    private TextView mTvWelfareNumber;//我的福利数量

    private int mpointDis;//两个小圆点之间的距离


    //通过handler来实现图片的自动轮播
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //让viewpager选中下一页
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            //Lg.e("当前图片的位置......",mViewPager.getCurrentItem()+1);
            //Lg.e(this,"图片在展博拉.......");
            //重新发消息
            handler.sendEmptyMessageDelayed(0, 5000);
        }
    };
    private UserInfo userInfo;
    private int userId;
    private String homeBunner;
    private HomeAdapter adapter;
    private ArrayList<Bunner> infos;

    private FrameLayout mInvestFrame;
    private ViewPager mHelperViewPager;
    private List<Fragment> fragments1 = new ArrayList<Fragment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //显示加载中的布局
        showLoadingDialog();

        //同步应用程序当前版本的cookie
        synVersionNameCookie(getActivity());
        final View mPatentView = getView();


        new Thread() {
            @Override
            public void run() {
                //获取首页bunner图片
                 homeBunner = getHomeBunner();

                Lg.e("homeBunner......", homeBunner);

                infos = (ArrayList<Bunner>) JsonUtil.parseJsonToList(homeBunner, new TypeToken<List<Bunner>>() {
                }.getType());

                Lg.e("当前位置的。。infos。。。", infos);
                //更新ui
                BaseApplication.mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initView(mPatentView);
                        initDate();
                        Lg.e("获取首页bunner图片", homeBunner);
                        adapter = new HomeAdapter();
                        mViewPager.setAdapter(adapter);

                        mViewPager.setCurrentItem(infos.size() * 8000);
                        handler.sendEmptyMessageDelayed(0, 5000);

                        if (BaseApplication.getInstance().isLogined()) {
                            userInfo = BaseApplication.getInstance().getUserInfo();
                            if (userInfo != null) {
                                userId = userInfo.getId();
                                //获取我的福利数量
                                getMyWelfare();
                                //显示我的福利数量的布局
                                mTvWelfareNumber.setVisibility(View.VISIBLE);
                            }

                        } else {//表示目前还没有登录,那么就隐藏主界面我的福利的数量的布局
                            mTvWelfareNumber.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }.start();

    }

    /**
     * 初始化
     */
    private void initView(View view) {


        mInvestFrame = (FrameLayout) view.findViewById(R.id.invest_frame);
        mInvestFrame.setOnClickListener(this);

        mHelperViewPager = (ViewPager)view.findViewById(R.id.helper_view_pager);


        //全部内容的布局
        mllMyContent = (LinearLayout) view.findViewById(R.id.ll_my_content);
        noNetWorkLayout = view.findViewById(R.id.noNetworkLayout);//没有网络时显示
        noNetWorkLayout.setOnClickListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        RelativeLayout mRlvpContent = (RelativeLayout) view.findViewById(R.id.rl_vp_content);

        //动态设置viewpager的高度
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        float height = (float) (width/2.5);
        params.height=(int) height;
        mViewPager.setLayoutParams(params);

        //动态设置viewpager父布局（相对布局）的高度
        ViewGroup.LayoutParams layoutParams = mRlvpContent.getLayoutParams();
        layoutParams.height=(int) height;
        mRlvpContent.setLayoutParams(layoutParams);


        //我的福利数量
        mTvWelfareNumber = (TextView) view.findViewById(R.id.tv_welfare_number);

        mllMyWelfare = (LinearLayout) view.findViewById(R.id.ll_my_welfare);
        mllMyWelfare.setOnClickListener(this);

        mllInviteFriend = (LinearLayout) view.findViewById(R.id.ll_invite_friend);
        mllInviteFriend.setOnClickListener(this);

        mllMobile = (LinearLayout) view.findViewById(R.id.ll_mobile);
        mllMobile.setOnClickListener(this);
        //白色圆圈的线性布局
        mllOvalWhite = (LinearLayout) view.findViewById(R.id.ll_oval_white);
        //小红点
        mIvOvalRed = (ImageView) view.findViewById(R.id.iv_oval_red);

        //初始化数据
        //initDate();

        initViewPager1();

//        adapter = new HomeAdapter();
//        mViewPager.setAdapter(adapter);


        //给viewpager设置页面变化的监听器
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //position当前的位置
            //positionOffset当前滑动位置的百分比
            //positionOffsetPixels当前位置移动的像素
            //当页面滑动过程中的回调
            @Override//当页面滚动的时候调用的方法
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if ((position % infos.size()) == (infos.size())) {
                    //获取小红点的布局参数
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvOvalRed.getLayoutParams();
                    //将移动中当前位置的左边距赋值给小红点左边距参数（重新设置小红点的左边距）
                    params.leftMargin = 0;
                    //重新设置布局参数
                    mIvOvalRed.setLayoutParams(params);
                } else {
                    //当前小红点离左边的边距=两个小圆点之间的距离*当前滑动位置的百分比
                    int leftMargin = (int) (mpointDis * positionOffset) + (position % infos.size()) * mpointDis;
                    //获取小红点的布局参数
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvOvalRed.getLayoutParams();
                    //将移动中当前位置的左边距赋值给小红点左边距参数（重新设置小红点的左边距）
                    params.leftMargin = leftMargin;
                    //重新设置布局参数
                    mIvOvalRed.setLayoutParams(params);
                }


            }

            //当某个页面被选中的时候调用的方法
            @Override
            public void onPageSelected(int position) {

            }

            //当页面状态改变的时候调用的方法
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //计算两个小圆点的距离
        //小红点移动的距离=小红点移动的百分比*两个点之间的距离
        //小红点移动的距离=第二个小红点右边距-第一个小红点右边距
        //meadure-layout(确定距离)-draw(当activity的oncreate方法执行完毕之后才会走获取控件右边距这个流程)
        //mpointDis = llcontainer.getChildAt(1).getLeft()-llcontainer.getChildAt(0).getLeft();

        //监听layout方法结束的事件,等到确定好了位置之后，再去获取两个圆点之间的距离
        //getViewTreeObserver()获取视图树的方法
        mIvOvalRed.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当layout方法执行完毕之后调用的方法
            @Override
            public void onGlobalLayout() {
                ////当layout方法执行完毕之后调用的方法
                mpointDis = mllOvalWhite.getChildAt(1).getLeft() - mllOvalWhite.getChildAt(0).getLeft();
                System.out.println("两个小点之间的距离:" + mpointDis);
                //获取视图树完毕后就将其移除
                mIvOvalRed.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        //由于ViewPager 没有点击事件，可以通过对VIewPager的setOnTouchListener进行监听已达到实现点击事件的效果
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            int flage = 0 ;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //当手指按下时，那么就停止轮播
                        handler.removeMessages(0);

                        flage = 0 ;
                        break ;
                    case MotionEvent.ACTION_MOVE:
                        flage = 1 ;
                        break ;
                    case  MotionEvent.ACTION_UP :
                        if (flage == 0) {
                            int item = mViewPager.getCurrentItem()%infos.size();
                            if (item == 0) {
                                Intent intent = new Intent(getActivity(), WebActivity.class);
                                intent.putExtra("linkUrl",infos.get(0).getUrl());
                                intent.putExtra("title","公告");
                                getActivity().startActivity(intent);
                            } else if (item == 1) {
                                Intent intent = new Intent(getActivity(), WebActivity.class);
                                intent.putExtra("linkUrl",infos.get(1).getUrl());
                                intent.putExtra("title","公告");
                                getActivity().startActivity(intent);
                            } else if (item == 2) {
                                Intent intent = new Intent(getActivity(), WebActivity.class);
                                intent.putExtra("linkUrl",infos.get(2).getUrl());
                                intent.putExtra("title","公告");
                                getActivity().startActivity(intent);
                            }else if (item == 3) {
                                Intent intent = new Intent(getActivity(), WebActivity.class);
                                intent.putExtra("linkUrl",infos.get(3).getUrl());
                                intent.putExtra("title","公告");
                                getActivity().startActivity(intent);
                            }else if (item == 4) {
                                Intent intent = new Intent(getActivity(), WebActivity.class);
                                intent.putExtra("linkUrl",infos.get(4).getUrl());
                                intent.putExtra("title","公告");
                                getActivity().startActivity(intent);
                            }else if (item == 5) {
                                Intent intent = new Intent(getActivity(), WebActivity.class);
                                intent.putExtra("linkUrl",infos.get(5).getUrl());
                                intent.putExtra("title","公告");
                                getActivity().startActivity(intent);
                            }else if (item == 6) {
                                Intent intent = new Intent(getActivity(), WebActivity.class);
                                intent.putExtra("linkUrl",infos.get(6).getUrl());
                                intent.putExtra("title","公告");
                                getActivity().startActivity(intent);
                            }
                        }
                        //在手指滑动完图片后，能够让轮播图继续自动滚动
                        handler.sendEmptyMessageDelayed(0, 5000);
                        break ;
                }
                return false;
            }
        });
    }


    //初始化数据的方法
    private void initDate() {
        //Lg.e("infos........size", infos.size());
        mViewpagerList = new ArrayList<ImageView>();
        if(infos.size()!=0){
            for (int i = 0; i < infos.size(); i++) {
                ImageView iv = new ImageView(getActivity());
//            //iv.setBackgroundResource(mImageId[i]);
//            iv.setBackgroundResource();
//            mViewpagerList.add(iv);
                ImageLoader.getInstance().displayImage(infos.get(i).getType(), iv, ImageLoadOptions.fadeIn_options);

                mViewpagerList.add(iv);

                //初始化小圆点
                ImageView point = new ImageView(getActivity());
                //小原点是一个shape图形
                point.setImageResource(R.drawable.shape_oval_home_bunner);
                //初始化布局参数，宽和高包裹内容，父控件是谁，就是谁声明的布局参数
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i > 0) {
                    //从二个小圆点开始设置左边距
                    //传递10dp，通过不同的设备来计算得到px值
                    int dp = DensityUtil.dip2px(getActivity(), 3);
                    params.leftMargin = dp;
                }
                //设置布局参数
                point.setLayoutParams(params);
                //将小圆点添加到线性布局中
                mllOvalWhite.addView(point);
            }
        }

    }




    //给viewpager设置数据适配器
    class HomeAdapter extends PagerAdapter {

        @Override//item的个数
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//            ImageView view = mViewpagerList.get(position % mImageId.length);
//            container.addView(view);
            ImageView imageView = new ImageView(getActivity());
            container.addView(imageView);
            ImageLoader.getInstance().displayImage(infos.get(position % infos.size()).getType(), imageView, ImageLoadOptions.fadeIn_options);

            return imageView;
        }

        @Override//销毁一个item
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent;
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == MOREWELFARE) {
                int userid = data.getIntExtra("userid", 0);
                //是从我的福利跳转到登录，登录成功后就跳转到我的福利界面
                intent = new Intent(getActivity(), MyWelfareActivity.class);
                intent.putExtra("userId", userid);
                Lg.e("userid", userid);
                startActivity(intent);
            } else if (requestCode == INVITEFRIEND) {
                int userid = data.getIntExtra("userid", 0);
                //是从邀请好友跳转到登录的，登录成功后就跳转到邀请好友界面
                intent = new Intent(getActivity(), InviteFriendsActivity.class);
                intent.putExtra("userId", userid);
                Lg.e("userid", userid);
                startActivity(intent);
            }
        }
    }

    /**
     * 请求接口获取我的福利数量
     */
    public void getMyWelfare() {
        String url = "/api/users/" + userId + "/vouchers-count";
        url = UrlHelper.getUrl(url);

        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObj = response;
                        Lg.e("获取数据成功", jsonObj.toString());
                        if (jsonObj != null) {
                            String unused = jsonObj.optString("UNUSED");//未使用
                            String used = jsonObj.optString("USED");    //已经使用
                            String expired = jsonObj.optString("EXPIRED");//已经过期
                            //设置各种状态代金券数量
                            mTvWelfareNumber.setText(unused);
                            if ("0".equals(unused)) {//如果我的福利数量是0.那么就隐藏
                                mTvWelfareNumber.setVisibility(View.INVISIBLE);
                            } else {
                                mTvWelfareNumber.setVisibility(View.VISIBLE);
                            }
                        }

                        //所有的数据加载成功后，取消正在加载
                        dimissLoadingDialog();
                        mllMyContent.setVisibility(View.VISIBLE);
                        noNetWorkLayout.setVisibility(View.INVISIBLE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dimissLoadingDialog();
                mllMyContent.setVisibility(View.INVISIBLE);
                noNetWorkLayout.setVisibility(View.VISIBLE);
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
        BaseApplication.getRequestQueue().add(jsonObjectRequest);
    }


    /**
     * 请求接口获取首页bunner图片
     */

    private String jsonStr = "";

    public String getHomeBunner() {

        String url = "/ums/cms/carousel";
        url = UrlHelper.getUrl(url);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(0);
            conn.setDefaultUseCaches(false);
            //conn.setRequestProperty("Connection","false");
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestProperty("user-agent", "android");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                //所有的数据加载成功后，取消正在加载
                dimissLoadingDialog();


                InputStream in = conn.getInputStream();
                BufferedReader rd = null;
                String contentEncoding = conn.getContentEncoding();
                if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                    rd = new BufferedReader(new InputStreamReader(new GZIPInputStream(in), "UTF-8"), 8192);
                } else {
                    rd = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8192);

                }

                String tempLine = rd.readLine();
                StringBuffer temp = new StringBuffer();
                while (tempLine != null) {
                    temp.append(tempLine);
                    tempLine = rd.readLine();

                }
                jsonStr = temp.toString();
                rd.close();
                in.close();

            } else {
                //所有的数据加载成功后，取消正在加载
                dimissLoadingDialog();
            }

        } catch (IOException e) {
            //所有的数据加载成功后，取消正在加载
            dimissLoadingDialog();
            e.printStackTrace();
        }

        conn.disconnect();

        return jsonStr;
    }


    @Override
    public void onPause() {
        MobclickAgent.onPause(getActivity());

        super.onPause();
    }

    @Override
    public void onResume() {
        //友盟统计
        MobclickAgent.onResume(getActivity());

        //统计用户的设备号码
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        //获取本机MIEI号码（仅手机存在）
        //String deviceId = telephonyManager.getDeviceId();
        //获取设备序列号
        String sn = telephonyManager.getSimSerialNumber();

        //获取用户机型
        String phonetype = android.os.Build.MODEL;

        //获取Android版本号
        String phoneVersionCode = android.os.Build.VERSION.RELEASE;

        Lg.e("设备：" + sn + "机型" + phonetype + "版本号：" + phoneVersionCode);

        //int duration=1;
        //统计用户机型，版本号,设备序列号
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userType", phonetype);//机型
        map.put("viersionCode", phoneVersionCode);//Android版本
        map.put("serialNumber", sn);//设备序列号
        MobclickAgent.onEvent(getActivity(), "phone_information", map);
        //MobclickAgent.onEventValue(WebViewActivity.this, "phoneinformation", map, duration);


        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_my_welfare://跳转我的福利
                //查看我的福利之前，检查用户是否登录，如果没有那么就跳转到登录界面
                if (BaseApplication.getInstance().isLogined()) {
                    if (userInfo != null) {
                        //已经登录，跳转到我的福利
                        intent = new Intent(getActivity(), MyWelfareActivity.class);
                        intent.putExtra("userId", userInfo.getId());
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("isMyWelfare", true);
                    startActivityForResult(intent, MOREWELFARE);
                }
                break;

            case R.id.ll_invite_friend://跳转邀请好友
                //打开邀请好友前检查用户是否登录，如果没有，那么久跳转到登录界面
                if (BaseApplication.getInstance().isLogined()) {
                    if (userInfo != null) {
                        intent = new Intent(getActivity(), InviteFriendsActivity.class);
                        intent.putExtra("userId", userInfo.getId());
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("isInviteFriend", true);
                    startActivityForResult(intent, INVITEFRIEND);
                }
                break;

            case R.id.ll_mobile://跳转安全保障
                intent = new Intent(getActivity(), HelperCenterActivity.class);
                intent.putExtra("is_home", true);
                startActivity(intent);
                break;

            case R.id.noNetworkLayout:
                getActivity().findViewById(R.id.net_error_content).setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().findViewById(R.id.net_error_content).setVisibility(View.VISIBLE);
                    }
                }, 300);
                //重新请求网络，获取数据
                //获取我的福利数量
                getMyWelfare();

                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //移除消息
            handler.removeMessages(0);
        } else {
            //可见时重新发送消息，轮播图片
            handler.sendEmptyMessageDelayed(0, 5000);

            userInfo = BaseApplication.getInstance().getUserInfo();


            //同步应用程序当前版本的cookie
            synVersionNameCookie(getActivity());

            //判断当前网络是否可用
            if (Util.isNetworkAvailable(getActivity())) {
                mllMyContent.setVisibility(View.VISIBLE);
                noNetWorkLayout.setVisibility(View.GONE);
                if (BaseApplication.getInstance().isLogined()) {
                    userInfo = BaseApplication.getInstance().getUserInfo();
                }
            } else {
                mllMyContent.setVisibility(View.GONE);
                noNetWorkLayout.setVisibility(View.VISIBLE);
            }

            //表示已经登录
            if (BaseApplication.getInstance().isLogined()) {
                if (userInfo != null) {
                    userId = userInfo.getId();
                    //获取我的福利数量
                    getMyWelfare();
                    //显示我的福利数量的布局
                    mTvWelfareNumber.setVisibility(View.VISIBLE);
                }

            } else {//表示目前还没有登录,那么就隐藏主界面我的福利的数量的布局
                mTvWelfareNumber.setVisibility(View.INVISIBLE);
            }


            //重新可见时，发送广播到HomeItemFragment界面，刷新界面
            Intent intent=new Intent();
            intent.setAction("home_item_refresh");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            getActivity().sendBroadcast(intent);

        }
        super.onHiddenChanged(hidden);
    }


    private void initViewPager1() {
        fragments1.add(new HomeItemFragmnet()); //首页条目展示

        MyInvestMentFragmentAdapter adapter =
                new MyInvestMentFragmentAdapter(fragments1, getActivity().getSupportFragmentManager(), mHelperViewPager);
        adapter.setOnExtraPageChangeListener(new MyInvestMentFragmentAdapter.OnExtraPageChangeListener() {
            public void onExtraPageSelected(int i) {
                //switchBtnList(i);
            }
        });
    }



//    /**
//     * 让轮播图viewpager滚动起来
//     */
//    public void startRoll() {
//        // 滚动viewpager
//        if (myAdapter == null) {
//            // 1.第一次初始化适配器
//            myAdapter = new MyAdapter();
//            viewpager.setAdapter(myAdapter);
//            viewpager.setCurrentItem(currentPosition);
//        } else {// 8.第二次，只需要通知适配器数据发生了变化，要刷新Ui
//            myAdapter.notifyDataSetChanged();
//        }
//        // 2.发送一个延时的消息，3秒后执行runnableTask类里run方法里的操作
//        // ★（为什么执行的是runnableTask，而不是handleMessage呢？这里涉及到handler消息机制源码解析）
//        handler.postDelayed(runnableTask, 3000);
//    }

}


