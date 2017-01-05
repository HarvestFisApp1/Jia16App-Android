package com.jia16.invest;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.jia16.base.BaseApplication;
import com.jia16.bean.TransferMoney;
import com.jia16.bean.UserInfo;
import com.jia16.pulltorefreshview.BaseListFragment;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.pulltorefreshview.adapter.TransferMoneyAdapter;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.TimeUtils;
import com.jia16.util.UrlHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我要投资界面--转让变现
 */
public class TransFerRealizationFragmnet extends BaseListFragment<TransferMoney> {

    private UserInfo userInfo;
    private int userId;
    private long currentTimeInLong;

    private BroadcastReceiver receiver;


    @Override
    public BasicAdapter<TransferMoney> getAdapter() {
        return new TransferMoneyAdapter(list);
    }

    @Override
    public Object requestData() {

        BaseApplication.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog();
            }
        }, 0);

        if (BaseApplication.getInstance().isLogined()) {
            userInfo = BaseApplication.getInstance().getUserInfo();
            if (userInfo != null) {
                userId = userInfo.getId();
            }
        }

        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();

        postUnuserdDatas1();

        //我要投资界面发送过来的广播
        registerReceiver();

        return list;
    }


    /**
     * 我要投资界面发送过来的广播
     */
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("invest_ftransfer_money");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            list.clear();
                            //重新请求数据
                            postUnuserdDatas1();
                            //回到顶部
                            listView.setSelection(0);
                        }
                    }, 0);
                }
            };
        }
        getActivity().registerReceiver(receiver, intentFilter);
    }


    /**
     * 获取转让变现的数据，然后展示数据
     */
    private void postUnuserdDatas1() {
        String url = "";
        //获取当前时间的毫秒数
        currentTimeInLong = TimeUtils.getCurrentTimeInLong();
        Lg.e("当前时间的毫秒值", currentTimeInLong);
        if (list.size() == 0) {

            url = "/api/subjects?page=1&pageLimit=5&timestamp=" + currentTimeInLong + "&catalog=JIASHI_V5&catalog=JIASHI_V10&catalog=JIASHI_V12&catalog=JIASHI_V13_T&status=FUNDING&status=FUNDED&status=PORTION_FUNDED&include=funding-subject-count&view=mainPage&orderFlag=1";

        } else {

            int page = Integer.valueOf(list.size() / 5).intValue() + 1;
            url = "/api/subjects?page=" + page + "&pageLimit=5&timestamp=" + currentTimeInLong + "&catalog=JIASHI_V5&catalog=JIASHI_V10&catalog=JIASHI_V12&catalog=JIASHI_V13_T&status=FUNDING&status=FUNDED&status=PORTION_FUNDED&include=funding-subject-count&view=mainPage&orderFlag=1";

        }
        url = UrlHelper.getUrl(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        dimissLoadingDialog();

                        Lg.e("response——获取预期年化数据成功", response.toString());

                        String items = response.optString("items");
                        Lg.e("items", items);
                        ArrayList<TransferMoney> infos = (ArrayList<TransferMoney>) JsonUtil.parseJsonToList(items, new TypeToken<List<TransferMoney>>() {
                        }.getType());
                        Lg.e("infos.....", infos.size());
                        Lg.e("list......................", list.size());
                        if (infos != null) {
                            if (list.size() % 5 == 0) {
                                list.addAll(infos);//如果不等于0，那么久表示已经没有数据了。那么就不要添加到集合中
                            }
                            adapter.notifyDataSetChanged();
                            refreshListView.onRefreshComplete();
                        }


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

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 重写父类下拉刷新的listview的模式，设置为只能加载更多
     */
    public void setRefreshMode() {
        //1.设置下拉刷新的listview的模式,设置为只能加载更多
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//默认为既可以下拉，也可以下拉
    }

}