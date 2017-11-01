package com.iplay.feastbooking.ui.timeline.data;

/**
 * Created by Guyuhan on 2017/10/29.
 */

public class TimeLineNode {

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private boolean isHead;

    private String describe;

    private boolean isActive;

    @Override
    public String toString() {
        return "TimeLineNode{" +
                "isHead=" + isHead +
                ", describe='" + describe + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
