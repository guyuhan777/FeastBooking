package com.iplay.feastbooking.ui.recommendedHotel.data;

import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.ui.recommendedHotel.data.basic.BasicHomeData;

import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class AdvertisementHomeData extends BasicHomeData {

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    private List<Advertisement> advertisements;



}
