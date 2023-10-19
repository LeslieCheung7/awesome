package com.yoimiya.awesome.network;

import android.app.Application;

public interface INetworkInfo {

    String getAppVersionName();

    String getAppVersionCode();

    boolean isDebug();

    Application getApplicationContext();
}
