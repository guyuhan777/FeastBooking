package com.iplay.feastbooking.ui.message.data;

import com.iplay.feastbooking.ui.order.data.basic.BasicData;

import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
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

    private static String ROLE_KEY = "role", ORDER_ID_KEY = "orderId";

    public static boolean isMessageValid(Message message) {
        if (message == null || message.getContentType() != ContentType.text) {
            return false;
        } else {
            TextContent content = (TextContent) message.getContent();
            if (content == null ||
                    (!content.getStringExtras().containsKey(ROLE_KEY) &&
                            !content.getStringExtras().containsKey(ORDER_ID_KEY))) {
                return false;
            }
        }
        return true;
    }
}