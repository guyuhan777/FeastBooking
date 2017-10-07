package com.iplay.feastbooking.messageEvent.register;

/**
 * Created by admin on 2017/10/8.
 */

public class RegisterConfirmMessageEvent {

    public static final int TYPE_NO_INTERNET = 1;

    public static final int TYPE_CONNECT_TIME_OVER = 2;

    public static final int TYPE_FAILURE = 4;

    public static final int TYPE_UNKNOWN_ERROR = 5;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
