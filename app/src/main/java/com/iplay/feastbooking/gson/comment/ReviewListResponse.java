package com.iplay.feastbooking.gson.comment;

import android.content.Context;

import java.util.List;

/**
 * Created by gu_y-pc on 2017/11/28.
 */

public class ReviewListResponse {

    public List<Content> content;

    public boolean first;

    public boolean last;

    public int number;

    public int numberOfElements;

    public int size;

    public int totalElements;

    public int totalPages;

    public static class Content{
        public String author;

        public int authorId;

        public String banquetHall;

        public int hotelId;

        public int id;

        public double rating;

        public String review;

        public String reviewTime;
    }

}
