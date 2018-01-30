package com.iplay.feastbooking.gson.login;

/**
 * Created by koishi on 18-1-30.
 */

public class EmailTotpResponse {
    private String token;

    private boolean totpValid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isTotpValid() {
        return totpValid;
    }

    public void setTotpValid(boolean totpValid) {
        this.totpValid = totpValid;
    }
}
