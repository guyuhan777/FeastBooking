package com.iplay.feastbooking.gson.homepage.hotelList;

/**
 * Created by gu_y-pc on 2017/12/25.
 */

public class HotelListRequireConfig {

    private double minRating;

    private double minPrice;

    private double maxPrice;

    private String hotelNameKeywords;

    public double getMinRating() {
        return minRating;
    }

    public void setMinRating(double minRating) {
        this.minRating = minRating;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getHotelNameKeywords() {
        return hotelNameKeywords;
    }

    public void setHotelNameKeywords(String hotelNameKeywords) {
        this.hotelNameKeywords = hotelNameKeywords;
    }
}
