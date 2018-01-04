package com.iplay.feastbooking.dto;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by gu_y-pc on 2018/1/4.
 */

public class HistorySearchItemDto extends DataSupport {
    @Column(unique = true)
    private int id;

    @Column(unique = true)
    private String keyword;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
