package com.yizhi.android.juhe;

import android.app.Application;

import com.zhaohang.juhe.location.base.JuHe;

import xyz.cq.clog.CLog;

/**
 * @author 程前 created on 2018/12/6.
 * blog: https://blog.csdn.net/ch1406285246
 * content:
 * modifyNote:
 */
public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CLog.INSTANCE.baseLog("YZJuHe").isLog(BuildConfig.DEBUG);
        //使用juhe_v1.0.4可不调用该方法,不调用该方法需要调用新的location方法
        JuHe.init(this);
    }
}
