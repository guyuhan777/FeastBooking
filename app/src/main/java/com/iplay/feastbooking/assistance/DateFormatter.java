package com.iplay.feastbooking.assistance;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/10/22.
 */

public class DateFormatter {

    public static  String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(date);
    }

}
