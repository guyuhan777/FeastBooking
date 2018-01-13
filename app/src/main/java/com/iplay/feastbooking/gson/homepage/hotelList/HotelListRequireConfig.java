package com.iplay.feastbooking.gson.homepage.hotelList;

/**
 * Created by gu_y-pc on 2017/12/26.
 */

public class HotelListRequireConfig {

    public enum SortType{COMPLEX, ORDER, PRICE};

    private HotelListFilterRequireConfig filterRequireConfig;

    private SortType sortType = SortType.COMPLEX;

    private int page;

    private void resetPage(){
        page = 0;
    }

    private boolean asc = false;

    public HotelListFilterRequireConfig getFilterRequireConfig() {
        return filterRequireConfig;
    }

    private HotelListRequireConfig(){

    }

    public void increasePage(){
        page++;
    }

    public void resetFilter(){
        resetPage();
        if(filterRequireConfig != null){
            filterRequireConfig.reset();
        }
    }

    public static HotelListRequireConfig getDefaultRequireConfig(){
        HotelListRequireConfig config = new HotelListRequireConfig();
        config.setFilterRequireConfig(new HotelListFilterRequireConfig());
        return config;
    }

    public String getQueryString() {
        String query = "?page=" + page + "&sort=";
        if (sortType == SortType.PRICE) {
            query += "minimumPrice";
        } else if (sortType == SortType.ORDER) {
            query += "numOfOrders";
        } else if (sortType == SortType.COMPLEX) {
            query += "rating";
        }
        if (asc) {
            query += ",asc";
        } else {
            query += ",desc";
        }
        if(filterRequireConfig != null && !filterRequireConfig.getQueryString().equals("")){
            query += "&" + filterRequireConfig.getQueryString();
        }
        return query;
    }

    public void setFilterRequireConfig(HotelListFilterRequireConfig filterRequireConfig) {
        this.filterRequireConfig = filterRequireConfig;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        resetPage();
        this.sortType = sortType;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
