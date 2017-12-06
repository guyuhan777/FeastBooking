package com.iplay.feastbooking.messageEvent.selfInfo;

/**
 * Created by gu_y-pc on 2017/12/6.
 */

public class RefreshSelfInfoMessageEvent {
    private final int userId;

    public RefreshSelfInfoMessageEvent(int userId){
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
