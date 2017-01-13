package com.jia16.assets;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.jia16.util.AmountUtil;
import com.jia16.util.Lg;
import com.jia16.util.ToastUtil;
import com.jia16.util.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的资产--申请转让--确认界面
 */
public class ApplyTransferAffirmActivity extends BaseActivity {

    /**
     * ApplyTransferNextActivity界面传递过来的数据
     */
    private String title;//标的名称
    private double transferAmount;//转让价格
    private double practicalAmount;//预计到账金额
    private int transferDate;//转让天数
    private int investsId;//投资的id
    private int subjectId;//subjectid申请转让
    private String floatAmount;//涨或者降价的金额
    private double transferPrincipal;//转让本金

    /**
     * 初始化数据
     */
    private TextView mTvTransferName;//表的名称
    private TextView mTvTransferPrice;//转让价格
    private TextView mTvPracticalAmount;//预计到账金额
    private TextView mTvTransferDate;//转让期限
    private Button mBtAffirmTransfer;//确认转让按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_fransfer_affirm);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        transferAmount = intent.getDoubleExtra("transferAmount", 0);
        practicalAmount = intent.getDoubleExtra("practicalAmount", 0);
        transferDate = intent.getIntExtra("transferDate", 0);
        investsId = intent.getIntExtra("investsId", 0);
        subjectId = intent.getIntExtra("subjectId", 0);
        floatAmount = intent.getStringExtra("floatAmount");
        transferPrincipal = intent.getDoubleExtra("transferPrincipal", 0);

        //初始化布局
        initView();

        //绑定数据
        initData();
    }



    /**
     * 初始化布局
     */
    public void initView() {

        ((TextView) findViewById(R.id.title_text)).setText("确认转让");

        // 设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvTransferName = (TextView) findViewById(R.id.tv_transfer_name);
        mTvTransferPrice = (TextView) findViewById(R.id.tv_transfer_price);
        mTvPracticalAmount = (TextView) findViewById(R.id.tv_practical_amount);
        mTvTransferDate = (TextView) findViewById(R.id.tv_transfer_date);
        mBtAffirmTransfer = (Button) findViewById(R.id.bt_affirm_transfer);
        mBtAffirmTransfer.setOnClickListener(this);

    }


    /**
     * 绑定数据
     */
    private void initData() {
        mTvTransferName.setText(title);
        mTvTransferPrice.setText(AmountUtil.addComma(AmountUtil.DT.format(transferAmount))+"元");
        mTvPracticalAmount.setText(AmountUtil.addComma(AmountUtil.DT.format(practicalAmount))+"元");
        mTvTransferDate.setText(transferDate+"天");

    }


    /**
     * 请求需要申请标的数据
     */
    private void postApplyFransferData() {
        showLoadingDialog();

        long timeStamp=System.currentTimeMillis();

        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        int userId=0;
        if(userInfo!=null){
            userId = userInfo.getId();
        }
        String url="/api/users/"+userId+"/subjects";
        url= UrlHelper.getUrl(url);

        JSONObject jsonObject = new JSONObject();
        JSONObject dic = new JSONObject();
        JSONObject jsoned = new JSONObject();
        try {
            dic.put("amount", floatAmount);//涨或者降价的金额
            dic.put("currency", "CNY");
            jsonObject.put("adjustmentAmount", dic);

            jsoned.put("amount",transferPrincipal);//转让本金
            jsoned.put("currency", "CNY");
            jsonObject.put("amount", jsoned);

            jsonObject.put("catalog", "JIASHI_V5");
            jsonObject.put("contractId",investsId);
            jsonObject.put("transferDays",transferDate);
            jsonObject.put("transferFeeRate",0);
            jsonObject.put("type","TRANSFER");

            jsonObject.put("timestamp", String.valueOf(timeStamp));
            jsonObject.put("subjectId",subjectId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                dimissLoadingDialog();

                //转让成功后，那么
                ToastUtil.getInstant().show(ApplyTransferAffirmActivity.this,"恭喜您，转让申请已经提交");

                //发送广播，到我的资产界面，重新请求数据，选中转让中
                Intent intent=new Intent();
                intent.setAction("transfer_apply_success_refresh_interface");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                sendBroadcast(intent);

                //关闭当前界面
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dimissLoadingDialog();

                AlertUtil.showOneBtnDialog(ApplyTransferAffirmActivity.this, "转让失败", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
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

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.bt_affirm_transfer://确认转让按钮

                postApplyFransferData();

            break;

            default:
                break;
        }
        super.onClick(view);
    }


    /**
     * 点击下一步前，检查转让本金数额是否正确
     */
//    private boolean checkParams(boolean getVerifyCode) {
//
//        String TransferAmount = mEtAmount.getText().toString();
//
//
//        if (TextUtils.isEmpty(TransferAmount)) {
//
//            //转让本金数额不能为空
//            //DMAlertUtil.showOneBtnDialog(ApplyTransferActivity.this,"转让本金数额不能为空",false);
//            ToastUtil.getInstant().show(ApplyTransferAffirmActivity.this,"转让本金数额不能为空");
//            return false;
//        }
//        //获取输入框中的转让金额
//        getTransferAmount = Double.parseDouble(TransferAmount);
//
//        if (getTransferAmount>transfermount) {
//
//            //DMAlertUtil.showOneBtnDialog(ApplyTransferActivity.this, "最高不能超过可转让金额", false);
//            ToastUtil.getInstant().show(ApplyTransferAffirmActivity.this,"最高不能超过可转让金额");
//            return false;
//        }
//
//        if (getTransferAmount<minInvestAmount) {
//
//            //DMAlertUtil.showOneBtnDialog(ApplyTransferActivity.this, "剩余金额不能小于起投金额", false);
//            ToastUtil.getInstant().show(ApplyTransferAffirmActivity.this,"剩余金额不能小于起投金额");
//            return false;
//        }
//
//        return true;
//    }
}
