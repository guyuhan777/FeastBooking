package com.iplay.feastbooking.messageEvent.consult;

import com.iplay.feastbooking.gson.consult.ConsultResult;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class ConsultOrderMessageEvent {

    public static final int TYPE_SUCCESS = 1;

    public static final int TYPE_FAILURE = 2;

    private String failureReason;

    private int type;

    private ConsultResult result;

    public ConsultResult getResult() {
        return result;
    }

    public void setResult(ConsultResult result) {
        this.result = result;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
