package com.jia16.more.mywelfare;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jia16.R;
import com.jia16.activity.MainActivity;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.more.helpercenter.MyInvestMentFragmentAdapter;
import com.jia16.util.AlertUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更多界面--我的福利 界面
 */
public class MyWelfareActivity extends BaseActivity {


    private FrameLayout mInvesterFrame; //未使用
    private FrameLayout mCommonFrame;   //已使用
    private FrameLayout mMobileFrame;   //已过期

    private TextView mInvesterTv;

    private TextView mCommonTv;

    private TextView mMobileTv;

    private View mInvesterLine;

    private View mCommonLine;

    private View mMobileLine;

    private ViewPager mHelperViewPager;

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private Button mBtnUseRule;//使用规则
    private Object myWelfare;
    private int userId;//更多界面传递过来的用户id


    private RadioButton mRbCheck1;
    private RadioButton mRbCheck2;
    private PopupWindow popupWindow;
    private boolean is_home_welfare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_welfare);

        //初始化布局
        initView();
        //绑定数据
        initData();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        final Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);

        //是否是从主界面开启的我的福利界面
        is_home_welfare = intent.getBooleanExtra("is_home_welfare", false);

        //请求接口获取我的福利数量
        getMyWelfare();

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                if(is_home_welfare){//是从主界面打开的 我的福利，那么按返回键时就回到更多界面
//                    Intent intent1=new Intent(MyWelfareActivity.this, MainActivity.class);
//                    intent1.putExtra("index",3);
//                    startActivity(intent1);
//                    finish();
//                }else {
                    finish();
                //}

            }
        });

        mHelperViewPager = (ViewPager) findViewById(R.id.helper_view_pager);

        mInvesterFrame = (FrameLayout) findViewById(R.id.invester_frame);
        mCommonFrame = (FrameLayout) findViewById(R.id.common_frame);
        mMobileFrame = (FrameLayout) findViewById(R.id.mobile_frame);

        mInvesterFrame.setOnClickListener(this);
        mCommonFrame.setOnClickListener(this);
        mMobileFrame.setOnClickListener(this);

        mInvesterTv = (TextView) findViewById(R.id.invester_tv);
        mCommonTv = (TextView) findViewById(R.id.common_tv);
        mMobileTv = (TextView) findViewById(R.id.mobile_tv);

        mInvesterLine = findViewById(R.id.invester_line);
        mCommonLine = findViewById(R.id.common_line);
        mMobileLine = findViewById(R.id.mobile_line);

        mBtnUseRule = (Button) findViewById(R.id.btn_use_rule);//使用规则


        //RadioButton的两个按钮
        mRbCheck1 = (RadioButton) findViewById(R.id.rb_check1);
        mRbCheck2 = (RadioButton) findViewById(R.id.rb_check2);


        initViewPager();
    }

    /**
     * 绑定数据
     */
    private void initData() {
        //使用规则
        mBtnUseRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出使用规则的弹框
                showPopupWindow(view);
            }
        });

        //按代金券面值排序
        mRbCheck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction("welfare_number_sort");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("isMoney",true);
                sendBroadcast(intent);
            }
        });
        //按代代金券有效期排序
        mRbCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction("welfare_term_validity");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("isMoney",false);
                sendBroadcast(intent);
            }
        });

    }

    private void initViewPager() {
        fragments.add(new UnUserdFragmnet()); //未使用界面
        fragments.add(new UserdFragmnet()); //已使用界面
        fragments.add(new ExpiredFragmnet());//已过期界面

        MyInvestMentFragmentAdapter adapter =
                new MyInvestMentFragmentAdapter(fragments, this.getSupportFragmentManager(), mHelperViewPager);
        adapter.setOnExtraPageChangeListener(new MyInvestMentFragmentAdapter.OnExtraPageChangeListener() {
            public void onExtraPageSelected(int i) {
                switchBtnList(i);
            }
        });
    }

    private void switchBtnList(int index) {
        switch (index) {
            case 0:
                mInvesterTv.setTextColor(getResources().getColor(R.color.main_color));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.text_gray));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                break;
            case 1:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.main_color));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.text_gray));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));
                break;
            case 2:
                mInvesterTv.setTextColor(getResources().getColor(R.color.text_gray));
                mInvesterLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mCommonTv.setTextColor(getResources().getColor(R.color.text_gray));
                mCommonLine.setBackgroundColor(getResources().getColor(R.color.helper_center_line));

                mMobileTv.setTextColor(getResources().getColor(R.color.main_color));
                mMobileLine.setBackgroundColor(getResources().getColor(R.color.helper_center_select_line));
                break;

            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invester_frame: //未使用
                mHelperViewPager.setCurrentItem(0);
                break;
            case R.id.common_frame: //已使用
                mHelperViewPager.setCurrentItem(1);
                break;
            case R.id.mobile_frame: //已过期
                mHelperViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }

    }

    /**
     * 请求接口获取我的福利数量
     */
    public void getMyWelfare() {
        String url="/api/users/"+userId+"/vouchers-count";
        url= UrlHelper.getUrl(url);

        JSONObject jsonObject=new JSONObject();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObj=response;
                        Lg.e("获取数据成功",jsonObj.toString());
                        if(jsonObj!=null){
                            String unused = jsonObj.optString("UNUSED");//未使用
                            String used = jsonObj.optString("USED");    //已经使用
                            String expired = jsonObj.optString("EXPIRED");//已经过期
                            //设置各种状态代金券数量
                            mInvesterTv.setText("未使用("+unused+")");
                            mCommonTv.setText("已使用("+used+")");
                            mMobileTv.setText("已过期("+expired+")");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String errorMsg=new String(error.networkResponse.data);
                    JSONObject json=new JSONObject(errorMsg);
                    if(json!=null){
                        String message = (String) json.opt("message");
                        if(message!=null){
                            AlertUtil.showOneBtnDialog(MyWelfareActivity.this, message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }else {
                            AlertUtil.showOneBtnDialog(MyWelfareActivity.this, "获取数据失败", new View.OnClickListener() {
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
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
     * 显示使用规则的弹窗
     */
    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(MyWelfareActivity.this).inflate(
                R.layout.pop_window, null);
        // 设置按钮的点击事件
        Button button = (Button) contentView.findViewById(R.id.button1);


        popupWindow = new PopupWindow(contentView,
               ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT, true);


        popupWindow.setTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);


        //设置弹窗打开时，背景父窗体的透明度
        WindowManager.LayoutParams params=MyWelfareActivity.this.getWindow().getAttributes();
        params.alpha=0.3f;
        MyWelfareActivity.this.getWindow().setAttributes(params);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow=null;

                    //设置弹窗关闭时，背景父窗体的透明度
                    WindowManager.LayoutParams params=MyWelfareActivity.this.getWindow().getAttributes();
                    params.alpha=1f;
                    MyWelfareActivity.this.getWindow().setAttributes(params);
                }
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.common_item_white_sele));
        //设置popupWindow显示的位置
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0,0);

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }


//    @Override
//    public void onBackPressed() {
//        if(is_home_welfare){
//            Intent intent1=new Intent(MyWelfareActivity.this, MainActivity.class);
//            intent1.putExtra("index",3);
//            startActivity(intent1);
//            finish();
//        }else {
//            finish();
//        }
//        super.onBackPressed();
//    }
}
