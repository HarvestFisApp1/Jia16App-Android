package com.jia16.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.reflect.TypeToken;
import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.base.BaseFragment;
import com.jia16.bean.FixedEarn;
import com.jia16.bean.HomeItem;
import com.jia16.bean.TransferMoney;
import com.jia16.util.AlertUtil;
import com.jia16.util.AmountUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.jia16.view.RoundProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页界面--条目展示
 */
public class HomeItemFragmnet extends BaseFragment implements View.OnClickListener {

    private BaseApplication mContext;
    private LayoutInflater inflater;

    /**
     * 新手专区
     */
    private TextView mTvAssetsTitle1;
    private TextView mTvWelfare1;
    private TextView mTvAssetsState1;
    private TextView mTvYearEarn1;
    private TextView mTvBeginMoney1;
    private TextView mTvInvestDate1;
    private RoundProgressBar mRbProgress1;
    private TextView mTvEarnDesc1;
    private LinearLayout mLLNewuserContent;

    /**
     * 固定收益
     */
    private TextView mTvAssetsTitle2;
    private TextView mTvWelfare2;
    private TextView mTvAssetsState2;
    private TextView mTvYearEarn2;
    private TextView mTvBeginMoney2;
    private TextView mTvInvestDate2;
    private RoundProgressBar mRbProgress2;
    private TextView mTvEarnDesc2;
    private LinearLayout mllEarnContent;

    /**
     * 个体网贷
     */
    private TextView mTvAssetsTitle3;
    private TextView mTvWelfare3;
    private TextView mTvAssetsState3;
    private TextView mTvYearEarn3;
    private TextView mTvBeginMoney3;
    private TextView mTvInvestDate3;
    private RoundProgressBar mRbProgress3;
    private TextView mTvEarnDesc3;
    private LinearLayout mllUnityContent;

    /**
     * 转让专区
     */
    private TextView mTvAssetsTitle4;
    private TextView mTvWelfare4;
    private TextView mTvAssetsState4;
    private TextView mTvYearEarn4;
    private TextView mTvHomeGeginMoney;
    private TextView mTvTransferDate;
    private LinearLayout mllFransferContent;

    private BroadcastReceiver receiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_home_item, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //同步应用程序当前版本的cookie
        synVersionNameCookie(getActivity());

        View mPatentView = getView();

        initView(mPatentView);

        getHomeItem();

