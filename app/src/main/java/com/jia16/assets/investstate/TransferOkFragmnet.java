package com.jia16.assets.investstate;

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
import com.jia16.bean.TransferOk;
import com.jia16.bean.UserInfo;
import com.jia16.pulltorefreshview.BaseListFragment;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.pulltorefreshview.adapter.TransferOkAdapter;
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
 * 我的资产---已转让
 */
public class TransferOkFragmnet extends BaseListFragment<TransferOk> {

    private UserInfo userInfo;
    private int userId;
    private TextView mNoDesc;

    @Override
    public BasicAdapter<TransferOk> getAdapter() {
        return new TransferOkAdapter(list);
    }


    //该方法是在子线程中
    @Override
    public Object requestData() {

        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();

        //请求数据，获取推荐详情
        postNewsDatas();

        return list;
    }


    /**
     * 请求数据，获取推荐详情
     */
    private void postNewsDatas() {

        userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            userId = userInfo.getId();
        }
        String url = null;

        if (list.size() == 0) {
            url = "/api/users/"+userId+"/subjects?page=1&pageLimit=10&view=home&status=SIGNED&catalog=JIASHI_V5&catalog=JIASHI_V10&catalog=JIASHI_V12&catalog=JIASHI_V13_T";
        } else {
            int page = Integer.valueOf(list.size() / 10).intValue() + 1;
            url = "/api/users/"+userId+"/subjects?page="+page+"&pageLimit=10&view=home&status=SIGNED&catalog=JIASHI_V5&catalog=JIASHI_V10&catalog=JIASHI_V12&catalog=JIASHI_V13_T";
        }
        url = UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //{"pageLimit":10,"totalPage":1,"items":[{"id":1990,"recommendedRegisterTime":"2016-11-05","recommendedPhone":"158****9300","recommendedTotalInvest":{"amount":2000,"currency":"CNY"},"parentMidasUserId":97071,"createdAt":"2016-11-06","recommendedMidasUserId":100360,"parentCashCommission":{"amount":2,"currency":"CNY"},"recommendedRealName":"**明","parentCashReward":{"amount":10,"currency":"CNY"},"recommendType":"邀请码"}],"totalCount":1,"page":1}
                        Lg.e("获取已转让详情", response.toString());
                        String items = response.optString("items");
                        Lg.e("item....", items);
                        ArrayList<TransferOk> infos = (ArrayList<TransferOk>) JsonUtil.parseJsonToList(items, new TypeToken<List<TransferOk>>() {
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
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mNoDesc.getLayoutParams();
                            params.height=0;
                            mNoDesc.setLayoutParams(params);
                        }else {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mNoDesc.getLayoutParams();
                            int height = DensityUtil.dip2px(getActivity(), 50);
                            params.height=height;
                            mNoDesc.setLayoutParams(params);
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

        View inflate = View.inflate(getActivity(), R.layout.listview_head_text, null);
        mNoDesc = (TextView) inflate.findViewById(R.id.tv_no_desc);
        listView.addHeaderView(inflate);
    }

}
