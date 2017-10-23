package com.iplay.feastbooking.gson.consult;

import java.io.Serializable;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class ConsultVO implements Serializable {

    public int banquetHallId;

    public String candidateDates;

    public String contact;

    public String phone;

    public String recommender;

    public int tables;

    @Override
    public String toString() {
        return "ConsultVO{" +
                "banquetHallId=" + banquetHallId +
                ", candidateDates='" + candidateDates + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", recommender='" + recommender + '\'' +
                ", tables=" + tables +
                '}';
    }
}
