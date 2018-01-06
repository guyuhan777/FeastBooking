package com.iplay.feastbooking.application;

import org.litepal.LitePalApplication;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by gu_y-pc on 2018/1/6.
 */

public class FeastBookingApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
    }
}
