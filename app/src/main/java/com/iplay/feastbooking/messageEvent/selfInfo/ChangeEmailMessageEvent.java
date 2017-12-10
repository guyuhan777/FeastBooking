package com.iplay.feastbooking.messageEvent.selfInfo;

import com.iplay.feastbooking.messageEvent.common.CommonMessageEvent;

/**
 * Created by gu_y-pc on 2017/12/10.
 */

public class ChangeEmailMessageEvent {

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public enum TYPE{SUCCESS, FAILURE}

    private TYPE type;

    private String failureReason;



}
