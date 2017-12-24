package com.iplay.feastbooking.messageEvent.favourite;

import com.iplay.feastbooking.ui.recommendedHotel.data.HotelHomeData;

import java.util.List;

/**
 * Created by gu_y-pc on 2017/12/24.
 */

public class FavouriteHotelMessageEvent {

    private boolean isInit = false;

    private int type;

    private String failureReason;

    public static final int TYPE_SUCCESS = 1;

    public static final int TYPE_FAILURE = 2;

    private List<HotelHomeData> homeDatas;

    public List<HotelHomeData> getHomeDatas() {
        return homeDatas;
    }

    public void setHomeDatas(List<HotelHomeData> homeDatas) {
        this.homeDatas = homeDatas;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
