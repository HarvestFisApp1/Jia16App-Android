package com.jia16.more.mywelfare;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
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
import com.jia16.bean.Ticket;
import com.jia16.bean.UserInfo;
import com.jia16.pulltorefreshview.BaseListFragment;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.pulltorefreshview.adapter.TicketAdapter;
import com.jia16.pulltorefreshview.adapter.UserTicketAdapter;
import com.jia16.util.AlertUtil;
import com.jia16.util.Constants;
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
 * 更多 ------我的福利 的已使用界面
 */
public class UserdFragmnet extends BaseListFragment<Ticket> {


    private UserInfo userInfo;
    private int userId;
    private String amount;//已使用的代金券的总金额
    private TextView mTvTicketNumber;
    private BroadcastReceiver receiver1;
    private BroadcastReceiver receiver2;
    private boolean isMoney=true;

    @Override
    public BasicAdapter<Ticket> getAdapter() {
        return new UserTicketAdapter(list);
    }

    @Override
    public Object requestData() {

        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();

        //获取未使用的代金券的总金额
        postUnuserdAmount();


        if(isMoney){
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


    @Override
    public void onResume() {
        isMoney = BaseApplication.getInstance().isMoney;
        super.onResume();
    }


    /**
     * 代金券按照金额排序的广播
     */
    private void registerReceiver1() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("welfare_number_sort");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        if(receiver1==null){
            receiver1 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //isMoney = intent.getBooleanExtra("isMoney", false);
                            list.clear();
                            //获取代金券的数据
                            postUnuserdDatas1();
                        }
                    }, 0);
                }
            };
        }
        getActivity().registerReceiver(receiver1, intentFilter);
    }


    /**
     * 代金券按照金额排序的广播
     */
    private void registerReceiver2() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("welfare_term_validity");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        if(receiver2==null){
            receiver2 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //isMoney = intent.getBooleanExtra("isMoney", true);
                            list.clear();
                            //获取代金券的数据
                            postUnuserdDatas2();
                        }
                    }, 0);
                }
            };
        }
        getActivity().registerReceiver(receiver2, intentFilter);
    }


    /**
     * 获取代金券的数据，然后展示数据
     */
    private void postUnuserdDatas1() {
        String url = "";
        if (list.size() == 0) {
            url = "/api/users/" + userId + "/simple-vouchers?status=USED&page=1&pageLimit=10&queryFlag=3";
        } else {
            int page = Integer.valueOf(list.size() / 10).intValue() + 1;
            url = "/api/users/" + userId + "/simple-vouchers?status=USED&page=" + page + "&pageLimit=10&queryFlag=3";
        }
        url = UrlHelper.getUrl(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //{"cookie":"JSESSIONID=7889407144502149558; Path=\/","pageLimit":10,"totalPage":1,"items":[{"id":1073590,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073591,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073592,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075107,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075108,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"}],"totalCount":5,"page":1}
                        Lg.e("response——获取数据成功", response.toString());
                        JSONObject jsonObj = response;
                        String items = response.optString("items");
                        //[{"id":1073590,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073591,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073592,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075107,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075108,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"}]
                        Lg.e("items", items);

                        ArrayList<Ticket> infos = (ArrayList<Ticket>) JsonUtil.parseJsonToList(items, new TypeToken<List<Ticket>>() {
                        }.getType());
                        Lg.e("infos.....", infos);
                        if (infos != null) {
                            if (list.size() % 10 == 0) {
                                list.addAll(infos);//如果不等于0，那么久表示已经没有数据了。那么就不要添加到集合中
                            }
                            adapter.notifyDataSetChanged();
                            refreshListView.onRefreshComplete();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String errorMsg = new String(error.networkResponse.data);
                    JSONObject json = new JSONObject(errorMsg);
                    if (json != null) {
                        String message = (String) json.opt("message");
                        if (message != null) {
                            AlertUtil.showOneBtnDialog(getActivity(), message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        } else {
                            AlertUtil.showOneBtnDialog(getActivity(), "获取数据失败", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
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
     * 获取代金券的数据，然后展示数据
     */
    private void postUnuserdDatas2() {
        String url = "";
        if (list.size() == 0) {
            url = "/api/users/" + userId + "/simple-vouchers?status=USED&page=1&pageLimit=10&queryFlag=1";
        } else {
            int page = Integer.valueOf(list.size() / 10).intValue() + 1;
            url = "/api/users/" + userId + "/simple-vouchers?status=USED&page=" + page + "&pageLimit=10&queryFlag=1";
        }
        url = UrlHelper.getUrl(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //{"cookie":"JSESSIONID=7889407144502149558; Path=\/","pageLimit":10,"totalPage":1,"items":[{"id":1073590,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073591,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073592,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075107,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075108,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"}],"totalCount":5,"page":1}
                        Lg.e("response——获取数据成功", response.toString());
                        JSONObject jsonObj = response;
                        String items = response.optString("items");
                        //[{"id":1073590,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073591,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1073592,"amount":{"amount":10,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075107,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"},{"id":1075108,"amount":{"amount":20,"currency":"CNY"},"status":"UNUSED","expireDate":"2016-12-16"}]
                        Lg.e("items", items);

                        ArrayList<Ticket> infos = (ArrayList<Ticket>) JsonUtil.parseJsonToList(items, new TypeToken<List<Ticket>>() {
                        }.getType());
                        Lg.e("infos.....", infos);
                        if (infos != null) {
                            if (list.size() % 10 == 0) {
                                list.addAll(infos);//如果不等于0，那么久表示已经没有数据了。那么就不要添加到集合中
                            }
                            adapter.notifyDataSetChanged();
                            refreshListView.onRefreshComplete();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String errorMsg = new String(error.networkResponse.data);
                    JSONObject json = new JSONObject(errorMsg);
                    if (json != null) {
                        String message = (String) json.opt("message");
                        if (message != null) {
                            AlertUtil.showOneBtnDialog(getActivity(), message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        } else {
                            AlertUtil.showOneBtnDialog(getActivity(), "获取数据失败", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
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
     * 获取未使用的代金券的总金额
     */
    private void postUnuserdAmount() {
        userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            userId = userInfo.getId();
        }
        String url = "/api/users/" + userId + "/voucher-total-amount?status=USED";
        url = UrlHelper.getUrl(url);
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Lg.e("response——获取数据成功", response.toString());
                        amount = response.optString("amount");
                        //设置代金券总金额
                        if (amount != null) {
                            int amounts = (int) Double.parseDouble(amount);
                            mTvTicketNumber.setText(amounts+"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String errorMsg = new String(error.networkResponse.data);
                    JSONObject json = new JSONObject(errorMsg);
                    if (json != null) {
                        String message = (String) json.opt("message");
                        if (message != null) {
                            AlertUtil.showOneBtnDialog(getActivity(), message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                        } else {
                            AlertUtil.showOneBtnDialog(getActivity(), "获取数据失败", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                        }
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

    //添加一个头布局
    @Override
    public void addHeadView() {
        View inflate = View.inflate(getActivity(), R.layout.listview_head, null);
        //代金券总的金额
        mTvTicketNumber = (TextView) inflate.findViewById(R.id.tv_ticket_number);
        TextView mTvWelfareDesc =  (TextView) inflate.findViewById(R.id.tv_welfare_desc);
        mTvWelfareDesc.setText("已使用代金券总额 ");
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

}