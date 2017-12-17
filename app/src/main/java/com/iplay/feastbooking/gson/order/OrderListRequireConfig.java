package com.iplay.feastbooking.gson.order;

/**
 * Created by gu_y-pc on 2017/12/16.
 */

public class OrderListRequireConfig {

    private static int defaultSize = 20;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public enum STATUS{UNFINISHED, FINISHED};

    private STATUS status;

    private int page;

    private int size = defaultSize;

    private boolean isAsc;

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    private boolean isInit;

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }
}
