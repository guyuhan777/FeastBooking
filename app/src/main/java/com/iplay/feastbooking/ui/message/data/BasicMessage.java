package com.iplay.feastbooking.ui.message.data;

import com.iplay.feastbooking.ui.order.data.basic.BasicData;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by gu_y-pc on 2018/1/13.
 */

public class BasicMessage extends BasicData {

    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
