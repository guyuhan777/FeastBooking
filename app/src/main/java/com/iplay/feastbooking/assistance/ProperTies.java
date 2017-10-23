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

    private static volatile Properties instance;

    public static Properties getProperties(Context context){
        if(instance == null){
            synchronized (ProperTies.class){
                if(instance == null){
                    instance = new Properties();
                    try {
                        InputStream in = context.getApplicationContext().getAssets().open("appConfig");
                        instance.load(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

}
