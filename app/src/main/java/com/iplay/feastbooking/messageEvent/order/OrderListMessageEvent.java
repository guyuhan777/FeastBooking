package com.iplay.feastbooking.messageEvent.order;

import com.iplay.feastbooking.gson.order.OrderListItem;

/**
 * Created by Guyuhan on 2017/10/28.
 */

public class OrderListMessageEvent {

    private boolean isInit = false;

    private int type;

    private OrderListItem orderListItemList;

    public static final int TYPE_SUCCESS = 1;

    public static final int TYPE_NO_INTERNET = 2;

    public static final int TYPE_FAILURE = 3;

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    private String failureReason;

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public OrderListItem getOrderListItemList() {
        return orderListItemList;
    }

    public void setOrderListItemList(OrderListItem orderListItemList) {
        this.orderListItemList = orderListItemList;
    }
}
