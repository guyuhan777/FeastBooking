package com.iplay.feastbooking.ui.recommendedHotel.data;

import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.ui.recommendedHotel.data.basic.BasicHomeData;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class HotelHomeData extends BasicHomeData {

    private RecommendHotelGO hotel;

    public RecommendHotelGO getHotel() {
        return hotel;
    }

    public void setHotel(RecommendHotelGO hotel) {
        this.hotel = hotel;
    }
}
