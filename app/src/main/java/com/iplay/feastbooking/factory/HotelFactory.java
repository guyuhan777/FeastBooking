package com.iplay.feastbooking.factory;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.entity.Hotel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class HotelFactory {

    public static List<Hotel> getMoreHotel(){
        List<Hotel> hotels = new ArrayList<Hotel>();

        for(int i =0;i<3;i++){
            Hotel hotel = new Hotel();
            hotel.hotel_name = "金碧辉煌酒店";
            hotel.icon_id = R.drawable.hotel2;
            hotel.location = "盛泽";
            hotel.startPricePerTable = 2888;
            hotel.endPricePerTable = 4888;
            hotel.startTableAmount = 1;
            hotel.endTableAmount = 99;
            hotel.numOfComment = 13;
            hotels.add(hotel);
        }

        return hotels;
    }

    public static List<Hotel> generateHotel(){
        List<Hotel> hotels = new ArrayList<Hotel>();

        for(int i =0;i<5;i++){
            Hotel hotel = new Hotel();
            hotel.hotel_name = "四季酒店";
            hotel.icon_id = R.drawable.hotel1;
            hotel.location = "吴江";
            hotel.startPricePerTable = 2888;
            hotel.endPricePerTable = 4888;
            hotel.startTableAmount = 1;
            hotel.endTableAmount = 99;
            hotel.numOfComment = 13;
            hotels.add(hotel);
        }

        return hotels;
    }

}
