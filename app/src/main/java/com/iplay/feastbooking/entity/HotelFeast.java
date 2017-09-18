package com.iplay.feastbooking.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/9/12.
 */

public class HotelFeast implements Serializable {

    public String name;

    public double pricePerTable;

    public List<String> meals;
}
