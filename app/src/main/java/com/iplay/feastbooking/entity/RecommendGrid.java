package com.iplay.feastbooking.entity;

import com.iplay.feastbooking.dto.RecommendGridDto;
import com.iplay.feastbooking.gson.homepage.RecommendGridGO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/2.
 */

public class RecommendGrid {

    private int hotelId;

    private String url;

    public static RecommendGrid transferFromGO(RecommendGridGO go){
        RecommendGrid recommendGrid = new RecommendGrid();
        recommendGrid.setHotelId(go.hotelId);
        recommendGrid.setUrl(go.pictureUrl);
        return recommendGrid;
    }

    public static RecommendGrid transFromDO(RecommendGridDto dao, String prefixPath, String seperator){
        RecommendGrid advertisement = new RecommendGrid();
        advertisement.setHotelId(dao.getHotelId());
        advertisement.setUrl(prefixPath + seperator + dao.getFilename());
        return advertisement;
    }

    public static List<RecommendGrid> transFromDOs(List<RecommendGridDto> daos, String prefixPath, String seperator){
        List<RecommendGrid> recommendGrids = new ArrayList<>();
        for (int i = 0; i<daos.size(); i++){
            recommendGrids.add(transFromDO(daos.get(i), prefixPath, seperator));
        }
        return recommendGrids;
    }

    public static List<RecommendGrid> transFromGOs(List<RecommendGridGO> gos){
        List<RecommendGrid> recommendGrids = new ArrayList<>();
        for (int i = 0; i<gos.size(); i++){
            recommendGrids.add(transferFromGO(gos.get(i)));
        }
        return recommendGrids;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RecommendGrid{" +
                "hotelId=" + hotelId +
                ", url='" + url + '\'' +
                '}';
    }
}
