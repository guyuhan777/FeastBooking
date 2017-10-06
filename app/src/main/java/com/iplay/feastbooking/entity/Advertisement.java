package com.iplay.feastbooking.entity;

import com.iplay.feastbooking.dao.AdvertisementDao;
import com.iplay.feastbooking.gson.homepage.advertisement.AdvertisementGO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/1.
 */

public class Advertisement {

    private String url;

    public static Advertisement transFromDO(AdvertisementDao dao, String prefixPath, String seperator){
        Advertisement advertisement = new Advertisement();
        advertisement.setUrl(prefixPath + seperator + dao.getFileName());
        return advertisement;
    }

    public static Advertisement transFromGO(AdvertisementGO go){
        Advertisement advertisement = new Advertisement();
        advertisement.setUrl(go.pictureUrl);
        return advertisement;
    }

    public static List<Advertisement> transFromDOs(List<AdvertisementDao> daos, String prefixPath, String seperator){
        List<Advertisement> advertisements = new ArrayList<>();
        for (int i = 0; i<daos.size(); i++){
            advertisements.add(transFromDO(daos.get(i), prefixPath, seperator));
        }
        return advertisements;
    }

    public static List<Advertisement> transFromGOs(List<AdvertisementGO> gos){
        List<Advertisement> advertisements = new ArrayList<>();
        for (int i = 0; i<gos.size(); i++){
            advertisements.add(transFromGO(gos.get(i)));
        }
        return advertisements;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "Advertisement{" +
                "url='" + url + '\'' +
                '}';
    }
}
