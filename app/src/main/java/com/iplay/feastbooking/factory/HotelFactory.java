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

    private static String environments[] = new String[]{"環境面積: 900㎡","樓層高度: 11.0㎡","立柱情況: 無立柱","大廳形狀: 正方形"
                                                    ,"迎賓區域: 大","桌布顏色: 原諒色", "可定桌數: 20-42桌","最低消費: $18000.0"};

    private static String configs[] = new String[]{"有草坪","有舞台","有T台","投影频幕","有燈光音響"};

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
            hotel.icon_id = R.drawable.bh2;
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
        hotel.icon_id = R.drawable.bh2;
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
            List<String> configLists = new ArrayList<>();
            for(int j=0;j<configs.length;j++){
                configLists.add(configs[j]);
            }
            room.configs = configLists;
            List<String> environmentLists = new ArrayList<>();
            for(int j=0;j<environments.length;j++){
                environmentLists.add(environments[j]);
            }
            room.environments = environmentLists;
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
