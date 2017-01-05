package com.jia16.assets.dealrun;

import android.content.Intent;
import android.view.View;
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
import com.jia16.bean.AllDeal;
import com.jia16.bean.Investment;
import com.jia16.bean.UserInfo;
import com.jia16.pulltorefreshview.BaseListFragment;
import com.jia16.pulltorefreshview.adapter.AllDealAdapter;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.util.AlertUtil;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的资产--资金流水--全部交易
 */
public class AllDealFragmnet extends BaseListFragment<AllDeal> {

    private int invertmentId;//资金流水界面的用户投资的id

    private UserInfo userInfo;

    private int userId;

    private TextView mNoDesc;

    @Override
    public BasicAdapter<AllDeal> getAdapter() {
        return new AllDealAdapter(list);
    }

    @Override
    public Object requestData() {
        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();

        userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            userId = userInfo.getId();
        }

        //获取投资id
        getInvestmentId();


        return list;
    }

    private int page;
    /**
     * 获取全部交易的流水
     */
    private void postAllDeal() {

        String url = "";
        if (list.size() == 0) {
            url = "/api/users/" + userId + "/accounts/" + invertmentId + "/transactions?page=1&pageLimit=10&include=transactionsCount&view=home";
        } else {
            page = Integer.valueOf(list.size() / 10).intValue() + 1;
            url = "/api/users/" + userId + "/accounts/" + invertmentId + "/transactions?page=" + page + "&pageLimit=10&include=transactionsCount&view=home";
        }
        url = UrlHelper.getUrl(url);

        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("response——获取交易流水数据成功", response.toString());
                        String totalCount = response.optString("totalCount");

                        String items = response.optString("items");
                        Lg.e("......items.....", items);

                        if(page<2){
                            //发送广播，传递数据，更新交易流水数量
                            Intent intent = new Intent();
                            intent.setAction("all_deal_investment");
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.putExtra("totalCount", totalCount);
                            getActivity().sendBroadcast(intent);

                        }


                        ArrayList<AllDeal> infos = (ArrayList<AllDeal>) JsonUtil.parseJsonToList(items, new TypeToken<List<AllDeal>>() {
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
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        BaseApplication.getRequestQueue().add(jsonObjectRequest);
    }


    /**
     * 获取投资id
     */
    private void getInvestmentId() {

        String url = "/api/users/current";
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

                        postAllDeal();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String messageMsg = new String(error.networkResponse.data);
                    JSONObject jsonObject = new JSONObject(messageMsg);
                    if (jsonObject != null) {
                        String message = jsonObject.optString("message");
                        if (message != null) {
                            AlertUtil.showOneBtnDialog(getActivity(), message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
                    } else {
                        AlertUtil.showOneBtnDialog(getActivity(), "获取信息失败，请检查网络", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
     * 为下拉刷新的listview添加一个头布局
     */
    @Override
    public void addHeadView() {

        View inflate = View.inflate(getActivity(), R.layout.listview_head_text, null);
        mNoDesc = (TextView) inflate.findViewById(R.id.tv_no_desc);
        //代金券总的金额
        listView.addHeaderView(inflate);
    }

}
