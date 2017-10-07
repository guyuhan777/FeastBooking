package com.iplay.feastbooking.gson.login;

/**
 * Created by admin on 2017/10/7.
 */

public class LoginResponse {

    public String token;

    public User user;

    public static class User{
        public int id;

        public String role;
    }

}