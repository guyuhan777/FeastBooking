package com.iplay.feastbooking.gson.homepage.hotelList;

/**
 * Created by gu_y-pc on 2017/12/25.
 */

public class HotelListFilterRequireConfig {

    private Double minRating;

    private Double minPrice;

    private Double maxPrice;

    private String hotelNameKeywords;

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

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

    public String getHotelNameKeywords() {
        return hotelNameKeywords;
    }

    public void setHotelNameKeywords(String hotelNameKeywords) {
        this.hotelNameKeywords = hotelNameKeywords;
    }

    public String getQueryString(){
        String query = "";
        if(minRating != null){
            query += ("min_rating=" + minRating + "&");
        }
        if(minPrice != null){
            query += ("min_price=" + minPrice + "&");
        }
        if(maxPrice != null){
            query += ("max_price=" + maxPrice + "&");
        }
        if(hotelNameKeywords != null){
            query += ("name=" + hotelNameKeywords + "&");
        }
        return query;
    }
}
