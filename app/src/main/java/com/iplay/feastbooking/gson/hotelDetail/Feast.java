package com.iplay.feastbooking.gson.hotelDetail;

import java.io.Serializable;

/**
 * Created by admin on 2017/10/13.
 */

public class Feast implements Serializable {

    public int id;

    public String name;

    public double price;

    @Override
    public String toString() {
        return "Feast{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
