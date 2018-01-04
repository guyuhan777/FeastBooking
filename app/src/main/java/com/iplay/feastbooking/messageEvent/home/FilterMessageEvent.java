package com.iplay.feastbooking.messageEvent.home;

/**
 * Created by gu_y-pc on 2018/1/4.
 */

public class FilterMessageEvent {
    private Double minPrice;

    private Double maxPrice;

    private Double minRate;

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinRate() {
        return minRate;
    }

    public void setMinRate(Double minRate) {
        this.minRate = minRate;
    }
}
