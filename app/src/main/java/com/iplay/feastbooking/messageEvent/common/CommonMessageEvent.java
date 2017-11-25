package com.iplay.feastbooking.messageEvent.common;


/**
 * Created by gu_y-pc on 2017/11/25.
 */

public class CommonMessageEvent<T> {

    public T getSuccessResult() {
        return successResult;
    }

    public void setSuccessResult(T successResult) {
        this.successResult = successResult;
    }

    private T successResult;

    public enum TYPE {TYPE_FAILURE, TYPE_SUCCESS}

    public String getFailureResult() {
        return failureResult;
    }

    public CommonMessageEvent.TYPE getType() {
        return type;
    }

    private CommonMessageEvent.TYPE type;

    public void setType(CommonMessageEvent.TYPE type) {
        this.type = type;
    }

    public void setFailureResult(String failureResult) {
        this.failureResult = failureResult;
    }

    private String failureResult;



    public CommonMessageEvent(){

    }

    public CommonMessageEvent(CommonMessageEvent.TYPE type, String... failureResult){
        this.type = type;
        if(this.type == CommonMessageEvent.TYPE.TYPE_FAILURE){
            this.failureResult = failureResult[0];
        }
    }

}
