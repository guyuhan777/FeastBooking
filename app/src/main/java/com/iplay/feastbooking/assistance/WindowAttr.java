package com.iplay.feastbooking.assistance;

import android.app.Activity;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by admin on 2017/7/25.
 */

public class WindowAttr {
    public static int getStatusBarHeight(Activity a){
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId > 0){
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
