package com.iplay.feastbooking.gson.hotelDetail;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by admin on 2017/10/13.
 */

public class BanquetHall implements Serializable {

    public int area;

    public int id;

    public double minimumPrice;

    public String name;

    public String pictureUrl;

    public int[] tableRange;

    @Override
    public String toString() {
        return "BanquetHall{" +
                "area=" + area +
                ", id=" + id +
                ", minimumPrice=" + minimumPrice +
                ", name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", tableRange=" + Arrays.toString(tableRange) +
                '}';
    }
}
