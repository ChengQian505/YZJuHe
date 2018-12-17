package com.yizhi.android.juhe.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yizhi.android.juhe.R;

import java.util.HashMap;
import java.util.Map;

import xyz.cq.clog.CLog;

public class WebActivity extends AppCompatActivity {

    private static String title;
    private static String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setTitle(title);
        WebView webview = findViewById(R.id.webView);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);//这行必须
        /*
           判断支付宝协议进行应用跳转，不进入浏览器
         */
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                CLog.log().i("loadUrl", url);
                if (url.startsWith("alipays://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
        webview.loadUrl(url);
    }

    public static void startActivity(Activity activity, String title, String url) {
        WebActivity.title = title;
        WebActivity.url = url;
        activity.startActivity(new Intent(activity, WebActivity.class));
    }

}
