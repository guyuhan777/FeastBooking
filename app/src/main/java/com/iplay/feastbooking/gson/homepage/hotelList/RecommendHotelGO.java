package com.iplay.feastbooking.gson.homepage.hotelList;

import java.util.List;

/**
 * Created by admin on 2017/10/6.
 */

public class RecommendHotelGO {

    public int id;

    public String name;

    public int numOfComment;

    public String pictureUrl;

    public List<Double> priceRange;

    public List<Integer> tableRange;

    public String getNumOfComment(){
        if(numOfComment == 0){
            return "暫無評論";
        }
        return  numOfComment + "條評論";
    }

    public String getPriceRange(){
        return priceRange.get(0) + "-" + priceRange.get(0);
    }

    public String getTableRange(){
        return tableRange.get(0) + "-" + tableRange.get(1) + "桌";
    }


    @Override
    public String toString() {
        return "RecommendHotelGO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numOfComment=" + numOfComment +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", priceRange=" + priceRange +
                ", tableRange=" + tableRange +
                '}';
    }
}
