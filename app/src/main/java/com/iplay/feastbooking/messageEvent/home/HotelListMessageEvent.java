package com.iplay.feastbooking.messageEvent.home;

import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;

import java.util.List;

/**
 * Created by admin on 2017/10/6.
 */

public class HotelListMessageEvent {

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status{SUCCESS, FAILURE};

    private List<RecommendHotelGO> hotels;

    private boolean isInit;

    private Status status;

    private String failureReason;

    public List<RecommendHotelGO> getHotels() {
        return hotels;
    }

    public void setHotels(List<RecommendHotelGO> hotels) {
        this.hotels = hotels;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
