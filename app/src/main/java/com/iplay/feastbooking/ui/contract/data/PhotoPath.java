package com.iplay.feastbooking.ui.contract.data;

/**
 * Created by Guyuhan on 2017/11/11.
 */

public class PhotoPath {

    public static final int TYPE_FROM_DISK = 1;
    public static final int TYPE_FROM_INTERNET = 2;

    private int type;

    private String url;

    public PhotoPath(int type, String url){
        this.type = type;
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
