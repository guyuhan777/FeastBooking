package com.iplay.feastbooking.dao;

import com.iplay.feastbooking.dto.UserDto;

/**
 * Created by gu_y-pc on 2017/12/6.
 */

public class UserDao {

    private static volatile UserDao instance;

    public static UserDao getInstance(){
        if(instance == null){
            synchronized (UserDao.class){
                instance = new UserDao();
            }
        }
        return instance;
    }
}
