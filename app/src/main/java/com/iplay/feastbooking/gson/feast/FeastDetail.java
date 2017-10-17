package com.iplay.feastbooking.gson.feast;

import java.util.List;

/**
 * Created by admin on 2017/10/14.
 */

public class FeastDetail {

    public int id;

    public String name;

    public double price;

    public List<String> courses;

    public List<String> pictureUrls;

    @Override
    public String toString() {
        return "FeastDetail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", courses=" + courses +
                ", pictureUrls=" + pictureUrls +
                '}';
    }
}
