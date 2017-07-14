package com.iplay.feastbooking.component.pull2Load;

/**
 * Created by admin on 2017/5/30.
 */

public interface LoadMoreDataListener {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_PROG = 1;

    public abstract void loadMoreData();
}
