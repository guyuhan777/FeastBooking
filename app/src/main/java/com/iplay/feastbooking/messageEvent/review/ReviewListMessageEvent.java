package com.iplay.feastbooking.messageEvent.review;

import com.iplay.feastbooking.ui.review.data.ReviewData;

import java.util.List;

/**
 * Created by gu_y-pc on 2017/11/28.
 */

public class ReviewListMessageEvent {

    private boolean isInit = false;

    private int type;

    private String failureReason;

    public static final int TYPE_SUCCESS = 1;

    public static final int TYPE_FAILURE = 2;

    private List<ReviewData> reviews;

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public List<ReviewData> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewData> reviews) {
        this.reviews = reviews;
    }
}
