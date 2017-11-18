package com.iplay.feastbooking.entity;

import java.io.Serializable;

/**
 * Created by Guyuhan on 2017/11/7.
 */

public class IdentityMatrix implements Serializable{

    private boolean isCustomer;

    private boolean isManager;

    private boolean isRecommender;

    public IdentityMatrix(boolean isCustomer, boolean isManager, boolean isRecommender){
        this.isCustomer = isCustomer;
        this.isManager = isManager;
        this.isRecommender = isRecommender;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public boolean isManager() {
        return isManager;
    }

    public boolean isRecommender() {
        return isRecommender;
    }
}
