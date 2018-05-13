package com.guoou.sdk.demo;

import android.app.Application;

import com.guoou.sdk.global.SdkManager;

/**
 * Created by LGB on 2017/7/19.
 */

public class GoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SdkManager.getInstance().init(this);
    }
}
