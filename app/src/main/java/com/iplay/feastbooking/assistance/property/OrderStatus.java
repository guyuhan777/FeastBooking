package com.iplay.feastbooking.assistance.property;

import java.util.HashMap;

/**
 * Created by gu_y-pc on 2017/11/25.
 */

public class OrderStatus {

    private static HashMap<String, String> orderStatusEn2ChMap;

    public static final String
            STATUS_CONSULTING = "CONSULTING",
            STATUS_RESERVED = "RESERVED",
            STATUS_FEASTED = "FEASTED",
            STATUS_CASHBACK = "CASHBACK",
            STATUS_TO_BE_REVIEWD = "TO_BE_REVIEWD";

    static {
        orderStatusEn2ChMap = new HashMap<>();
        orderStatusEn2ChMap.put(STATUS_CONSULTING,"咨詢中");
        orderStatusEn2ChMap.put(STATUS_RESERVED,"已預訂");
        orderStatusEn2ChMap.put(STATUS_FEASTED, "已擺酒");
        orderStatusEn2ChMap.put(STATUS_CASHBACK, "等待返現");
        orderStatusEn2ChMap.put(STATUS_TO_BE_REVIEWD, "待評價");
    }

    public static String getOrderStatusCh(String enKey){
        return orderStatusEn2ChMap.get(enKey);
    }


}
