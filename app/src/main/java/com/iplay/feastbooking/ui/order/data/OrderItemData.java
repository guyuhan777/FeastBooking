package com.iplay.feastbooking.ui.order.data;

import com.iplay.feastbooking.gson.order.OrderListItem;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;

/**
 * Created by Guyuhan on 2017/10/29.
 */

public class OrderItemData extends BasicData {

    private OrderListItem.Content content;

    public OrderListItem.Content getContent() {
        return content;
    }

    public void setContent(OrderListItem.Content content) {
        this.content = content;
    }
}
