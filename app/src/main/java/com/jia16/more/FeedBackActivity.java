package com.jia16.more;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.bean.UserInfo;
import com.jia16.util.AlertUtil;
import com.jia16.util.DMAlertUtil;
import com.jia16.util.FormatUtil;
import com.jia16.util.ToastUtil;
import com.jia16.util.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 我要反馈界面
 */
public class FeedBackActivity extends BaseActivity {

    private static String OPTIMIZE_PAGE = "OPTIMIZE_PAGE";    //页面优化
    private static String SYSTEM_PROBLEM = "SYSTEM_PROBLEM";   //系统问题
    private static String NEED_NEW_FEATURES = "NEED_NEW_FEATURES";    //需要新功能
    private static String OTHERS = "OTHERS";                //其他
    private String feedbackType = null;

    /**
     * 4个单选框按钮
     */
    private RadioButton mRbNum1;
    private RadioButton mRbNum2;
    private RadioButton mRbNum3;
    private RadioButton mRbNum4;
    /**
     * 意见文本输入框
     */
    private EditText mEtFeedBack;
    /**
     * 当前输入的字数
     */
    private TextView mTvLeng;
    /**
     * 提交按钮
     */
    private Button mBtSubject;
    private RadioGroup mrbButton;
    private int userId;
    private UserInfo userInfo;

