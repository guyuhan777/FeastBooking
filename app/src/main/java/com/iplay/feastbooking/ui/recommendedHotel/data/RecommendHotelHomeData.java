package com.iplay.feastbooking.ui.recommendedHotel.data;

import com.iplay.feastbooking.entity.RecommendGrid;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class RecommendHotelHomeData extends BasicData {

    private RecommendGrid recommendGrid;

    public RecommendGrid getRecommendGrid() {
        return recommendGrid;
    }

    public void setRecommendGrid(RecommendGrid recommendGrid) {
        this.recommendGrid = recommendGrid;
    }
}
