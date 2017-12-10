package com.iplay.feastbooking.messageEvent.selfInfo;

import com.iplay.feastbooking.messageEvent.common.CommonMessageEvent;

/**
 * Created by gu_y-pc on 2017/12/10.
 */

public class ChangeEmailConfirmMessageEvent {

    public enum TYPE{SUCCESS, FAILURE};

    private TYPE type;

    private String failureResult;

    private String token;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
