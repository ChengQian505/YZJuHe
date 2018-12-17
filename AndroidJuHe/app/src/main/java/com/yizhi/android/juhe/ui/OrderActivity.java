package com.yizhi.android.juhe.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.common.StringUtils;
import com.yizhi.android.juhe.R;
import com.yizhi.android.juhe.utils.TestUtil;
import com.yizhi.android.juhe.utils.pay.OrderQueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.cq.clog.CLog;

public class OrderActivity extends AppCompatActivity {

    private static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle(title);
        Button btn_query = findViewById(R.id.btn_query);
        final TextView et_query = findViewById(R.id.et_query);
        final TextView text_return = findViewById(R.id.text_return);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderQueryUtils.get(et_query.getText().toString(), new TestUtil.CallBack1() {
                    @Override
                    public void onSuccess(final String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //查询结果为json字符串，转换为json对象
                                try {
                                    JSONObject resJson = new JSONObject(result);
                                    //订单金额
                                    String orderMoney = resJson.getString("orderMoney");
                                    //支付结果
                                    Integer payResult = resJson.getInt("payResult");
                                    //平台订单号
                                    String orderNo = resJson.getString("orderNo");
                                    text_return.setText("返回结果：\n" + result.replace(",","\n") + "\n订单金额：" + orderMoney + "\n支付结果:" + payResult + "\n平台订单号:" + orderNo);
                                } catch (JSONException e) {
                                    text_return.setText("订单解析异常:" + e.getMessage());
                                    CLog.log().e("Order", "订单解析", e);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFail(final Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text_return.setText("订单查询异常:" + t.getMessage());
                            }
                        });
                        CLog.log().e("Order", "订单查询", t);
                    }
                });
            }
        });
    }

    public static void startActivity(Activity activity, String title) {
        OrderActivity.title = title;
        activity.startActivity(new Intent(activity, OrderActivity.class));
    }

}
