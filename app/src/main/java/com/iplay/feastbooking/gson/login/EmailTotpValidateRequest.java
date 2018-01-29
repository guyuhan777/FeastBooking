package com.iplay.feastbooking.gson.login;

/**
 * Created by koishi on 18-1-29.
 */

public class EmailTotpValidateRequest {

    private String email;

    private String totp;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTotp() {
        return totp;
    }

    public void setTotp(String totp) {
        this.totp = totp;
    }
}
