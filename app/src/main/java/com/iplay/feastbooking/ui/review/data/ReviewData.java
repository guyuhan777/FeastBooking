package com.iplay.feastbooking.ui.review.data;

import com.iplay.feastbooking.ui.order.data.basic.BasicData;

import java.io.Serializable;

/**
 * Created by gu_y-pc on 2017/11/28.
 */

public class ReviewData extends BasicData implements Serializable{

    private final String author;

    private final int authorId;

    private final String banquetHall;

    private final int hotelId;

    private final int id;

    private final double rating;

    private final String review;

    private final String reviewTime;


    public ReviewData(String author, int authorId, String banquetHall, int hotelId, int id, double rating, String review, String reviewTime) {
        this.author = author;
        this.authorId = authorId;
        this.banquetHall = banquetHall;
        this.hotelId = hotelId;
        this.id = id;
        this.rating = rating;
        this.review = review;
        this.reviewTime = reviewTime;
    }

    public String getAuthor() {
        return author;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getBanquetHall() {
        return banquetHall;
    }

    public int getHotelId() {
        return hotelId;
    }

    public int getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    @Override
    public String toString() {
        return "ReviewData{" +
                "author='" + author + '\'' +
                ", authorId=" + authorId +
                ", banquetHall='" + banquetHall + '\'' +
                ", hotelId=" + hotelId +
                ", id=" + id +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                ", reviewTime='" + reviewTime + '\'' +
                '}';
    }
}
