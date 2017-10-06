package com.iplay.feastbooking.net.utilImpl.loginUtil;

import android.content.Context;

import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.net.utilImpl.recommendHotelUtil.RecommendHotelListUtility;

import java.util.Properties;

/**
 * Created by admin on 2017/10/7.
 */

public class LoginUtility {

    private static volatile LoginUtility utility;

    private Context mContext;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private LoginUtility(Context context){
        mContext = context;
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
    }

    public static LoginUtility getInstance(Context context){
        if(utility == null){
            synchronized (LoginUtility.class){
                utility = new LoginUtility(context);
            }
        }
        return utility;
    }
}
