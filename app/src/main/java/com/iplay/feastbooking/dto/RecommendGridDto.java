package com.iplay.feastbooking.dto;

import com.iplay.feastbooking.gson.homepage.RecommendGridGO;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/10/2.
 */

public class RecommendGridDto extends DataSupport {

    private int id;

    private int hotelId;

    private String filename;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public static RecommendGridDto transFromGO(RecommendGridGO go, String basicPath){
        RecommendGridDto dao = new RecommendGridDto();
        dao.setFilename(go.pictureUrl.replace(basicPath,""));
        dao.setHotelId(go.hotelId);
        return dao;
    }
}
