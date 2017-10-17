package com.iplay.feastbooking.assistance;

import com.iplay.feastbooking.dao.UserDao;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by admin on 2017/10/17.
 */

public class LoginUserHolder {

    private static volatile LoginUserHolder instance;

    private volatile UserDao currentUser;

    private volatile boolean isInit = false;

    private LoginUserHolder(){

    }

    public synchronized UserDao getCurrentUser(){
        if(!isInit && currentUser == null){
            List<UserDao> userDaos = DataSupport.where("isLogin = ?","" + 1).find(UserDao.class);
            if(userDaos.size() == 1){
                currentUser = userDaos.get(0);
            }
            isInit = true;
        }
        return currentUser;
    };

    public static LoginUserHolder getInstance(){
        if(instance == null){
            synchronized (LoginUserHolder.class){
                instance = new LoginUserHolder();
            }
        }
        return instance;
    }


}
