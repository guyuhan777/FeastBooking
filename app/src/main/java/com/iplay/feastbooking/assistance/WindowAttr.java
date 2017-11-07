package com.iplay.feastbooking.assistance;

import android.app.Activity;

import java.util.HashMap;

/**
 * Created by admin on 2017/7/25.
 */

public class WindowAttr {

    private static HashMap<String, String> orderStatusEn2ChMap = new HashMap<>();

    static {
        orderStatusEn2ChMap.put("CONSULTING","咨詢中");
    }

    public static int getStatusBarHeight(Activity a){
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId > 0){
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getOrderStatusCh(String enKey){
        return orderStatusEn2ChMap.get(enKey);
    }

}
