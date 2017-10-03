package com.iplay.feastbooking.messageEvent;

import com.iplay.feastbooking.entity.RecommendGrid;

import java.util.List;

/**
 * Created by admin on 2017/10/3.
 */

public class RecommendGridMessageEvent {
    private List<RecommendGrid> recommendGrids;

    public List<RecommendGrid> getRecommendGrids() {
        return recommendGrids;
    }

    public void setRecommendGrids(List<RecommendGrid> recommendGrids) {
        this.recommendGrids = recommendGrids;
    }

}
