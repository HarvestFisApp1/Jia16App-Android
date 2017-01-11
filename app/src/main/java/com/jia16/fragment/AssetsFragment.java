package com.jia16.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.assets.MyTotalAconutActivity;
import com.jia16.assets.investstate.ApplyForFragmnet;
import com.jia16.assets.dealrun.CashRunActivity;
import com.jia16.assets.investstate.HoldForFragmnet;
import com.jia16.assets.investstate.ReturnMoneyFragmnet;
import com.jia16.assets.investstate.TransferForFragmnet;
import com.jia16.assets.investstate.TransferOkFragmnet;
import com.jia16.base.BaseApplication;
import com.jia16.base.BaseFragment;
import com.jia16.bean.Investment;
import com.jia16.bean.UserInfo;
import com.jia16.more.helpercenter.MyInvestMentFragmentAdapter;
import com.jia16.util.AlertUtil;
import com.jia16.util.AmountUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.PopupWindowUtils;
import com.jia16.util.TimeUtils;
import com.jia16.util.UrlHelper;
import com.jia16.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的资产 -
 */
public class AssetsFragment extends BaseFragment implements View.OnClickListener {

    private BaseApplication mContext;
    private LayoutInflater inflater;

    private FrameLayout mInvesterFrame; //申请中
    private FrameLayout mCommonFrame;//持有中
    private FrameLayout mMobileFrame;//已回款
    private FrameLayout mMakeoverFrame;//转让中
    private FrameLayout mMakeokFrame;//已转让

    private TextView mInvesterTv;
    private TextView mCommonTv;
    private TextView mMobileTv;
    private TextView mMakeoverTv;
    private TextView mMakeokTv;

    private View mInvesterLine;
    private View mCommonLine;
    private View mMobileLine;
    private View mMakeoverLine;
    private View mMakeokLine;


    private ViewPager mHelperViewPager;

    private List<Fragment> fragments = new ArrayList<Fragment>();


    private TextView mTvAssetsUsername;//用户名

    private LinearLayout mllMyContent; //全部内容的布局
    private View noNetWorkLayout;//没有网络时显示
    private UserInfo userInfo;
    private int userId;
    private TextView mTvUnuserWelfare;//未使用代金券总额
    private TextView mTvAvailableAmount;//显示可用金额
    private TextView mTvSettledProfit;//累计收益的布局


    //当前可用资金
    private double availableamount;
    //当前冻结中的资金
    private String freezeAmount;
    private double freeza;
    //获取当前投资中(持有中的)的金额
    private String amount;
    private double amount2;

    /**
     * 显示申请中，持有中等的数量
     */
    private TextView mTvApplying;//申请中
    private TextView mTvPending;//持有中
    private TextView mTvDone;//已回款
    private TextView mTvTransferring;//转让中
    private TextView mTvTransferrend;//已转让
    private TextView mTvTotalMoney;//总资产

    private TextView mTvCostRun;//资金流水

    private int invertmentId;
    private LinearLayout mllMyAcount;//我的总资产的线性布局
    private double mAcout;//总资产
    private String availableAmount;

    private BroadcastReceiver receiverTransferFor;//转让中的界面发送过来的广播

    /**
     * 转让中界面传递过来的数据
     */
    private PopupWindow popupWindow;
    private int transferId;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_assets, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showLoadingDialog();

        //同步应用程序当前版本的cookie
        synVersionNameCookie(getActivity());

        View mPatentView = getView();

        //初始化布局
        initView(mPatentView);


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

        //获取未使用的代金券的总金额
        postUnuserdAmount();

        //显示申请中，持有中等的数量
        postInvertmentCount();

        //获取用户投资的id
        getInvestmentId();

        //绑定数据
        initData();

