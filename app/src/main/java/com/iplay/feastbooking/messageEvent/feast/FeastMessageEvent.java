package com.iplay.feastbooking.messageEvent.feast;

import com.iplay.feastbooking.gson.feast.FeastDetail;

/**
 * Created by admin on 2017/10/14.
 */

public class FeastMessageEvent {

    private FeastDetail feastDetail;

    public FeastDetail getFeastDetail() {
        return feastDetail;
    }

    public void setFeastDetail(FeastDetail feastDetail) {
        this.feastDetail = feastDetail;
    }
}
