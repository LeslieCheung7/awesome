package com.yoimiya.awesome.network;

import android.app.Application;

public interface INetworkInfo {

    /**
     * 获取App版本名
     */
    String getAppVersionName();

    /**
     * 获取App版本号
     */
    String getAppVersionCode();

    /**
     * 是否debug模式
     */
    boolean isDebug();

    /**
     * 获取全局Context
     */
    Application getApplicationContext();
}
