package com.iplay.feastbooking.dao;

import com.iplay.feastbooking.gson.RecommendGridGO;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/10/2.
 */

public class RecommendGridDao extends DataSupport {

    private int id;

    @Column(unique = true)
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

    public static RecommendGridDao transFromGO(RecommendGridGO go, String basicPath){
        RecommendGridDao dao = new RecommendGridDao();
        dao.setFilename(go.pictureUrl.replace(basicPath,""));
        dao.setHotelId(go.hotelId);
        return dao;
    }
}
