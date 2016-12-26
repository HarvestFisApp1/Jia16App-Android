package com.jia16.more.advicescenter;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.jia16.base.BaseApplication;
import com.jia16.bean.Advices;
import com.jia16.pulltorefreshview.BaseListFragment;
import com.jia16.pulltorefreshview.adapter.BasicAdapter;
import com.jia16.pulltorefreshview.adapter.HomeAdapter;
import com.jia16.util.JsonUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;
import com.jia16.web.WebActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * 更多 ------消息中心 的新闻界面
 */
public class NewsFragmnet extends BaseListFragment<Advices> {


    @Override
    public BasicAdapter<Advices> getAdapter() {
        return new HomeAdapter(list);
    }

    @Override
    public Object requestData() {

        //检查如果是下拉刷新，就清空集合
        checkPullFromStart();


        final String result = postNewsDatas();
        ArrayList<Advices> infos = (ArrayList<Advices>) JsonUtil.parseJsonToList(result, new TypeToken<List<Advices>>() {
        }.getType());
        if (infos != null) {
            if(list.size()%10==0){//如果不等于0，那么久表示已经没有数据了。那么就不要添加到集合中
                list.addAll(infos);
            }

            BaseApplication.mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Lg.e("result........", result);
                    adapter.notifyDataSetChanged();
                    refreshListView.onRefreshComplete();
                }
            });
        }
        return infos;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("linkUrl", list.get(position - 1).getUrl());
        intent.putExtra("title", "新闻");
        getActivity().startActivity(intent);
    }

    private String jsonStr = "";

    private String postNewsDatas() {

        String url = null;

        if (list.size() == 0) {
            url = "/ums/cms/news?page=1&pageLimit=10";
        } else {
                int page = Integer.valueOf(list.size() / 10).intValue() + 1;
                url = "/ums/cms/news?page=" + page + "&pageLimit=10";
        }
        url = UrlHelper.getUrl(url);

        HttpURLConnection conn = null;


        try {
            conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(0);
            conn.setDefaultUseCaches(false);
            //conn.setRequestProperty("Connection","false");
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestProperty("user-agent", "android");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                BufferedReader rd = null;
                String contentEncoding = conn.getContentEncoding();
                if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                    rd = new BufferedReader(new InputStreamReader(new GZIPInputStream(in), "UTF-8"), 8192);
                } else {
                    rd = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8192);

                }

                String tempLine = rd.readLine();
                StringBuffer temp = new StringBuffer();
                while (tempLine != null) {
                    temp.append(tempLine);
                    tempLine = rd.readLine();

                }
                jsonStr = temp.toString();
                rd.close();
                in.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        return jsonStr;
    }


}
