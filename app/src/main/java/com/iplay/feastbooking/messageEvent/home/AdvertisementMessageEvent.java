package com.iplay.feastbooking.messageEvent.home;

import com.iplay.feastbooking.entity.Advertisement;

import java.util.List;

/**
 * Created by admin on 2017/10/2.
 */

public class AdvertisementMessageEvent {

    private List<Advertisement> advertisements;

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }
}
