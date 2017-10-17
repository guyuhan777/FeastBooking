package com.iplay.feastbooking.messageEvent.banquetHall;

import com.iplay.feastbooking.gson.room.BanquetHallDetail;

/**
 * Created by admin on 2017/10/14.
 */

public class BanquetHallMessageEvent {

    private BanquetHallDetail banquetHallDetail;

    public BanquetHallDetail getBanquetHallDetail() {
        return banquetHallDetail;
    }

    public void setBanquetHallDetail(BanquetHallDetail banquetHallDetail) {
        this.banquetHallDetail = banquetHallDetail;
    }
}
