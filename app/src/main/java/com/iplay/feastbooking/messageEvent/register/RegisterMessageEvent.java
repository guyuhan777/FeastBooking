package com.iplay.feastbooking.messageEvent.register;

/**
 * Created by admin on 2017/10/7.
 */

public class RegisterMessageEvent {

    public static final int TYPE_NO_INTERNET = 1;

    public static final int TYPE_CONNECT_TIME_OUT = 2;

    public static final int TYPE_FAILURE = 3;

    public static final int TYPE_SUCCESS = 4;

    public static final int TYPE_UNKNOWN_ERROR = 5;

    private int type;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