        registerReceiver();

    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("home_item_refresh");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //重新请求数据
                            getHomeItem();
                        }
                    }, 0);
                }
            };
        }
        getActivity().registerReceiver(receiver, intentFilter);
    }


    /**
     * 初始化
     */
    private void initView(View view) {

        //新手专区
        mTvAssetsTitle1 = (TextView) view.findViewById(R.id.assets_title1);
        mTvWelfare1 = (TextView) view.findViewById(R.id.tv_welfare1);
        mTvAssetsState1 = (TextView) view.findViewById(R.id.tv_assets_state1);
        mTvYearEarn1 = (TextView) view.findViewById(R.id.tv_year_earn1);
        mTvBeginMoney1 = (TextView) view.findViewById(R.id.tv_begin_money1);
        mTvInvestDate1 = (TextView) view.findViewById(R.id.tv_invest_date1);
        mRbProgress1 = (RoundProgressBar) view.findViewById(R.id.rb_progress1);
        mTvEarnDesc1 = (TextView) view.findViewById(R.id.tv_earn_desc1);
        //新手专享的总的布局
        mLLNewuserContent = (LinearLayout) view.findViewById(R.id.ll_newuser_content);


        //固定收益
        mTvAssetsTitle2 = (TextView) view.findViewById(R.id.assets_title2);
        mTvWelfare2 = (TextView) view.findViewById(R.id.tv_welfare2);
        mTvAssetsState2 = (TextView) view.findViewById(R.id.tv_assets_state2);
        mTvYearEarn2 = (TextView) view.findViewById(R.id.tv_year_earn2);
        mTvBeginMoney2 = (TextView) view.findViewById(R.id.tv_begin_money2);
        mTvInvestDate2 = (TextView) view.findViewById(R.id.tv_invest_date2);
        mRbProgress2 = (RoundProgressBar) view.findViewById(R.id.rb_progress2);
        mTvEarnDesc2 = (TextView) view.findViewById(R.id.tv_earn_desc2);
        //固定收益的总布局
        mllEarnContent = (LinearLayout) view.findViewById(R.id.ll_earn_content);

        //个体网贷
        mTvAssetsTitle3 = (TextView) view.findViewById(R.id.assets_title3);
        mTvWelfare3 = (TextView) view.findViewById(R.id.tv_welfare3);
        mTvAssetsState3 = (TextView) view.findViewById(R.id.tv_assets_state3);
        mTvYearEarn3 = (TextView) view.findViewById(R.id.tv_year_earn3);
        mTvBeginMoney3 = (TextView) view.findViewById(R.id.tv_begin_money3);
        mTvInvestDate3 = (TextView) view.findViewById(R.id.tv_invest_date3);
        mRbProgress3 = (RoundProgressBar) view.findViewById(R.id.rb_progress3);
        mTvEarnDesc3 = (TextView) view.findViewById(R.id.tv_earn_desc3);

        mllUnityContent = (LinearLayout) view.findViewById(R.id.ll_unity_content);

        //转让专区
        mTvAssetsTitle4 = (TextView) view.findViewById(R.id.assets_title4);
        mTvWelfare4 = (TextView) view.findViewById(R.id.tv_welfare4);
        mTvAssetsState4 = (TextView) view.findViewById(R.id.tv_assets_state4);
        mTvYearEarn4 = (TextView) view.findViewById(R.id.tv_year_earn4);
        mTvHomeGeginMoney = (TextView) view.findViewById(R.id.tv_home_begin_money);
        mTvTransferDate = (TextView) view.findViewById(R.id.tv_home_transfer_date);
        //转让专区的总的布局
        mllFransferContent = (LinearLayout) view.findViewById(R.id.ll_fransfer_content);
    }


    /**
     * 获取主界面展示条目的数据
     */
    public void getHomeItem() {
        String url = UrlHelper.getUrl("/api/subjects/app_choice");
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("获取首页条目数据成功", response);

                        //新手专享
                        String newUser = response.optString("newUser");
                        //Lg.e("新手专享。。。。。。。。。。。。。。",newUser);
                        ArrayList<HomeItem> infos1 = (ArrayList<HomeItem>) JsonUtil.parseJsonToList(newUser, new TypeToken<List<HomeItem>>() {
                        }.getType());

                        if (infos1.size() != 0) {
                            mTvAssetsTitle1.setText(infos1.get(0).getTitle());
                            //是否可以使用代金券
                            String canUseVoucherTag = infos1.get(0).getCanUseVoucherTag();
                            if (canUseVoucherTag.equals("canUseVoucher")) {
                                mTvWelfare1.setVisibility(View.VISIBLE);
                            } else {
                                mTvWelfare1.setVisibility(View.GONE);
                            }

                            double annualrate1 = infos1.get(0).getInstalmentPolicy().getAnnualRate() * 100;
                            String annualRate1 = AmountUtil.addComma(AmountUtil.DT.format(annualrate1));
                            mTvYearEarn1.setText(annualRate1 + "%");
                            mTvBeginMoney1.setText("起投 " + infos1.get(0).getInvestmentPolicy().getMinimumInvestmentAmount().getAmount() + "元");
                            mTvInvestDate1.setText("期限 " + infos1.get(0).getInstalmentPolicy().getInterval().getCount() + "天");
                            //进度条
                            mRbProgress1.setMax((int) infos1.get(0).getAmount().getAmount());//设置进度条的最大值
                            mRbProgress1.setProgress((int) infos1.get(0).getCurrentInvestmentAmount().getAmount());//设置当前进度
                            mTvEarnDesc1.setText(infos1.get(0).getConfig().getTagName());

                            //如果有（新手专享）数据，那么就显示
                            mLLNewuserContent.setVisibility(View.VISIBLE);
                        } else {
                            //如果没有（新手专享）数据，那么就隐藏
                            mLLNewuserContent.setVisibility(View.INVISIBLE);
                        }


                        //固定收益
                        String fixedIncome = response.optString("fixedIncome");
                        //Lg.e("。。。。。固定收益。。。。。。。。。",fixedIncome);
                        ArrayList<FixedEarn> infos2 = (ArrayList<FixedEarn>) JsonUtil.parseJsonToList(fixedIncome, new TypeToken<List<FixedEarn>>() {
                        }.getType());
                        if(infos2.size() != 0){
                            mTvAssetsTitle2.setText(infos2.get(0).getTitle());
                            //是否可以使用代金券
                            String canUseVoucherTag2 = infos2.get(0).getCanUseVoucherTag();
                            if (canUseVoucherTag2.equals("canUseVoucher")) {
                                mTvWelfare2.setVisibility(View.VISIBLE);
                            } else {
                                mTvWelfare2.setVisibility(View.GONE);
                            }
                            //是否可以转让
                            boolean transferable = infos2.get(0).isTransferable();
                            if (transferable) {
                                mTvAssetsState2.setVisibility(View.VISIBLE);
                            } else {
                                mTvAssetsState2.setVisibility(View.GONE);
                            }
                            double annualrate2 = infos2.get(0).getInstalmentPolicy().getAnnualRate() * 100;
                            String annualRate2 = AmountUtil.addComma(AmountUtil.DT.format(annualrate2));
                            mTvYearEarn2.setText(annualRate2 + "%");
                            mTvBeginMoney2.setText("起投 " + infos2.get(0).getInvestmentPolicy().getMinimumInvestmentAmount().getAmount() + "元");
                            mTvInvestDate2.setText("期限 " + infos2.get(0).getInstalmentPolicy().getInterval().getCount() + "天");
                            //进度条
                            mRbProgress2.setMax((int) infos2.get(0).getAmount().getAmount());//设置进度条的最大值
                            mRbProgress2.setProgress((int) infos2.get(0).getCurrentInvestmentAmount().getAmount());//设置当前进度
                            mTvEarnDesc2.setText(infos2.get(0).getConfig().getTagName());

                            //表示有（固定收益）的数据,那么就显示布局
                            mllEarnContent.setVisibility(View.VISIBLE);
                        }else {
                            //表示没有（固定收益）的数据,那么就隐藏布局
                            mllEarnContent.setVisibility(View.INVISIBLE);
                        }


                        //个体网贷
                        String personalLoan = response.optString("personalLoan");
                        //Lg.e("。。。。。固定收益。。。。。。。。。",fixedIncome);
                        ArrayList<FixedEarn> infos3 = (ArrayList<FixedEarn>) JsonUtil.parseJsonToList(personalLoan, new TypeToken<List<FixedEarn>>() {
                        }.getType());
                        if(infos3.size() != 0){
                            mTvAssetsTitle3.setText(infos3.get(0).getTitle());
                            //是否可以使用代金券
                            String canUseVoucherTag3 = infos3.get(0).getCanUseVoucherTag();
                            if (canUseVoucherTag3.equals("canUseVoucher")) {
                                mTvWelfare3.setVisibility(View.VISIBLE);
                            } else {
                                mTvWelfare3.setVisibility(View.GONE);
                            }
                            //是否可以转让
                            boolean transferable2 = infos3.get(0).isTransferable();
                            if (transferable2) {
                                mTvAssetsState3.setVisibility(View.VISIBLE);
                            } else {
                                mTvAssetsState3.setVisibility(View.GONE);
                            }
                            double annualrate3 = infos3.get(0).getInstalmentPolicy().getAnnualRate() * 100;
                            String annualRate3 = AmountUtil.addComma(AmountUtil.DT.format(annualrate3));
                            mTvYearEarn3.setText(annualRate3 + "%");
                            mTvBeginMoney3.setText("起投 " + infos3.get(0).getInvestmentPolicy().getMinimumInvestmentAmount().getAmount() + "元");
                            mTvInvestDate3.setText("期限 " + infos3.get(0).getInstalmentPolicy().getInterval().getCount() + "天");
                            //进度条
                            mRbProgress3.setMax((int) infos3.get(0).getAmount().getAmount());//设置进度条的最大值
                            mRbProgress3.setProgress((int) infos3.get(0).getCurrentInvestmentAmount().getAmount());//设置当前进度
                            mTvEarnDesc3.setText(infos3.get(0).getConfig().getTagName());

                            //表示有（个体网贷）的数据,那么就隐藏布局
                            mllUnityContent.setVisibility(View.VISIBLE);
                        }else {
                            //表示没有（个体网贷）的数据,那么就隐藏布局
                            mllUnityContent.setVisibility(View.INVISIBLE);
                        }



                        //转让专区
                        String transfer = response.optString("transfer");
                        //Lg.e("。。。。。转让专区。。。。。。。。。",transfer);
                        ArrayList<TransferMoney> infos4 = (ArrayList<TransferMoney>) JsonUtil.parseJsonToList(transfer, new TypeToken<List<TransferMoney>>() {
                        }.getType());
                        if (infos4.size() != 0) {
                            mTvAssetsTitle4.setText(infos4.get(0).getTitle());
                            //是否可以使用代金券
                            String canUseVoucherTag4 = infos4.get(0).getCanUseVoucherTag();
                            if (canUseVoucherTag4.equals("canUseVoucher")) {
                                mTvWelfare4.setVisibility(View.VISIBLE);
                            } else {
                                mTvWelfare4.setVisibility(View.GONE);
                            }
                            //是否可以转让
                            boolean transferable4 = infos4.get(0).isTransferable();
                            if (transferable4) {
                                mTvAssetsState4.setVisibility(View.VISIBLE);
                            } else {
                                mTvAssetsState4.setVisibility(View.GONE);
                            }
                            double annualrate4 = infos4.get(0).getInstalmentPolicy().getAnnualRate() * 100;
                            String annualRate4 = AmountUtil.addComma(AmountUtil.DT.format(annualrate4));
                            mTvYearEarn4.setText(annualRate4 + "%");
                            double amount = infos4.get(0).getTransferAmount().getAmount();
                            String transferAmount = AmountUtil.addComma(AmountUtil.DT.format(amount));
                            mTvHomeGeginMoney.setText(transferAmount + "元");
                            int count = infos4.get(0).getInstalmentPolicy().getInterval().getCount();
                            mTvTransferDate.setText(count + "天");

                            //表示有（转让专区）的数据,那么就显示布局
                            mllFransferContent.setVisibility(View.VISIBLE);
                        } else {
                            //表示没有（转让专区）的数据,那么就隐藏布局
                            mllFransferContent.setVisibility(View.INVISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);

                dimissLoadingDialog();

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


                case R.id.noNetworkLayout:


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

        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }
}