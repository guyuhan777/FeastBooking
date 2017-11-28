package com.iplay.feastbooking.ui.order.data;

import com.iplay.feastbooking.ui.order.data.basic.BasicData;

/**
 * Created by Guyuhan on 2017/10/29.
 */

public class FootStateData extends BasicData {



    public static final int TYPE_ALL_LOADED = 1;

    public static final int TYPE_CLICK_TO_LOAD_MORE = 2;

    public static final int TYPE_LOADING = 2;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
