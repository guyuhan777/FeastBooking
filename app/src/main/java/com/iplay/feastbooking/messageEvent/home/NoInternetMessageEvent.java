package com.iplay.feastbooking.messageEvent.home;

/**
 * Created by admin on 2017/10/7.
 */

public class NoInternetMessageEvent {

    public static final int TYPE_LOGIN = 1;

    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
