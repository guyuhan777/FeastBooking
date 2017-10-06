package com.iplay.feastbooking.messageEvent;

/**
 * Created by admin on 2017/10/6.
 */

public class HotelListNoInternetMessageEvent {

    public static final int TYPE_INIT = 1;

    public static final int TYPE_LOAD_MORE = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

}
