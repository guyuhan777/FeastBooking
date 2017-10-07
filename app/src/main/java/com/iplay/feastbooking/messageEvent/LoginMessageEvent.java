package com.iplay.feastbooking.messageEvent;

/**
 * Created by admin on 2017/10/7.
 */

public class LoginMessageEvent {

    public static final int TYPE_SUCCESS = 1;

    public static final int TYPE_CONNECT_TIME_OUT = 2;

    public static final int TYPE_FAILURE = 3;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;


}
