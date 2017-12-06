package com.iplay.feastbooking.dto;

import com.iplay.feastbooking.gson.homepage.advertisement.AdvertisementGO;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/10/1.
 */

public class AdvertisementDto extends DataSupport{

    private int id;

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public static AdvertisementDto transFromGO(AdvertisementGO go, String basicPath){
        AdvertisementDto dao = new AdvertisementDto();
        dao.setFileName(go.pictureUrl.replace(basicPath,""));
        return dao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AdvertisementDto{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
