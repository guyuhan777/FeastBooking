package com.iplay.feastbooking.gson.selfInfo;

/**
 * Created by gu_y-pc on 2017/12/16.
 */

public class ChangePasswordRequest {

    private final String password;

    private final String newPassword;

    public ChangePasswordRequest(String password, String newPassword){
        this.password = password;
        this.newPassword = newPassword;
    }
}
