package com.iplay.feastbooking.net.utilImpl.jMessage;

import android.content.Context;

import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.assistance.SecurityUtility;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.dto.UserDto;
import com.iplay.feastbooking.ui.message.data.BasicMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public List<BasicMessage> getAllMessages(){
        final List<BasicMessage> basicMessages = new ArrayList<>();
        List<Conversation> conversations = JMessageClient.getConversationList();
        if(conversations != null) {
            for (Conversation conversation : conversations) {
                if (conversation != null) {
                    List<Message> conversationMessages = conversation.getAllMessage();
                    for(int i=0; i< conversationMessages.size(); i++){
                        Message message = conversationMessages.get(i);
                        if(BasicMessage.isMessageValid(message)){
                            BasicMessage basicMessage = new BasicMessage();
                            basicMessage.setMessage(message);
                            basicMessage.setConversation(conversation);
                            basicMessages.add(basicMessage);
                        }else {
                            conversation.deleteMessage(message.getId());
                        }
                    }
                }
            }
        }
        Collections.sort(basicMessages, new Comparator<BasicMessage>() {
            @Override
            public int compare(BasicMessage o1, BasicMessage o2) {
                return o1.getMessage().getCreateTime() > o2.getMessage().getCreateTime() ? -1 : 1;
            }
        });
        return basicMessages;
    }

    public int getUnreadMessageCount(){
        int unreadCtn = 0;
        List<Conversation> conversations = JMessageClient.getConversationList();
        if(conversations != null) {
            for (Conversation conversation : conversations) {
                if (conversation != null) {
                    List<Message> messages = conversation.getAllMessage();
                    if(messages != null){
                        for(Message message : messages){
                            if(BasicMessage.isMessageValid(message)){
                                if(!message.haveRead()){
                                    ++unreadCtn;
                                }
                            }else {
                                conversation.deleteMessage(message.getId());
                            }
                        }
                    }
                }
            }
        }
        return unreadCtn;
    }

    public List<Conversation> getAllConversations(){
        UserDto currentUser = UserDao.getInstance().getLoginUser();
        if(currentUser != null){
            return JMessageClient.getConversationList();
        }
        return null;
    }
}
