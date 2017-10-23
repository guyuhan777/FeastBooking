package com.iplay.feastbooking.ui.recommendedHotel.data;

import com.iplay.feastbooking.entity.RecommendGrid;
import com.iplay.feastbooking.ui.recommendedHotel.data.basic.BasicHomeData;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class RecommendHotelHomeData extends BasicHomeData {

    private RecommendGrid recommendGrid;

    public RecommendGrid getRecommendGrid() {
        return recommendGrid;
    }

    public void setRecommendGrid(RecommendGrid recommendGrid) {
        this.recommendGrid = recommendGrid;
    }
}