    private EditText mEtNologinPhone;//联系方式输入框
    private View mEtNologinDevider; //分隔线

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        //初始化布局
        initView();
        //绑定数据
        initData();
    }

    /**
     * 初始化布局
     */
    public void initView() {

        ((TextView)findViewById(R.id.title_text)).setText("我要反馈");

        userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo != null) {
            userId = userInfo.getId();
        }

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRbNum1 = (RadioButton) findViewById(R.id.rb_num1);
        mRbNum2 = (RadioButton) findViewById(R.id.rb_num2);
        mRbNum3 = (RadioButton) findViewById(R.id.rb_num3);
        mRbNum4 = (RadioButton) findViewById(R.id.rb_num4);

        mrbButton = (RadioGroup) findViewById(R.id.rg_button);

        //意见文本输入框
        mEtFeedBack = (EditText) findViewById(R.id.et_feed_back);
        //当前输入的字数
        mTvLeng = (TextView) findViewById(R.id.tv_leng);

        //提交按钮
        mBtSubject = (Button) findViewById(R.id.bt_subject);

        //联系方式
        mEtNologinPhone = (EditText) findViewById(R.id.et_nologin_phone);

        //分隔线
        mEtNologinDevider = findViewById(R.id.et_nologin_devider);

        if (BaseApplication.getInstance().isLogined()) {
            //线表示已经登录，那么就隐藏联系方式输入框和分隔
            mEtNologinPhone.setVisibility(View.GONE);
            mEtNologinDevider.setVisibility(View.GONE);
        } else {
            //线表示用户还没有登录，那么就显示联系方式输入框和分隔
            mEtNologinPhone.setVisibility(View.VISIBLE);
            mEtNologinDevider.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 绑定数据
     */
    private void initData() {
        //动态获取输入的字数
        mEtFeedBack.addTextChangedListener(mTextWatcher);

        //提交按钮
        mBtSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交按钮具体操作
                feedBackSubject();
            }
        });


        mRbNum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRbNum1.setChecked(true);
                feedbackType = OPTIMIZE_PAGE;
                mRbNum2.setChecked(false);
                mRbNum3.setChecked(false);
                mRbNum4.setChecked(false);
            }
        });

        mRbNum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRbNum1.setChecked(false);
                mRbNum2.setChecked(true);
                feedbackType = SYSTEM_PROBLEM;
                mRbNum3.setChecked(false);
                mRbNum4.setChecked(false);
            }
        });
        mRbNum3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRbNum1.setChecked(false);
                mRbNum2.setChecked(false);
                mRbNum3.setChecked(true);
                feedbackType = NEED_NEW_FEATURES;
                mRbNum4.setChecked(false);
            }
        });


        mRbNum4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRbNum1.setChecked(false);
                mRbNum2.setChecked(false);
                mRbNum3.setChecked(false);
                mRbNum4.setChecked(true);
                feedbackType = OTHERS;
            }
        });

    }

    /**
     * 提交按钮具体操作
     */
    private void feedBackSubject() {

        String url = "/ums/feedback";
        url = UrlHelper.getUrl(url);
        JSONObject jsonObject=null;

        if (userInfo != null) {//表示已经登录

            if (checkParams(true)) {//在登录情况下检查参数
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("feedbackContent", mEtFeedBack.getText().toString());
                    jsonObject.put("contactMethod", "");
                    jsonObject.put("feedbackType", feedbackType);
                    jsonObject.put("userId", userId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } else {//表示还没有登录
            if (checkParams(false)) {//在没有登录情况下检查参数

                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("feedbackContent", mEtFeedBack.getText().toString());
                    jsonObject.put("contactMethod", mEtNologinPhone.getText().toString());
                    jsonObject.put("feedbackType", feedbackType);
                    jsonObject.put("userId", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AlertUtil.showOneBtnDialog(FeedBackActivity.this, "感谢您的反馈，我们将尽快对您的反馈" +
                                "进行回复，您的宝贵建议是我们不断前进的动力!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                removeFeedBack();
                                finish();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String errorMsg = new String(error.networkResponse.data);
                    JSONObject jsonError = new JSONObject(errorMsg);
                    String message = (String) jsonError.opt("message");
                    if (message != null) {
                        AlertUtil.showOneBtnDialog(FeedBackActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                removeFeedBack();
                                finish();
                            }
                        });
                    } else {
                        AlertUtil.showOneBtnDialog(FeedBackActivity.this, "提交反馈失败", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                removeFeedBack();
                                finish();
                            }
                        });
                    }
                    if (jsonError != null) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                if (userInfo != null) {
                    headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                    headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
                } else {
                    headers.put("CSRF-TOKEN", sharedPreferences.getString("find_csrf", ""));
                    headers.put("Cookie", sharedPreferences.getString("findCookie", ""));
                }
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
     * 反馈成功，失败，清理输入框中的内容
     */
    private void removeFeedBack() {
        //反馈成功，那么就清空输入框
        mEtFeedBack.setText("");
        //清除反馈类型的button
        mRbNum1.setChecked(false);
        mRbNum2.setChecked(false);
        mRbNum3.setChecked(false);
        mRbNum4.setChecked(false);
    }


    /**
     * 动态获取输入的字数，并设置在文本中
     */
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            editStart = mEtFeedBack.getSelectionStart();
            editEnd = mEtFeedBack.getSelectionEnd();
            mTvLeng.setText(temp.length() + "");
            if (temp.length() > 150) {
                ToastUtil.getInstant().show(FeedBackActivity.this, "您输入的字数已经超过限制");
                editable.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                mEtFeedBack.setText(editable);
                mEtFeedBack.setSelection(tempSelection);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            mTvLeng.setText(s);//将输入的内容实时显示
        }
    };

    @Override
    protected void onResume() {

        super.onResume();
    }


    /**
     * 提交反馈前检查参数
     */
    private boolean checkParams(boolean islogin) {

        String textBack = mEtFeedBack.getText().toString();

        if (TextUtils.isEmpty(textBack)) {

            //反馈内容不能为空
            DMAlertUtil.showOneBtnDialog(FeedBackActivity.this, "反馈内容不能为空", false);

            return false;
        }

        if (mRbNum1.isChecked() == false && mRbNum2.isChecked() == false && mRbNum3.isChecked() == false && mRbNum4.isChecked() == false) {
            //请选择反馈类型
            DMAlertUtil.showOneBtnDialog(FeedBackActivity.this, "请选择反馈类型", false);
            return false;
        }

        if(islogin){
            return true;
        }
        String noLoginPhone = mEtNologinPhone.getText().toString();

        if(TextUtils.isEmpty(noLoginPhone)){
            DMAlertUtil.showOneBtnDialog(FeedBackActivity.this, "请输入您的联系方式", false);
            return false;
        }

        //验证区分电话号码还是邮箱,
        if(noLoginPhone.contains("@")){
            if (!FormatUtil.checkEmail(noLoginPhone)) {
                DMAlertUtil.showOneBtnDialog(FeedBackActivity.this, "邮箱格式不正确", false);
                return false;
            }
        }
        if(!noLoginPhone.contains("@")){
            if (!FormatUtil.isMobileNO(noLoginPhone)) {
                DMAlertUtil.showOneBtnDialog(FeedBackActivity.this, "电话号码格式不正确", false);
                return false;
            }
        }


        return true;
    }

}
