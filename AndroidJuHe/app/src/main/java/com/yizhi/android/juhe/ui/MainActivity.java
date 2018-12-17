package com.yizhi.android.juhe.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yizhi.android.juhe.R;
import com.yizhi.android.juhe.utils.TestUtil;
import com.yizhi.android.juhe.utils.pay.AlipayAppUtils;
import com.yizhi.android.juhe.utils.pay.MiniProgramUtils;
import com.yizhi.android.juhe.utils.pay.QuickPayUtils;
import com.yizhi.android.juhe.wxapi.WXEntryActivity;
import com.zhaohang.juhe.location.base.CallBack;
import com.zhaohang.juhe.location.base.JuHe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import xyz.cq.clog.CLog;

/**
 * @author qian.cheng
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WXEntryActivity.api = WXAPIFactory.createWXAPI(this, "wx1e2929686b30ef32", false);
    }


    public void alipayApp(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("alipays://platformapi/startApp?appId=10000011&url=" + URLEncoder.encode(AlipayAppUtils.getUrl(), "UTF-8")));
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void alipayCode(View view) {
        CodeActivity.startActivity(this, "支付宝付款码");
    }

    public void wXpayCode(View view) {
        CodeActivity.startActivity(this, "微信付款码");
    }

    public void quickPay(View view) {
        try {
            WebActivity.startActivity(this, "快捷支付", QuickPayUtils.getUrl());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void orderQuery(View view) {
        OrderActivity.startActivity(this, "订单查询");
    }


    /**
     * 需要在Application里面调用init方法
     */
    public void jumpApplet1(View view) {
        //请求权限可以自己处理，定位权限需要开启，位置服务需要打开
        JuHe.requestLocationPermissions(this, new CallBack() {
            @Override
            public void onSuccess(String address) {
                JuHe.location(MainActivity.this, new CallBack() {
                    @Override
                    public void onSuccess(final String address) {
                        CLog.log().i("MainA", address);
                        request(address);
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        CLog.show(MainActivity.this, "errorcode:" + errorCode + "  msg:" + msg);
                    }
                });
            }

            @Override
            public void onFail(int errorCode, String msg) {
                CLog.show(MainActivity.this, "errorcode:" + errorCode + "  msg:" + msg);
            }
        });
    }

    //纬度
    private String latitude = "31.116585";
    //经度
    private String longitude = "121.378571";

    /**
     * 需要自己传入定位信息,保证传入信息的准确性，不能传固定经纬度
     */
    public void jumpApplet2(View view) {
        JuHe.location(this, new CallBack() {
            @Override
            public void onSuccess(final String address) {
                CLog.log().i("MainA", address);
                request(address);
            }

            @Override
            public void onFail(int errorCode, String msg) {
                CLog.show(MainActivity.this, "errorcode:" + errorCode + "  msg:" + msg);
            }
        }, latitude, longitude);
    }

    public void jumpApplet3(View view) {
        try {
            JSONObject location = new JSONObject();
            location.put("latitude", latitude);
            location.put("longitude", latitude);
            JSONObject address = new JSONObject();
            address.put("location", location);
            CLog.log().i(address.toString());
            MiniProgramUtils.post(new TestUtil.CallBack1() {
                @Override
                public void onSuccess(String result) {
                    CLog.log().i("MainA", result);
                    //需要导入微信包  implementation 'com.tencent.mm.opensdk:wechat-sdk-android-with-mta:+'
                    try {
                        JSONObject jsonObject = new JSONObject(new JSONObject(result).get("json").toString());
                        String appid = jsonObject.getString("appid");
                        String miniId = jsonObject.getString("gid");
                        String path1 = jsonObject.getString("requestParam");
                        int jsonType = jsonObject.getInt("jsonType");
                        if (jsonType == 0) {
                            path1 = "json=" + path1;
                        } else {
                            path1 = "json=" + path1 + "&jsonType=" + jsonType;
                        }
                        IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, appid, false);
                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                        // 填小程序原始id
                        req.userName = miniId;
                        //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                        req.path = "pages/pay/pay" + "?" + path1;
                        // 可选打开 开发版，体验版和正式版
                        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
                        api.sendReq(req);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(Throwable t) {
                    CLog.log().d("MainA", "请求异常", t);
                }
            }, address.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void request(String address) {
        MiniProgramUtils.post(new TestUtil.CallBack1() {
            @Override
            public void onSuccess(String result) {
                CLog.log().i("MainA", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JuHe.smallProgramJump(MainActivity.this, new CallBack() {
                        @Override
                        public void onSuccess(String s) {

                        }

                        @Override
                        public void onFail(int i, String s) {
                            CLog.show(MainActivity.this, "errorcode:" + i + "  msg:" + s);
                        }
                    }, jsonObject.get("json").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Throwable t) {
                CLog.log().d("MainA", "请求异常", t);
            }
        }, address);
    }


}
