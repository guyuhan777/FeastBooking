package com.iplay.feastbooking.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/9/12.
 */

public class HotelRoom implements Serializable{

    public int icon_id;

    public String name;

    public double area;

    public int minTableCount;

    public int maxTableCount;

    public double minExpense;

    public List<String> configs;

    public List<String> environments;
}
