package com.iplay.feastbooking.assistance;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by admin on 2017/9/27.
 */

public class ProperTies {

    public static final int TYPE_BTN_ACTIVE = 1;

    public static final int TYPE_BTN_WAITING = 2;

    public static final int TYPE_BTN_DISABLE = 3;

    public static Properties getProperties(Context context){
        Properties properties = new Properties();
        try {
            InputStream in = context.getAssets().open("appConfig");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
