package com.iplay.feastbooking.gson.order;

import java.util.List;

/**
 * Created by Guyuhan on 2017/10/28.
 */

public class OrderListItem {

    public List<Content> content;

    public static class Content{

        public String banquetHall;

        public String customer;

        public String date;

        public String hotel;

        public int id;

        public String orderStatus;

        public List<String> roleInOrders;

        public int tables;

        @Override
        public String toString() {
            return "Content{" +
                    "banquetHall='" + banquetHall + '\'' +
                    ", customer='" + customer + '\'' +
                    ", date='" + date + '\'' +
                    ", hotel='" + hotel + '\'' +
                    ", id=" + id +
                    ", orderStatus='" + orderStatus + '\'' +
                    ", roleInOrders=" + roleInOrders +
                    ", tables=" + tables +
                    '}';
        }
    }

}
