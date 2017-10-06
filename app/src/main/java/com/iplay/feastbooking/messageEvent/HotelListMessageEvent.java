package com.iplay.feastbooking.messageEvent;

import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;

import java.util.List;

/**
 * Created by admin on 2017/10/6.
 */

public class HotelListMessageEvent {

    public static final int TYPE_INIT = 1;

    public static final int TYPE_LOAD = 2;

    private List<RecommendHotelGO> hotels;

    private int type;

    public List<RecommendHotelGO> getHotels() {
        return hotels;
    }

    public void setHotels(List<RecommendHotelGO> hotels) {
        this.hotels = hotels;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
