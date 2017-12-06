package com.iplay.feastbooking.assistance;

import com.iplay.feastbooking.dto.UserDto;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by admin on 2017/10/17.
 */

public class LoginUserHolder {

    private static volatile LoginUserHolder instance;

    private volatile UserDto currentUser;

    private LoginUserHolder(){

    }

    public synchronized UserDto getCurrentUser(){
        if(currentUser == null){
            List<UserDto> userDtos = DataSupport.where("isLogin = ?","" + 1).find(UserDto.class);
            if(userDtos.size() == 1){
                currentUser = userDtos.get(0);
            }
        }
        return currentUser;
    };

    public synchronized void removeCurrentUser(){
        currentUser = null;
    }

    public static LoginUserHolder getInstance(){
        if(instance == null){
            synchronized (LoginUserHolder.class){
                instance = new LoginUserHolder();
            }
        }
        return instance;
    }


}
