package com.jia16.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jia16.R;
import com.jia16.base.BaseActivity;
import com.jia16.base.BaseApplication;
import com.jia16.util.AlertUtil;
import com.jia16.util.DMAlertUtil;
import com.jia16.util.FormatUtil;
import com.jia16.util.Lg;
import com.jia16.util.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改用户名界面
 *
 * @author jiaohongyun
 * @date 2015年5月25日
 */
public class ModificationUserActivity extends BaseActivity {


    /**
     * 修改用户名输入框
     */
    private EditText mModificationUser;

    /**
     * 修改用户完成按钮
     */
    private Button mBtnFinish;
    /**
     * 我的账户界面传递过来的用户id
     */
    private int userid;
    /**
     * //我的账户界面传递过来的用户名
     */
    private String oldUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_user);
        initView();
    }


    /**
     * 初始化页面元素
     */
    @Override
    protected void initView() {
        super.initView();

        ((TextView)findViewById(R.id.title_text)).setText("修改用户名");

        //修改用户名输入框
        mModificationUser = (EditText) findViewById(R.id.et_modification_user);

        final Intent intent = getIntent();
        //我的账户界面传递过来的用户名
        oldUserName = intent.getStringExtra("userName");
        userid = intent.getIntExtra("userid",0);//我的账户界面传递过来的用户id
        Lg.e("userid.......***",userid);
        mModificationUser.setText(oldUserName);

        //完成按钮
        mBtnFinish = (Button) findViewById(R.id.btn_finish);
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防重复点击
                if(checkClick(v.getId())){
                    //检查参数
                    if(checkParams()){

                        String url="/ums/users/"+userid+"/user-name";
                        url = UrlHelper.getUrl(url);
                        Lg.e("修改用户名的接口",url);

                        JSONObject jsonObject=new JSONObject();

                        try {
                            jsonObject.put("userName",mModificationUser.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest stringRequest=new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        AlertUtil.showOneBtnDialog(ModificationUserActivity.this, "修改成功", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        });
                                        //修改成功,回到我的账户界面
                                        String newUserName = mModificationUser.getText().toString();
                                        Intent intent1 = getIntent();
                                        intent.putExtra("newUserName",newUserName);
                                        setResult(RESULT_OK,intent);
                                        finish();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                try {
                                    String errorMessage = new String(error.networkResponse.data);
                                    JSONObject objDat=new JSONObject(errorMessage);
                                    String message = objDat.getString("message");
                                    if(message!=null){
                                        AlertUtil.showOneBtnDialog(ModificationUserActivity.this, message, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //点击确定后，关闭该界面
                                                finish();
                                            }
                                        });
                                    }else{
                                        AlertUtil.showOneBtnDialog(ModificationUserActivity.this, "修改用户名失败", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<>();
                                String token = sharedPreferences.getString("_csrf", "");
                                String cookie = sharedPreferences.getString("Cookie", "");
                                Lg.e("token", token, "cookie", cookie);
                                headers.put("CSRF-TOKEN", sharedPreferences.getString("_csrf", ""));
                                headers.put("Cookie", sharedPreferences.getString("Cookie", ""));
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json";
                            }
                        };

                        BaseApplication.getRequestQueue().add(stringRequest);
                    }
                }

            }
        });

    }


    /**
     * 完成修改之前检查参数
     *
     * @param
     * @return
     */
    private boolean checkParams() {

        String newUsersName = mModificationUser.getText().toString();
        if (TextUtils.isEmpty(newUsersName)) {

            //用户名不能为空
            DMAlertUtil.showOneBtnDialog(ModificationUserActivity.this, getString(R.string.find_user_name), false);
            return false;
        }
        if(newUsersName.contains(" ")){
            //不能带有空格字符
            DMAlertUtil.showOneBtnDialog(ModificationUserActivity.this,"格式不正确，不能带有空格字符",false);
            return false;
        }

        if(newUsersName.equals(oldUserName)){
            DMAlertUtil.showOneBtnDialog(ModificationUserActivity.this,"您还未修改，请修改用户名！");
            return false;
        }

        if(newUsersName.length()<=6){
            DMAlertUtil.showOneBtnDialog(ModificationUserActivity.this,"用户名必须要6位以上，请重新输入",false);
            return false;
        }
        if(!FormatUtil.validateuserName(newUsersName)){
            DMAlertUtil.showOneBtnDialog(ModificationUserActivity.this,"用户名格式不对,请重新输入!",false);
            return false;
        }

        return true;
    }

}
