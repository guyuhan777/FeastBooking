package com.iplay.feastbooking.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class Hotel implements Serializable{

    public String hotel_name;

    public String location;

    public int icon_id;

    public double startPricePerTable;

    public double endPricePerTable;

    public int startTableAmount;

    public int endTableAmount;

    public int numOfComment;

    public double rate;

    public List<HotelRecentDiscount> recentDiscounts;

    public String description;

    public List<HotelFeast> feasts;

    public List<HotelRoom> rooms;

}
