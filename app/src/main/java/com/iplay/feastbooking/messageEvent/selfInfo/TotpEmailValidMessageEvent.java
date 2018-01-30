package com.iplay.feastbooking.messageEvent.selfInfo;

/**
 * Created by koishi on 18-1-28.
 */

public class TotpEmailValidMessageEvent {

    private boolean resultSuccess;

    private String failureReason;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isResultSuccess() {
        return resultSuccess;
    }

    public void setResultSuccess(boolean resultSuccess) {
        this.resultSuccess = resultSuccess;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
