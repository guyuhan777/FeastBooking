package com.iplay.feastbooking.dao;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.dto.UserDto;
import com.iplay.feastbooking.ui.home.HomeActivity;
import com.iplay.feastbooking.ui.login.WelcomeActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

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

    public UserDto getLoginUser(){
        List<UserDto> userDtos = DataSupport.where("isLogin = ?","" + 1).find(UserDto.class);
        if(userDtos.size() == 1){
            return userDtos.get(0);
        }else {
            return null;
        }
    }
}
