package com.yizhi.android.juhe.utils.pay;

import android.util.Log;

import com.yizhi.android.juhe.utils.TestUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import xyz.cq.clog.CLog;

/**
 * @author 程前 on 2018/9/20.
 * blog: https://blog.csdn.net/ch1406285246
 * company: 上海兆行
 * content:支付宝付款码
 * modifyNote:
 */
public class AlipayCodeUtils {

    // 测试用参数值
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    static String sdate = sdf.format(new Date());// 格式化当前时间
    static String merid = "yft2017082500005";// 分配的商户号
    static String noncestr = "test2";// 随机字符串，内容自定义
    static String orderMoney = "1.00";// 订单金额 单位 元
    static String orderTime = sdate;// 下单时间
    static String sign = "";// 签名 请
    static String key = "gNociwieX1aCSkhvVemcXkaF9KVmkXm8";// 商户号对应的密钥
    static String notifyUrl = "ceshi";// 用于接收回调通知的地址
    static String id = "测试";// 不参与验签
    // 二维码请求地址 以接口文档为准
    static String scanPayUrl = "https://alipay.3c-buy.com/api/createPcOrder";

    public static void getBitmap(final TestUtil.CallBack2 callBack) {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("merchantOutOrderNo", sdf.format(new Date()));// 订单号，根据实际情况生成
        paraMap.put("merid", merid);
        paraMap.put("noncestr", noncestr);
        paraMap.put("orderMoney", orderMoney);
        paraMap.put("orderTime", orderTime);
        paraMap.put("notifyUrl", notifyUrl);
        /**
         * 对参数按照 key=value 的格式，并参照参数名 ASCII 码排序后得到字符串 stringA
         */
        String stringA = TestUtil.formatUrlMap(paraMap, false, false);
        /**
         * 在 stringA 最后拼接上 key 得到 stringsignTemp 字符串， 并对 stringsignTemp 进行 MD5 运算，得到
         * sign 值
         */
        String stringsignTemp = stringA + "&key=" + key;
        sign = TestUtil.getMD5(stringsignTemp);

        /**
         * 对参数按照 key=value 的格式,参照参数名 ASCII 码排序,并对value做utf-8的encode编码后得到字符串 param
         */
        String param = TestUtil.formatUrlMap(paraMap, true, false);
        final String url = scanPayUrl + "?" + param + "&sign=" + sign + "&id=" + id;
        CLog.log().i("alipayCode",url);
        /**
         * 根据url发送post请求，返回一个json 的 String字符串
         */
        new Thread(){
            @Override
            public void run() {
                TestUtil.sendPost(callBack,url, null, "UTF-8");
            }
        }.start();
    }

}
