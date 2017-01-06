package com.jia16.invest.iteminvest;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.jia16.R;
import com.jia16.base.BaseApplication;
import com.jia16.bean.InvestConstant;
import com.jia16.bean.ShuangYueJia;
import com.jia16.bean.UserInfo;
import com.jia16.pulltorefreshview.BaseListFragment;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.pulltorefreshview.adapter.InvestYearAdapter;
import com.jia16.util.AlertUtil;
import com.jia16.util.AmountUtil;
import com.jia16.util.DensityUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.TimeUtils;
import com.jia16.util.UrlHelper;
import com.jia16.view.RoundProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我要投资  界面--起投金额
 */
public class InvestMoneyFragmnet extends BaseListFragment<InvestConstant> {


    private UserInfo userInfo;
    private int userId;
    private BroadcastReceiver receiver1;
    private BroadcastReceiver receiver2;
    private boolean isEarn=true;
    private long currentTimeInLong;


    /**
     * 添加头布局
     */
    private TextView mAssetsTitle;
    private TextView mTvYearEarn;
    private TextView mTvBeginMoney;
    private  TextView mTvInvestDate;
    private TextView mTvEarnDesc;
    private RoundProgressBar mRbProgress;
    private LinearLayout mllItem;

    @Override
    public BasicAdapter<InvestConstant> getAdapter() {
        return new InvestYearAdapter(list);
    }

