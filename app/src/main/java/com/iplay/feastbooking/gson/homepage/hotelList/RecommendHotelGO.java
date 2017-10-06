package com.iplay.feastbooking.gson.homepage.hotelList;

import java.util.Arrays;

/**
 * Created by admin on 2017/10/6.
 */

public class RecommendHotelGO {

    public int id;

    public String name;

    public int numOfComment;

    public String pictureUrl;

    public int[] priceRange;

    public int[] tableRange;

    public String getNumOfComment(){
        if(numOfComment == 0){
            return "暫無評論";
        }
        return  numOfComment + "條評論";
    }

    public String getPriceRange(){
        return priceRange[0] + "-" + priceRange[1];
    }

    public String getTableRange(){
        return tableRange[0] + "-" + tableRange[1] + "桌";
    }

    @Override
    public String toString() {
        return "RecommendHotelGO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numOfComment=" + numOfComment +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", priceRange=" + Arrays.toString(priceRange) +
                ", tableRange=" + Arrays.toString(tableRange) +
                '}';
    }
}
