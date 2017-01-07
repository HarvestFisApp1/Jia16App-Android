package com.jia16.assets.investstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;
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
import com.jia16.bean.Returnmoney;
import com.jia16.bean.UserInfo;
import com.jia16.pulltorefreshview.BaseListFragment;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.pulltorefreshview.adapter.ReturnMoneyAdapter;
import com.jia16.util.AlertUtil;
import com.jia16.util.AmountUtil;
import com.jia16.util.DensityUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的资产---已回款
 */
public class ReturnMoneyFragmnet extends BaseListFragment<Returnmoney> {

    private UserInfo userInfo;
    private int userId;
    private TextView mTvEarnMoney;//以赚取收益==累计收益

    private BroadcastReceiver receiver1;
    private TextView mTvNoData;//显示没有更多数据的布局

    @Override
    public BasicAdapter<Returnmoney> getAdapter() {
        return new ReturnMoneyAdapter(list);
    }


    //该方法是在子线程中
    @Override
    public Object requestData() {

        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();

        //请求数据，获取推荐详情
        postNewsDatas();

        //是我的资产界面发送过来的广播
        registerReceiver1();

        return list;
    }

    /**
     * 我的资产界面发送过来的广播
     */
    private void registerReceiver1() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("earn_earnings");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver1=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        double earn_earnings = intent.getDoubleExtra("earn_earnings", 0);
                        //设置已赚取收益
                        mTvEarnMoney.setText(AmountUtil.addComma(AmountUtil.DT.format(earn_earnings))+"(元)");
                    }
                },0);
            }
        };
            getActivity().registerReceiver(receiver1,intentFilter);
    }


    /**
     * 请求数据，获取已回款详情
     */
    private void postNewsDatas() {

        userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            userId = userInfo.getId();
        }
        String url = null;

        if (list.size() == 0) {
            url = "/api/users/"+userId+"/additional-contracts?page=1&pageLimit=10&view=home&status=DONE";
        } else {
            int page = Integer.valueOf(list.size() / 10).intValue() + 1;
            url = "/api/users/"+userId+"/additional-contracts?page="+page+"&pageLimit=10&view=home&status=DONE";
        }
        url = UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("获取已回款详情", response.toString());
                        String items = response.optString("items");
                        Lg.e("item....", items);
                        ArrayList<Returnmoney> infos = (ArrayList<Returnmoney>) JsonUtil.parseJsonToList(items, new TypeToken<List<Returnmoney>>() {
                        }.getType());
                        if (infos != null) {
                            if (list.size() % 10 == 0) {//如果不等于0，那么久表示已经没有数据了。那么就不要添加到集合中
                                list.addAll(infos);
                            }
                            adapter.notifyDataSetChanged();
                            refreshListView.onRefreshComplete();
                        }

                        if (list.size() > 0) {
                            //如果集合中有数据，那么就隐藏头布局
                            //将头布局的高度重新设置
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvNoData.getLayoutParams();
                            params.height=0;
                            mTvNoData.setLayoutParams(params);
                        }else {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvNoData.getLayoutParams();
                            int height = DensityUtil.dip2px(getActivity(), 50);
                            params.height=height;
                            mTvNoData.setLayoutParams(params);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
     * 重写父类下拉刷新的listview的模式，设置为只能加载更多
     */
    public void setRefreshMode() {
        //1.设置下拉刷新的listview的模式,设置为只能加载更多
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);//默认为既可以下拉，也可以下拉
    }


    /**
     * 为下拉刷新的listview添加一个头布局
     */
    @Override
    public void addHeadView() {
        //写一个相对布局，来进行显示与隐藏
        View inflate = View.inflate(getActivity(), R.layout.listview_head_invest_text, null);
        //已赚取收益
        mTvEarnMoney = (TextView) inflate.findViewById(R.id.tv_earn_money);
        //如果没有数据，显示的布局
        mTvNoData = (TextView) inflate.findViewById(R.id.tv_no_data);
        listView.addHeaderView(inflate);
    }

    @Override
    public void onResume() {

        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();

        list.clear();

        //请求数据，获取已回款详情
        postNewsDatas();

        super.onResume();
    }
}