        //转让中的界面发送过来的广播
        registerReceiverTransferFor();

    }

    /**
     * 转让中的界面发送过来的广播
     */
    private void registerReceiverTransferFor() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("show_transfer_popupWindow");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiverTransferFor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 一个自定义的布局，作为显示的内容
                        View contentView = LayoutInflater.from(getActivity()).inflate(
                                R.layout.cancel_transfer_popwindow, null);
                        //弹出使用规则的弹框
                        popupWindow = PopupWindowUtils.showPopupWindow(contentView,60);

                        //取消按钮
                        ImageView mIvButton = (ImageView) contentView.findViewById(R.id.iv_button);

                        //确认按钮
                        Button mBtAffirmTransfer = (Button) contentView.findViewById(R.id.bt_affirm_transfer);

                        mIvButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (popupWindow != null && popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                    popupWindow =null;
                                }
                            }
                        });

                        //获取广播传递过来的该转让标的id
                        transferId = intent.getIntExtra("transferId", 0);

                        //确认撤销转让
                        mBtAffirmTransfer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (popupWindow != null && popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                    popupWindow =null;
                                }
                                //请求接口，撤销转让标的
                                postCancelTransfer();
                            }
                        });

                        //显示popupWindow弹窗
                        popupWindow.showAsDropDown(contentView);
                    }
                },0);
            }
        };
        getActivity().registerReceiver(receiverTransferFor,intentFilter);

    }

    /**
     * 请求接口，撤销转让标的
     */
    private void postCancelTransfer() {

        showLoadingDialog();

        long currentTimeMillis = System.currentTimeMillis();

        userId = userInfo.getId();
        String url = "/api/users/"+userId+"/subjects/"+transferId+"/pass";
        url = UrlHelper.getUrl(url);

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("timestamp",currentTimeMillis);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        dimissLoadingDialog();

                       //撤销转让成功
                        Lg.e("撤销转让成功",".........撤销转让成功.....");


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dimissLoadingDialog();
                AlertUtil.showOneBtnDialog(getActivity(), "撤销转让失败", new View.OnClickListener() {
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
        BaseApplication.getRequestQueue().add(jsonObjectRequest);

    }


    /**
     * 初始化布局
     */
    public void initView(View view) {

        //全部内容的布局
        mllMyContent = (LinearLayout) view.findViewById(R.id.ll_my_content);
        noNetWorkLayout = view.findViewById(R.id.noNetworkLayout);//没有网络时显示
        noNetWorkLayout.setOnClickListener(this);

        mHelperViewPager = (ViewPager) view.findViewById(R.id.helper_view_pager);

        mInvesterFrame = (FrameLayout) view.findViewById(R.id.invester_frame);//申请中
        mCommonFrame = (FrameLayout) view.findViewById(R.id.common_frame);//持有中
        mMobileFrame = (FrameLayout) view.findViewById(R.id.mobile_frame);//已回款
        mMakeoverFrame = (FrameLayout) view.findViewById(R.id.makeover_frame);//转让中
        mMakeokFrame = (FrameLayout) view.findViewById(R.id.makeok_frame);//已转让

        mInvesterFrame.setOnClickListener(this);
        mCommonFrame.setOnClickListener(this);
        mMobileFrame.setOnClickListener(this);
        mMakeoverFrame.setOnClickListener(this);
        mMakeokFrame.setOnClickListener(this);


        mInvesterTv = (TextView) view.findViewById(R.id.invester_tv);
        mCommonTv = (TextView) view.findViewById(R.id.common_tv);
        mMobileTv = (TextView) view.findViewById(R.id.mobile_tv);
        mMakeoverTv = (TextView) view.findViewById(R.id.makeover_tv);
        mMakeokTv = (TextView) view.findViewById(R.id.makeok_tv);

        mInvesterLine = view.findViewById(R.id.invester_line);
        mCommonLine = view.findViewById(R.id.common_line);
        mMobileLine = view.findViewById(R.id.mobile_line);
        mMakeoverLine = view.findViewById(R.id.makeover_line);
        mMakeokLine = view.findViewById(R.id.makeok_line);

        //用户名
        mTvAssetsUsername = (TextView) view.findViewById(R.id.tv_assets_username);
        //未使用代金券总额
        mTvUnuserWelfare = (TextView) view.findViewById(R.id.tv_unuser_welfare);
        //可用金额
        mTvAvailableAmount = (TextView) view.findViewById(R.id.tv_available_amount);
        //累计收益的布局
        mTvSettledProfit = (TextView) view.findViewById(R.id.tv_settled_profit);

        //显示申请中，持有中等的数量
        mTvApplying = (TextView) view.findViewById(R.id.tv_applying);
        mTvPending = (TextView) view.findViewById(R.id.tv_pending);
        mTvDone = (TextView) view.findViewById(R.id.tv_done);
        mTvTransferring = (TextView) view.findViewById(R.id.tv_transferring);
        mTvTransferrend = (TextView) view.findViewById(R.id.tv_transferrend);
        mTvTotalMoney = (TextView) view.findViewById(R.id.tev_total_money);
        mllMyAcount = (LinearLayout) view.findViewById(R.id.ll_my_acount);
        mllMyAcount.setOnClickListener(this);
        //资金流水
        mTvCostRun = (TextView) view.findViewById(R.id.tv_cash_run);
        mTvCostRun.setOnClickListener(this);

        initViewPager();

    }

    private void initViewPager() {
        fragments.add(new ApplyForFragmnet());
        fragments.add(new HoldForFragmnet());
        fragments.add(new ReturnMoneyFragmnet());
        fragments.add(new TransferForFragmnet());
        fragments.add(new TransferOkFragmnet());

        MyInvestMentFragmentAdapter adapter =
                new MyInvestMentFragmentAdapter(fragments, getActivity().getSupportFragmentManager(), mHelperViewPager);
        adapter.setOnExtraPageChangeListener(new MyInvestMentFragmentAdapter.OnExtraPageChangeListener() {
            public void onExtraPageSelected(int i) {
                switchBtnList(i);
            }
        });

        //进入我的资产界面，默认显示的是持有中的
        mHelperViewPager.setCurrentItem(1);
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

                mMakeoverTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMakeoverLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMakeokTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMakeokLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));
                break;
            case 1:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.main_color));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMakeoverTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMakeoverLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMakeokTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMakeokLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));
                break;
            case 2:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.text_gray));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.main_color));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mMakeoverTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMakeoverLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMakeokTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMakeokLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));
                break;

            case 3:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.text_gray));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMakeoverTv.setTextColor(getResources().getColor(R.color.main_color));
                mMakeoverLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mMakeokTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMakeokLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));
                break;

            case 4:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.text_gray));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMakeoverTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMakeoverLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMakeokTv.setTextColor(getResources().getColor(R.color.main_color));
                mMakeokLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));
                break;

            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.invester_frame: //申请中
                mHelperViewPager.setCurrentItem(0);
                break;
            case R.id.common_frame: //持有中
                mHelperViewPager.setCurrentItem(1);
                break;
            case R.id.mobile_frame: //已回款
                mHelperViewPager.setCurrentItem(2);
                break;
            case R.id.makeover_frame: //转让中
                mHelperViewPager.setCurrentItem(3);
                break;
            case R.id.makeok_frame: //已转让
                mHelperViewPager.setCurrentItem(4);
                break;

            case R.id.tv_cash_run://资金流水
                intent = new Intent(getActivity(), CashRunActivity.class);
                intent.putExtra("userId", userInfo.getId());
                getActivity().startActivity(intent);
                break;

            case R.id.ll_my_acount://账户详情--总资产
                intent=new Intent(getActivity(),MyTotalAconutActivity.class);
                //数据类型都是Double类型
                intent.putExtra("mtotal_acount",mAcout);//总资产
                intent.putExtra("available_acount",availableamount);//可用资产
                intent.putExtra("invest_acount",amount2);//投资中
                intent.putExtra("freeze_acount",freeza);//冻结资金
                startActivity(intent);
            break;

            case R.id.noNetworkLayout:
                getActivity().findViewById(R.id.net_error_content).setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().findViewById(R.id.net_error_content).setVisibility(View.VISIBLE);
                    }
                },300);
                //重新请求网络，获取数据
                //获取未使用的代金券的总金额
                postUnuserdAmount();

                //显示申请中，持有中等的数量
                postInvertmentCount();

                //获取用户投资的id
                getInvestmentId();

                //绑定数据
                initData();

                initViewPager();
                break;

            default:
                break;
        }
    }

    /**
     * 绑定数据
     */
    private void initData() {
        if (userInfo != null) {
            //设置用户名
            mTvAssetsUsername.setText(userInfo.getUsername());
        }
    }




    /**
     * 获取投资id
     */
    private void getInvestmentId() {
        long currentTimeInLong = TimeUtils.getCurrentTimeInLong();
        String url = "/api/users/current?_="+currentTimeInLong;
        url = UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String accounts = response.optString("accounts");

                        ArrayList<Investment> infos = (ArrayList<Investment>) JsonUtil.parseJsonToList(accounts, new TypeToken<List<Investment>>() {
                        }.getType());
                        for (int i = 0; i < infos.size(); i++) {
                            if (infos.get(i).getDescriptionType().equals("INVESTMENT")) {
                                invertmentId = infos.get(i).getId();
                                Lg.e("invertmentId", invertmentId);
                            }
                        }
                        //获取可用金额，冻结中金额
                        postAvailableAmount();
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
     * 获取可用金额，冻结中金额
     */
    private void postAvailableAmount() {

        String url = "/pay/lanmao/form/QUERY_USER_INFORMATION.json?platformUserNo=" + invertmentId;
        url = UrlHelper.getUrl(url);
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //{"availableAmount":0,"idCardNo":"420582********5016","userIdentity":"INVESTOR","isImportUserActivate":"true","freezeAmount":0,"activeStatus":"ACTIVATED","code":"0","userRole":"NORMAL","authlist":"","userType":"PERSONAL","balance":0,"auditStatus":"PASSED","bankcode":"ICBK","description":"操作成功","name":"尚*","idCardType":"PRC_ID","bankcardNo":"***************3935","platformUserNo":"230101","mobile":"15250201667","accessType":"FULL_CHECKED"}
                        Lg.e("response——获取数据成功", response.toString());
                        //当前可用资金
                        availableAmount = response.optString("availableAmount");
                        Lg.e("availableAmount。。。。", availableAmount);
                        if (!TextUtils.isEmpty(availableAmount)) {
                            availableamount = Double.parseDouble(availableAmount);
                            mTvAvailableAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(availableamount)));
                        }


                        //当前冻结中的资金
                        freezeAmount = response.optString("freezeAmount");
                        Lg.e("freezeAmount。。。。", freezeAmount);

                        //获取投资中的总资产，以及累计投资收益
                        postPendingAmount();

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
     * 显示申请中，持有中等的数量
     */
    private void postInvertmentCount() {
        if (userInfo != null) {
            userId = userInfo.getId();
        }

        String url = "/api/users/" + userId + "/investment-counts";
        url = UrlHelper.getUrl(url);
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("response——获取数据成功", response.toString());
                        //设置申请中的数量
                        String applying = response.optString("APPLYING");
                        mTvApplying.setText(applying);

                        //设置持有中的数量
                        String pending = response.optString("PENDING");
                        mTvPending.setText(pending);

                        //设置已回款的数量
                        String done = response.optString("DONE");
                        mTvDone.setText(done);

                        //设置转让中的数量
                        String transferring = response.optString("TRANSFERRING");
                        mTvTransferring.setText(transferring);

                        //设置已转让的数量
                        String transferred = response.optString("TRANSFERRED");
                        mTvTransferrend.setText(transferred);

                       Lg.e("设置申请中的数量....",applying+"...."+pending+"..."+done+"..."+transferring+".."+transferred);
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
     * 获取未使用的代金券的总金额
     */
    private void postUnuserdAmount() {
        if (userInfo != null) {
            userId = userInfo.getId();
        }
        String url = "/api/users/" + userId + "/voucher-total-amount?status=UNUSED";
        url = UrlHelper.getUrl(url);
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("response——获取数据成功", response.toString());
                        String amount = response.optString("amount");
                        //设置代金券总金额
                        if (amount != null) {
                            double welfare = Double.parseDouble(amount);
                            mTvUnuserWelfare.setText(AmountUtil.addComma(AmountUtil.DT.format(welfare)));
                        }
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
     * 获取总资产，以及累计投资收益
     */
    private void postPendingAmount() {
        if (userInfo != null) {
            userId = userInfo.getId();
        }
        String url = "/api/users/" + userId + "/accounts/statistics?accountType=INVESTMENT";
        url = UrlHelper.getUrl(url);
        final JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //{"cookie":"JSESSIONID=3271547596667824918; Path=\/","pendingInvestment":{"amount":10070,"currency":"CNY"},"pendingSubjectCount":2,"pendingProfit":{"amount":1154.75,"currency":"CNY"},"accumulatedInvestment":{"amount":12070,"currency":"CNY"},"settledProfit":{"amount":3.89,"currency":"CNY"}}
                        Lg.e("response——获取数据成功", response.toString());
                        //获取当前投资中(持有中的)的金额
                        String pendingInvestment = response.optString("pendingInvestment");
                        String settledProfit = response.optString("settledProfit");
                        String pendingProfit = response.optString("pendingProfit");
                        try {
                            JSONObject jsonObject1 = new JSONObject(pendingInvestment);
                            //获取当前投资中(持有中的)的金额
                            amount = jsonObject1.getString("amount");
                            amount2 = Double.parseDouble(AssetsFragment.this.amount);

                            //获取预期应得收益
                            JSONObject jsons=new JSONObject(pendingProfit);
                            String amountpending = jsons.getString("amount");
                            double pendingAmount = Double.parseDouble(amountpending);

                            //获取成功发送广播到持有中的fragment中更新界面
                            Intent intents=new Intent();
                            intents.setAction("hold_for_amount");
                            intents.addCategory(Intent.CATEGORY_DEFAULT);
                            //传递数据，当前持有中，预期应得收益
                            intents.putExtra("current_hold",amount2);
                            intents.putExtra("get_enraing",pendingAmount);
                            getActivity().sendBroadcast(intents);


                            //double freeza = 0;
                            if (!TextUtils.isEmpty(freezeAmount)) {
                                freeza = Double.parseDouble(AssetsFragment.this.freezeAmount);
                            }
                            double available = 0;
                            if (!TextUtils.isEmpty(availableAmount)) {
                                available = Double.parseDouble(AssetsFragment.this.availableAmount);
                            }

                            mAcout = amount2 + freeza + available;
                            mTvTotalMoney.setText(AmountUtil.addComma(AmountUtil.DT.format(mAcout)));
                            Lg.e("总资产。。。。。", mAcout);


                            JSONObject object = new JSONObject(settledProfit);
                            //获取累计收益的金额
                            String amount1 = object.getString("amount");
                            double settledamount = Double.parseDouble(amount1);
                            mTvSettledProfit.setText("+"+AmountUtil.addComma(AmountUtil.DT.format(settledamount)));

                            //发送广播，到已回款界面(已赚取收益)，更新ui数据
                            Intent intent=new Intent();
                            intent.setAction("earn_earnings");
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.putExtra("earn_earnings",settledamount);
                            getActivity().sendBroadcast(intent);



                            //所有的数据加载成功后，取消正在加载
                            dimissLoadingDialog();
                            mllMyContent.setVisibility(View.VISIBLE);
                            noNetWorkLayout.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dimissLoadingDialog();
                mllMyContent.setVisibility(View.INVISIBLE);
                noNetWorkLayout.setVisibility(View.VISIBLE);

//                AlertUtil.showOneBtnDialog(getActivity(), "获取数据失败", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });

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



    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden){

        }else {
            //判断当前网络是否可用
            if (Util.isNetworkAvailable(getActivity())) {
                mllMyContent.setVisibility(View.VISIBLE);
                noNetWorkLayout.setVisibility(View.INVISIBLE);
                if (BaseApplication.getInstance().isLogined()) {
                    userInfo = BaseApplication.getInstance().getUserInfo();
                }
            } else {
                mllMyContent.setVisibility(View.INVISIBLE);
                noNetWorkLayout.setVisibility(View.VISIBLE);
            }

            //获取未使用的代金券的总金额
            postUnuserdAmount();

            //显示申请中，持有中等的数量
            postInvertmentCount();


            //获取用户投资的id
            getInvestmentId();

            //绑定数据
            initData();

            initViewPager();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiverTransferFor);
        super.onDestroy();
    }

}
