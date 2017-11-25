package com.iplay.feastbooking.gson.comment;

/**
 * Created by gu_y-pc on 2017/11/25.
 */

public class CommentVO {
    private final double rating;

    private final String review;

    public CommentVO(double rating, String review){
        this.rating = rating;
        this.review = review;
    }
}
