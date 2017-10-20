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

    private LoginUserHolder(){

    }

    public synchronized UserDao getCurrentUser(){
        if(currentUser == null){
            List<UserDao> userDaos = DataSupport.where("isLogin = ?","" + 1).find(UserDao.class);
            if(userDaos.size() == 1){
                currentUser = userDaos.get(0);
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
