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
import com.jia16.bean.ApplyFor;
import com.jia16.bean.UserInfo;
import com.jia16.pulltorefreshview.BaseListFragment;
import com.jia16.pulltorefreshview.adapter.ApplyForAdapter;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.util.AlertUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的资产---申请中
 */
public class ApplyForFragmnet extends BaseListFragment<ApplyFor> {

    private UserInfo userInfo;
    private int userId;
    private TextView mNoDesc;

    @Override
    public BasicAdapter<ApplyFor> getAdapter() {
        return new ApplyForAdapter(list);
    }


    //该方法是在子线程中
    @Override
    public Object requestData() {

        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();

        //请求数据，获取申请中的数据
        postApplyDatas();

        return list;
    }


    /**
     * 请求数据，获取推荐详情
     */
    private void postApplyDatas() {

        userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            userId = userInfo.getId();
        }
        String url = null;

        if (list.size() == 0) {
            url = "/api/users/"+userId+"/investment-requests?page=1&pageLimit=10&view=home&status=FUNDING&status=FUNDED&status=PENDING&status=PASS";
        } else {
            int page = Integer.valueOf(list.size() / 10).intValue() + 1;
            url = "/api/users/"+userId+"/investment-requests?page="+page+"&pageLimit=10&view=home&status=FUNDING&status=FUNDED&status=PENDING&status=PASS";
        }
        url = UrlHelper.getUrl(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("获取申请中的数据", response.toString());
                        String items = response.optString("items");
                        Lg.e("item....", items);
                        ArrayList<ApplyFor> infos = (ArrayList<ApplyFor>) JsonUtil.parseJsonToList(items, new TypeToken<List<ApplyFor>>() {
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
                            params.height=50;
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