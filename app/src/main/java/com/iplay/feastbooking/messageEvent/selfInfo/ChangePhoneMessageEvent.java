package com.iplay.feastbooking.messageEvent.selfInfo;

/**
 * Created by gu_y-pc on 2017/12/16.
 */

public class ChangePhoneMessageEvent {

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public enum TYPE{SUCCESS, FAILURE};

    private TYPE type;

    private String failureResult;

    private String phone;

}
