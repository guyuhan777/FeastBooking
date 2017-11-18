package com.iplay.feastbooking.messageEvent.orderdetail;

/**
 * Created by Guyuhan on 2017/11/18.
 */

public class OrderDetailChangeMessageEvent {
    public enum TYPE {TYPE_FAILURE, TYPE_SUCCESS}

    public String getFailureResult() {
        return failureResult;
    }

    public TYPE getType() {
        return type;
    }

    private TYPE type;

    public void setType(TYPE type) {
        this.type = type;
    }

    public void setFailureResult(String failureResult) {
        this.failureResult = failureResult;
    }

    private String failureResult;

    public OrderDetailChangeMessageEvent(){

    }

    public OrderDetailChangeMessageEvent(TYPE type, String... failureResult){
        this.type = type;
        if(this.type == TYPE.TYPE_FAILURE){
            this.failureResult = failureResult[0];
        }
    }

}