    @Override
    public Object requestData() {

        BaseApplication.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog();
            }
        },0);

        if(BaseApplication.getInstance().isLogined()){
            userInfo = BaseApplication.getInstance().getUserInfo();
            if(userInfo!=null){
                userId = userInfo.getId();
            }
        }


        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();


        if(isEarn){
            //获取代金券的数据
            postUnuserdDatas1();
        }else {
            //代金券按照有效期限排序
            postUnuserdDatas2();
        }

        //代金券按照金额排序的广播
        registerReceiver1();
        //代金券按照有效期限排序的广播
        registerReceiver2();

        return list;
    }



    /**
     * 代金券按照金额排序的广播
     */
    private void registerReceiver1() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("invest_fixation_earn");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        if(receiver1==null){
            receiver1 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isEarn = intent.getBooleanExtra("isEarn", false);
                            list.clear();
                            //获取代金券的数据
                            postUnuserdDatas1();
                            listView.setSelection(0);
                        }
                    },0);
                }
            };
        }
        getActivity().registerReceiver(receiver1,intentFilter);
    }


    /**
     * 代金券按照金额排序的广播
     */
    private void registerReceiver2() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("invest_personage_loan");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        if(receiver2==null){
            receiver2 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isEarn = intent.getBooleanExtra("isEarn", true);
                            list.clear();
                            //获取代金券的数据
                            postUnuserdDatas2();
                            listView.setSelection(0);
                        }
                    },0);
                }
            };
        }
        getActivity().registerReceiver(receiver2,intentFilter);
    }


    /**
     * 获取代金券的数据，然后展示数据
     */
    private void postUnuserdDatas1() {
        String url = "";
        //获取当前时间的毫秒数
        currentTimeInLong = TimeUtils.getCurrentTimeInLong();
        Lg.e("当前时间的毫秒值", currentTimeInLong);
        if (list.size() == 0) {
            if(BaseApplication.getInstance().isLogined()){
                url = "/api/subjects/app-fix-income?page=1&pageLimit=5&timestamp="+ currentTimeInLong +"&catalog=JIASHI_V1&catalog=JIASHI_V2&catalog=JIASHI_V3&catalog=JIASHIV4&userId="+userId+"&orderFlag=5";
            }else {
                url = "/api/subjects/app-fix-income?page=1&pageLimit=5&timestamp="+ currentTimeInLong +"&catalog=JIASHI_V1&catalog=JIASHI_V2&catalog=JIASHI_V3&catalog=JIASHIV4&orderFlag=5";
            }

        } else {
            int page = Integer.valueOf(list.size() / 6).intValue() + 1;
            if(BaseApplication.getInstance().isLogined()){
                url = "/api/subjects/app-fix-income?page="+page+"&pageLimit=5&timestamp="+ currentTimeInLong +"&catalog=JIASHI_V1&catalog=JIASHI_V2&catalog=JIASHI_V3&catalog=JIASHIV4&userId="+userId+"&orderFlag=5";
            }else {
                url = "/api/subjects/app-fix-income?page="+page+"&pageLimit=5&timestamp="+ currentTimeInLong +"&catalog=JIASHI_V1&catalog=JIASHI_V2&catalog=JIASHI_V3&catalog=JIASHIV4&orderFlag=5";
            }

        }
        url = UrlHelper.getUrl(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        dimissLoadingDialog();

                        Lg.e("response——获取预期年化数据成功", response.toString());

                        JSONObject jsonObj = response;

                        String items = response.optString("items");
                        Lg.e("items", items);
                        ArrayList<InvestConstant> infos = (ArrayList<InvestConstant>) JsonUtil.parseJsonToList(items, new TypeToken<List<InvestConstant>>() {
                        }.getType());
                        Lg.e("infos.....", infos.size());
                        Lg.e("list......................",list.size());
                        if (infos != null) {
                            if (list.size() % 6 == 0) {
                                list.addAll(infos);//如果不等于0，那么久表示已经没有数据了。那么就不要添加到集合中
                            }
                            adapter.notifyDataSetChanged();
                            refreshListView.onRefreshComplete();
                        }

                        //添加的头布局
                        String shuangyuejia = response.optString("shuangyuejiaSubjects");
                        Lg.e("....shuangyuejia",shuangyuejia);
                        if(!TextUtils.isEmpty(shuangyuejia)){
                            ArrayList<ShuangYueJia> inf = (ArrayList<ShuangYueJia>) JsonUtil.parseJsonToList(shuangyuejia, new TypeToken<List<ShuangYueJia>>() {
                            }.getType());
                            Lg.e("...............",inf);
                            //获取标的名称
                            String title = inf.get(0).getTitle();
                            String numInstal = inf.get(0).getNumInstal();
                            mAssetsTitle.setText(title+"_"+numInstal+"期");
                            //获取年化收益
                            double annualRate = inf.get(0).getAnnualRate();
                            String annualRated = AmountUtil.addComma(AmountUtil.DT.format(annualRate));
                            mTvYearEarn.setText(annualRated+"%");
                            //起投金额
                            mTvBeginMoney.setText("起投 "+inf.get(0).getMinInvAmount()+"元");
                            //期限
                            mTvInvestDate.setText("期限 "+inf.get(0).getLockPeriod()+"天");
                            //描述
                            mTvEarnDesc.setText(inf.get(0).getTagName());
                            //进度条
                            mRbProgress.setMax(inf.get(0).getTotalAmount());//设置进度条的最大值
                            mRbProgress.setProgress((int) inf.get(0).getInOrderInvest());//设置当前进度
                        }

                        //显示头布局
                        ViewGroup.LayoutParams layoutParams = mllItem.getLayoutParams();
                        int layoutDp = DensityUtil.dip2px(getActivity(), 172);
                        layoutParams.height=layoutDp;
                        mllItem.setLayoutParams(layoutParams);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dimissLoadingDialog();
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
     * 获取代金券的数据，然后展示数据
     */
    private void postUnuserdDatas2() {
        String url = "";
        if (list.size() == 0) {
            ///api/subjects/app-fix-income?page=1&pageLimit=5&timestamp=1483435052803&catalog=JIASHI_V8&catalog=JIASHI_V9&catalog=JIASHI_V13&status=FUNDING&status=FUNDED&status=PORTION_FUNDED&orderFlag=5
            url = "/api/subjects/app-fix-income?page=1&pageLimit=5&timestamp="+currentTimeInLong+"&catalog=JIASHI_V8&catalog=JIASHI_V9&catalog=JIASHI_V13&status=FUNDING&status=FUNDED&status=PORTION_FUNDED&orderFlag=5";
        } else {
            int page = Integer.valueOf(list.size() / 6).intValue() + 1;
            url = "/api/subjects/app-fix-income?page="+page+"&pageLimit=5&timestamp="+currentTimeInLong+"&catalog=JIASHI_V8&catalog=JIASHI_V9&catalog=JIASHI_V13&status=FUNDING&status=FUNDED&status=PORTION_FUNDED&orderFlag=5";
        }
        url = UrlHelper.getUrl(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        dimissLoadingDialog();

                        Lg.e("response——获取数据成功", response.toString());
                        JSONObject jsonObj = response;
                        String items = response.optString("items");
                        //[{"id":1073590,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073591,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073592,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075107,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075108,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"}]
                        Lg.e("items", items);

                        ArrayList<InvestConstant> infos = (ArrayList<InvestConstant>) JsonUtil.parseJsonToList(items, new TypeToken<List<InvestConstant>>() {
                        }.getType());
                        Lg.e("infos.....", infos);
                        if (infos != null) {
                            if (list.size() % 6 == 0) {
                                list.addAll(infos);//如果不等于0，那么久表示已经没有数据了。那么就不要添加到集合中
                            }
                            adapter.notifyDataSetChanged();
                            refreshListView.onRefreshComplete();
                        }

                        //隐藏头布局
                        ViewGroup.LayoutParams layoutParams = mllItem.getLayoutParams();
                        layoutParams.height=0;
                        mllItem.setLayoutParams(layoutParams);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dimissLoadingDialog();
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




    //添加一个头布局
    @Override
    public void addHeadView() {
        View inflate = View.inflate(getActivity(), R.layout.item_invest_year_item, null);
        //标的名称
        mAssetsTitle = (TextView) inflate.findViewById(R.id.assets_title);
        //年化收益
        mTvYearEarn = (TextView) inflate.findViewById(R.id.tv_year_earn);
        //起投金额
        mTvBeginMoney = (TextView) inflate.findViewById(R.id.tv_begin_money);
        //投资期限
        mTvInvestDate = (TextView) inflate.findViewById(R.id.tv_invest_date);
        //描述
        mTvEarnDesc = (TextView) inflate.findViewById(R.id.tv_earn_desc);
        //进度条
        mRbProgress = (RoundProgressBar) inflate.findViewById(R.id.rb_progress);
        //整个布局内容
        mllItem = (LinearLayout) inflate.findViewById(R.id.ll_item);
        listView.addHeaderView(inflate);
    }


    /**
     * 重写父类下拉刷新的listview的模式，设置为只能加载更多
     */
    public void setRefreshMode() {
        //1.设置下拉刷新的listview的模式,设置为只能加载更多
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//默认为既可以下拉，也可以下拉
    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver1);
        getActivity().unregisterReceiver(receiver2);
        super.onDestroy();
    }

    public void onPause() {
        BaseApplication.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog();
            }
        },0);

        list.clear();

        if(isEarn){
            //获取代金券的数据
            postUnuserdDatas1();
        }else {
            //代金券按照有效期限排序
            postUnuserdDatas2();
        }
        listView.setSelection(0);
        super.onPause();
    }
}