package com.iplay.feastbooking.gson.cashBack;

import android.support.annotation.NonNull;

import com.iplay.feastbooking.entity.IdentityMatrix;

/**
 * Created by gu_y-pc on 2017/12/23.
 */

public class OrderCashBackMessageEvent {

    private double amountPaid;

    private int orderId;

    private CashbackStrategy cashbackStrategy;

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public CashbackStrategy getCashbackStrategy() {
        return cashbackStrategy;
    }

    public void setCashbackStrategy(CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    public String getPercentageForCustomer(){
        return (cashbackStrategy == null ? 0 : cashbackStrategy.getPercentageForCustomer()) + "%";
    }

    public String getPercentageForRecommender(){
        return (cashbackStrategy == null ? 0 : cashbackStrategy.getPercentageForRecommender()) + "%";
    }

    public String getPercentageForManager(){
        return (cashbackStrategy == null ? 0 : cashbackStrategy.getPercentageForManager()) + "%";
    }

    public String getExpectedAmount(@NonNull IdentityMatrix matrix){
        double result = amountPaid;
        if(cashbackStrategy != null){
            int seed = 0;
            if(matrix.isCustomer()){
                seed += cashbackStrategy.getPercentageForCustomer();
            }
            if(matrix.isManager()){
                seed += cashbackStrategy.getPercentageForManager();
            }
            if(matrix.isRecommender()){
                seed += cashbackStrategy.getPercentageForManager();
            }
            result *= (double)seed / 100;
        }
        return "$" + result;
    }

    public static class CashbackStrategy{
        private double percentageForCustomer;

        private double percentageForManager;

        private double percentageForRecommender;

        public double getPercentageForCustomer() {
            return percentageForCustomer;
        }

        public void setPercentageForCustomer(double percentageForCustomer) {
            this.percentageForCustomer = percentageForCustomer;
        }

        public double getPercentageForManager() {
            return percentageForManager;
        }

        public void setPercentageForManager(double percentageForManager) {
            this.percentageForManager = percentageForManager;
        }

        public double getPercentageForRecommender() {
            return percentageForRecommender;
        }

        public void setPercentageForRecommender(double percentageForRecommender) {
            this.percentageForRecommender = percentageForRecommender;
        }
    }


}
