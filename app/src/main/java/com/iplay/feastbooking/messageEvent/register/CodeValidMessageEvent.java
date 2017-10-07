package com.iplay.feastbooking.messageEvent.register;

/**
 * Created by admin on 2017/10/7.
 */

public class CodeValidMessageEvent {

    public static final int TYPE_NO_INTERNET = 1;

    public static final int TYPE_CONNECT_TIME_OVER = 2;

    public static final int TYPE_EMAIL_ALREADY_EXIST = 3;

    public static final int TYPE_SUCCESS = 4;

    public static final int TYPE_UNKNOWN_ERROR = 5;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

}
