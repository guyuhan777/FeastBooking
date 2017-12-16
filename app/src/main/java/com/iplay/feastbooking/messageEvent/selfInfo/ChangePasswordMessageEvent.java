package com.iplay.feastbooking.messageEvent.selfInfo;

/**
 * Created by gu_y-pc on 2017/12/16.
 */

public class ChangePasswordMessageEvent {

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getFailureResult() {
        return failureResult;
    }

    public void setFailureResult(String failureResult) {
        this.failureResult = failureResult;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public enum TYPE{SUCCESS, FAILURE};

    private TYPE type;

    private String failureResult;

    private String password;

}
