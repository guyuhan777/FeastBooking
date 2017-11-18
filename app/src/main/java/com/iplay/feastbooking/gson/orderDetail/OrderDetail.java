package com.iplay.feastbooking.gson.orderDetail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Guyuhan on 2017/11/5.
 */

public class OrderDetail implements Serializable {

    public String banquetHall;

    public List<String> candidateDates;

    public String contact;

    public String customer;

    public String feastingDate;

    public String hotel;

    public int id;

    public String lastUpdated;

    public String manager;

    @SerializedName("orderContractDTO")
    public OrderContract orderContract;

    public long orderNumber;

    @SerializedName("orderPaymentDTO")
    public OrderPayment orderPayment;

    public String orderStatus;

    public String orderTime;

    public String phone;

    public String recommender;

    public boolean reviewed;

    public int tables;

    @Override
    public String toString() {
        return "OrderDetail{" +
                "banquetHall='" + banquetHall + '\'' +
                ", candidateDates=" + candidateDates +
                ", contact='" + contact + '\'' +
                ", contact='" + customer + '\'' +
                ", feastingDate='" + feastingDate + '\'' +
                ", hotel='" + hotel + '\'' +
                ", id=" + id +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", manager='" + manager + '\'' +
                ", orderContract=" + orderContract +
                ", orderNumber=" + orderNumber +
                ", orderPayment=" + orderPayment +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", phone='" + phone + '\'' +
                ", recommender='" + recommender + '\'' +
                ", reviewed='" + reviewed + '\'' +
                ", tables='" + tables + '\'' +
                '}';
    }

    public static class OrderContract implements Serializable{
        public String approvalStatus;

        public List<String> files;

        public String lastUpdated;

        @Override
        public String toString() {
            return "OrderContract{" +
                    "approvalStatus='" + approvalStatus + '\'' +
                    ", files=" + files +
                    ", lastUpdated='" + lastUpdated + '\'' +
                    '}';
        }
    }

    public static class OrderPayment implements Serializable{
        public int amountPaid;

        public String approvalStatus;

        public List<String> files;

        public String lastUpdated;

        @Override
        public String toString() {
            return "OrderPayment{" +
                    "amountPaid=" + amountPaid +
                    ", approvalStatus='" + approvalStatus + '\'' +
                    ", files=" + files +
                    ", lastUpdated='" + lastUpdated + '\'' +
                    '}';
        }
    }
}
