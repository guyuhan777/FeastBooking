package com.iplay.feastbooking.net.utilImpl.jMessage;

import android.content.Context;

import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.assistance.SecurityUtility;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by gu_y-pc on 2018/1/13.
 */

public class JMessageUtility {

    private static volatile JMessageUtility instance;

    private final Properties properties;

    private final String appKey;

    public JMessageUtility(Context context){
        properties = ProperTies.getProperties(context);
        appKey = properties.getProperty("appKey");
    }

    public static JMessageUtility getInstance(Context context){
        if(instance == null){
            synchronized (JMessageUtility.class){
                if(instance == null){
                    instance = new JMessageUtility(context);
                }
            }
        }
        return instance;
    }

    public Conversation getSingleConversation(){
        UserDto currentUser = UserDao.getInstance().getLoginUser();
        if(currentUser != null){
            return JMessageClient.getSingleConversation(currentUser.getUsername(), appKey);
        }
        return null;
    }

    public void login(){
        JMessageClient.logout();
        UserDto currentUser = UserDao.getInstance().getLoginUser();
        if(currentUser != null){
            String md5Password = SecurityUtility.md5(currentUser.getPassword());
            JMessageClient.login(currentUser.getUsername(), md5Password, null);
        }
    }

    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        List<Conversation> conversations = JMessageClient.getConversationList();
        for(Conversation conversation : conversations){
            if(conversation != null){
                messages.addAll(conversation.getAllMessage());
            }
        }
        return messages;
    }

    public int getUnreadMessageCount(){
        return JMessageClient.getAllUnReadMsgCount();
    }

    public List<Conversation> getAllConversations(){
        UserDto currentUser = UserDao.getInstance().getLoginUser();
        if(currentUser != null){
            return JMessageClient.getConversationList();
        }
        return null;
    }
}
