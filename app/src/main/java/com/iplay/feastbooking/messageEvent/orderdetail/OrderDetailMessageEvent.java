package com.iplay.feastbooking.messageEvent.orderdetail;

import com.iplay.feastbooking.gson.orderDetail.OrderDetail;

/**
 * Created by Guyuhan on 2017/11/5.
 */

public class OrderDetailMessageEvent {

    public static final int TYPE_FAILURE = 1;

    public static final int TYPE_SUCCESS = 2;

    private int type;

    private String failureResult;

    private OrderDetail orderDetail;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFailureResult() {
        return failureResult;
    }

    public void setFailureResult(String failureResult) {
        this.failureResult = failureResult;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }
}
