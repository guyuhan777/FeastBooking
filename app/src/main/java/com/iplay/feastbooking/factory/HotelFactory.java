package com.iplay.feastbooking.factory;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.entity.Hotel;
import com.iplay.feastbooking.entity.HotelFeast;
import com.iplay.feastbooking.entity.HotelRecentDiscount;
import com.iplay.feastbooking.entity.HotelRoom;

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

    public static Hotel getSampleHotel(){
        Hotel hotel = new Hotel();
        hotel.icon_id = R.drawable.hotel5;
        hotel.hotel_name = "白雲酒店";
        hotel.rate = 3.3;
        List<HotelRecentDiscount> recentDiscounts = new ArrayList<>();
        for(int i=0;i<3;i++){
            HotelRecentDiscount recentDiscount = new HotelRecentDiscount();
            recentDiscount.date = "2017-09-03";
            recentDiscount.discount = 0.5;
            recentDiscounts.add(recentDiscount);
        }
        hotel.recentDiscounts = recentDiscounts;

        List<HotelRoom> hotelRooms = new ArrayList<>();
        for(int i=0;i<3;i++){
            HotelRoom room = new HotelRoom();
            room.area = 500;
            room.minTableCount = 10;
            room.maxTableCount = 18;
            room.name = "雨果厅 3F";
            room.minExpense = 5000;
            hotelRooms.add(room);
        }
        hotelRooms.get(0).icon_id = R.drawable.bh1;
        hotelRooms.get(1).icon_id = R.drawable.bh2;
        hotelRooms.get(2).icon_id = R.drawable.bh3;

        hotel.rooms = hotelRooms;
        List<HotelFeast> hotelFeasts = new ArrayList<>();
        for(int i=0;i<5;i++){
            HotelFeast feast = new HotelFeast();
            feast.name = "永结同心宴";
            feast.pricePerTable = 5000;
            List<String> meals = new ArrayList<>();
            for(int j=0;j<13;j++){
                meals.add("鸿运乳猪拼盘");
            }
            feast.meals = meals;
            hotelFeasts.add(feast);
        }
        hotel.feasts = hotelFeasts;
        return hotel;
    }
}
