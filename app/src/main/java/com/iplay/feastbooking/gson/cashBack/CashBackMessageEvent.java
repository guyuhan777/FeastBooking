package com.iplay.feastbooking.gson.cashBack;

/**
 * Created by gu_y-pc on 2017/12/16.
 */

public class CashBackMessageEvent {

    private final double completedCashback;

    private final double pendingCashback;

    private final double totalCashback;

    private final int userId;

    public CashBackMessageEvent(double completedCashback, double pendingCashback, double totalCashback, int userId) {
        this.completedCashback = completedCashback;
        this.pendingCashback = pendingCashback;
        this.totalCashback = totalCashback;
        this.userId = userId;
    }

    public double getCompletedCashback() {
        return completedCashback;
    }

    public double getPendingCashback() {
        return pendingCashback;
    }

    public double getTotalCashback() {
        return totalCashback;
    }

    public int getUserId() {
        return userId;
    }
}
