package com.iplay.feastbooking.net.message;

import okhttp3.MediaType;

/**
 * Created by admin on 2017/9/28.
 */

public class UtilMessage<T> {

    public static final int TYPE_UNRECEIVED = 0;
    public static final int TYPE_RECEIVED = 1;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private T message;

    private final int TYPE;

    public UtilMessage(int type){
        TYPE = type;
    }

    public void setMessage(T message){
        this.message = message;
    }

    public int getType(){
        return TYPE;
    }

    public T getMessage(){
        return message;
    }
}
