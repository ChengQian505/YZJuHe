package com.yizhi.android.juhe.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.yizhi.android.juhe.R;
import com.yizhi.android.juhe.utils.TestUtil;
import com.yizhi.android.juhe.utils.pay.AlipayCodeUtils;
import com.yizhi.android.juhe.utils.pay.WXpayCodeUtils;
import xyz.cq.clog.CLog;

public class CodeActivity extends AppCompatActivity {
    private static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        setTitle(title);
        final ImageView imageView = findViewById(R.id.image);
        if (title.equals("支付宝付款码")) {
            AlipayCodeUtils.getBitmap(new TestUtil.CallBack2() {

                @Override
                public void onSuccess(final Bitmap bitmap) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFail(Throwable t) {
                    CLog.log().e("CodeA","支付宝付款码", t);
                }
            });
        } else if (title.equals("微信付款码")) {
            WXpayCodeUtils.getBitmap(new TestUtil.CallBack2() {

                @Override
                public void onSuccess(final Bitmap bitmap) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFail(Throwable t) {
                    CLog.log().e("CodeA", "微信付款码", t);
                }
            });
        }
    }


    public static void startActivity(Activity activity, String title) {
        CodeActivity.title = title;
        activity.startActivity(new Intent(activity, CodeActivity.class));
    }
}
