package com.iplay.feastbooking.messageEvent.activityFinish;

import java.util.HashSet;

/**
 * Created by admin on 2017/10/7.
 */

public class ActivityFinishMessageEvent {

    private HashSet<String> activitySet = new HashSet<>();

    public void put(String tag){
        activitySet.add(tag);
    }

    public boolean isExist(String tag){
        return activitySet.contains(tag);
    }
}
