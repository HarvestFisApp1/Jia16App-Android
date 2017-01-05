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
import com.jia16.bean.HoldFor;
import com.jia16.bean.UserInfo;
import com.jia16.pulltorefreshview.BaseListFragment;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.pulltorefreshview.adapter.HoldForAdapter;
import com.jia16.util.AlertUtil;
import com.jia16.util.AmountUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的资产---持有中
 */
public class HoldForFragmnet extends BaseListFragment<HoldFor> {

    private UserInfo userInfo;
    private int userId;
    private TextView mTvHoldAmount;
    private TextView mTvEarning;

    private TextView mTvNoData;

    @Override
    public BasicAdapter<HoldFor> getAdapter() {
        return new HoldForAdapter(list);
    }


    //该方法是在子线程中
    @Override
    public Object requestData() {

        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();

        //请求数据，获取持有中的数据
        postHoldDatas();

        registerReceiver1();

        return list;
    }

    /**
     * 我的资产界面发送过来的广播
     */
    private void registerReceiver1() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("hold_for_amount");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        BroadcastReceiver receiver1=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //获取我的资产界面传递过来的数据(double类型)
                        //当前持有中的金额
                        double current_hold = intent.getDoubleExtra("current_hold", 0);
                        //预期应得收益
                        double get_enraing = intent.getDoubleExtra("get_enraing", 0);
                        //设置当前持有金额
                        mTvHoldAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(current_hold)));
                        //设置到期应得收益
                        mTvEarning.setText(AmountUtil.addComma(AmountUtil.DT.format(get_enraing)));
                    }
                },0);
            }
        };
            getActivity().registerReceiver(receiver1,intentFilter);
    }


    /**
     * 请求数据，获取持有中的数据
     */
    private void postHoldDatas() {

        userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            userId = userInfo.getId();
        }
        String url = null;
        Lg.e("list.......",list.size());
        if (list.size() == 0) {
            url = "/api/users/"+userId+"/additional-contracts?page=1&pageLimit=10&view=home&status=PENDING";
        } else {
            int page = Integer.valueOf(list.size() / 10).intValue() + 1;
            url = "/api/users/"+userId+"/additional-contracts?page="+page+"&pageLimit=10&view=home&status=PENDING";
        }
        url = UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("获取持有中的数据", response.toString());
                        String items = response.optString("items");
                        Lg.e("item....", items);
                        ArrayList<HoldFor> infos = (ArrayList<HoldFor>) JsonUtil.parseJsonToList(items, new TypeToken<List<HoldFor>>() {
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
                        } else {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvNoData.getLayoutParams();
                            params.height=38;
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

        View inflate = View.inflate(getActivity(), R.layout.listview_hold_head_text, null);
        //当前持有金额
        mTvHoldAmount = (TextView) inflate.findViewById(R.id.tv_hold_amount);
        //到期应得收益
        mTvEarning = (TextView) inflate.findViewById(R.id.tv_earning);

        //没有数据显示（无更多数据）的布局
        mTvNoData = (TextView) inflate.findViewById(R.id.tv_no_data);
        listView.addHeaderView(inflate);
    }

    @Override
    public void onResume() {

        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();

        list.clear();

        //请求数据，获取持有中的数据
        postHoldDatas();

        super.onResume();
    }
}
