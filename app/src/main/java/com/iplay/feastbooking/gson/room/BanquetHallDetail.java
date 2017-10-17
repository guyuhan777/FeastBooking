package com.iplay.feastbooking.gson.room;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/10/14.
 */

public class BanquetHallDetail implements Serializable {

    public int id;

    public String name;

    public int area;

    public int[] tableRange;

    public double minimumPrice;

    public double height;

    public List<String> pictureUrls;

    public String columns;

    public String shape;

    public String actualArea;

    public String colorOfTablecloth;

    public List<String> extraInfos;

    @Override
    public String toString() {
        return "BanquetHallDetail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", area=" + area +
                ", tableRange=" + Arrays.toString(tableRange) +
                ", minimumPrice=" + minimumPrice +
                ", height=" + height +
                ", pictureUrls=" + pictureUrls +
                ", columns='" + columns + '\'' +
                ", shape='" + shape + '\'' +
                ", actualArea='" + actualArea + '\'' +
                ", colorOfTableCloth='" + colorOfTablecloth + '\'' +
                ", extraInfos=" + extraInfos +
                '}';
    }
}
