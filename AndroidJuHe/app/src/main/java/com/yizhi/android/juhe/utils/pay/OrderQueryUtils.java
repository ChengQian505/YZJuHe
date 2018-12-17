package com.yizhi.android.juhe.utils.pay;

import android.util.Log;

import com.yizhi.android.juhe.utils.TestUtil;

import java.util.HashMap;
import java.util.Map;

import xyz.cq.clog.CLog;

/**
 * @author 程前 on 2018/9/21.
 * blog: https://blog.csdn.net/ch1406285246
 * company: 上海兆行
 * content:订单查询
 * modifyNote:
 */
public class OrderQueryUtils {
    //想要查询的订单的订单号
    private static String merchantOutOrderNo="201808021000162261";
    //商户号
    private static String merid="yft2017082500005";
    //随机字符串，商户自定义
    private static String noncestr="123456789";
    //查询接口地址，以接口文档为准
    private static String queryUrl="http://jh.chinambpc.com/api/queryOrder";
    //商户密钥
    private static String key = "gNociwieX1aCSkhvVemcXkaF9KVmkXm8";

    public static void get(String merchantOutOrderNo, final TestUtil.CallBack1 callBack) {
        //拼装参数
        Map<String, String> param=new HashMap<String, String>();
        param.put("merchantOutOrderNo", merchantOutOrderNo);
        param.put("merid", merid);
        param.put("noncestr", noncestr);
        //将参数转换为key=value形式
        String paramStr= TestUtil.formatUrlMap(param, false, false);
        //在最后拼接上密钥
        String signStr=paramStr+"&key="+key;
        //MD5签名
        String sign=TestUtil.getMD5(signStr);
        //拼接签名
        paramStr=paramStr+"&sign="+sign;
        CLog.log().i("orderQuery",queryUrl);
        //发起查询
        final String finalParamStr = paramStr;
        new Thread(){
            @Override
            public void run() {
                TestUtil.sendPost(callBack,queryUrl, finalParamStr, "UTF-8");
            }
        }.start();
    }
}
