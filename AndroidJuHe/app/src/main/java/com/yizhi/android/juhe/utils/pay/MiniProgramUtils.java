package com.yizhi.android.juhe.utils.pay;

import com.yizhi.android.juhe.utils.MD5;
import com.yizhi.android.juhe.utils.TestUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * @author 程前 created on 2018/11/29.
 * blog: https://blog.csdn.net/ch1406285246
 * content:
 * modifyNote:
 */
public class MiniProgramUtils {
    // 测试用参数值
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public static void post(final TestUtil.CallBack1 callBack, String address){
        String payUrlString1="https://alipay.3c-buy.com/api/createWcOrder";
        String key="gNociwieX1aCSkhvVemcXkaF9KVmkXm8";
        TreeMap<String,String> paramMap=new TreeMap<String, String>();
        paramMap.put("merchantOutOrderNo",sdf.format(new Date()));
        paramMap.put("merid","yft2017082500005");
        paramMap.put("noncestr","ceshi");
        paramMap.put("notifyUrl", "http://jh.chinambpc.com/api/callback");
        paramMap.put("orderMoney","1");
        paramMap.put("orderTime","20180516150037");
        String StringA=TestUtil.formatUrlMap(paramMap,false, false);//待签名串
        String sign=MD5.MD5Encode(StringA+"&key="+key);//签名
        paramMap.put("address", address);
        String StringB=TestUtil.formatUrlMap(paramMap,true, false);
        final String url1=payUrlString1+"?"+StringB+"&sign="+sign;//请求链接
        new Thread(new Runnable() {
            @Override
            public void run() {
                TestUtil.sendPost(callBack,url1,null,"UTF-8");
            }
        }).start();
    }

}
