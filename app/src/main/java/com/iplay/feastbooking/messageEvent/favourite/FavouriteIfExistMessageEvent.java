package com.iplay.feastbooking.messageEvent.favourite;

/**
 * Created by gu_y-pc on 2017/12/24.
 */

public class FavouriteIfExistMessageEvent {

    private boolean isExist;

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        this.isExist = exist;
    }
}
