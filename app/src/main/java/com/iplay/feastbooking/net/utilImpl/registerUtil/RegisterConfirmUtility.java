package com.iplay.feastbooking.net.utilImpl.registerUtil;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.gson.register.RegisterConfirmRequest;
import com.iplay.feastbooking.gson.register.RegisterConfirmResponse;
import com.iplay.feastbooking.messageEvent.register.RegisterConfirmMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.message.UtilMessage;
import com.iplay.feastbooking.ui.home.HomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2017/9/29.
 */

public class RegisterConfirmUtility {

    private static volatile RegisterConfirmUtility utility;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String registerAPI;

    private final String tokenPrefix;

    private RegisterConfirmUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
        registerAPI = properties.getProperty("register");
        tokenPrefix = properties.getProperty("tokenPrefix");
    }

    public void register(String token, final String mail, final String username, final String password, final Context context){
        if(!NetProperties.isNetworkConnected(context)){
            RegisterConfirmMessageEvent event = new RegisterConfirmMessageEvent();
            event.setType(RegisterConfirmMessageEvent.TYPE_NO_INTERNET);
            EventBus.getDefault().post(event);
        }else {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            final Gson gson = new Gson();
            RegisterConfirmRequest registerConfirmRequest = new RegisterConfirmRequest();
            registerConfirmRequest.password = password;
            registerConfirmRequest.username = username;
            String json = gson.toJson(registerConfirmRequest);
            RequestBody body = RequestBody.create(UtilMessage.JSON, json);
            Request req = new Request.Builder().url(serverUrl + urlSeperator + registerAPI).header("Authorization", tokenPrefix + " " + token)
                    .post(body).build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    RegisterConfirmMessageEvent event = new RegisterConfirmMessageEvent();
                    event.setType(RegisterConfirmMessageEvent.TYPE_CONNECT_TIME_OVER);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        RegisterConfirmMessageEvent event = new RegisterConfirmMessageEvent();
                        event.setType(RegisterConfirmMessageEvent.TYPE_UNKNOWN_ERROR);
                        EventBus.getDefault().post(event);
                    } else {
                        RegisterConfirmResponse confirmResponse = gson.fromJson(response.body().string(), RegisterConfirmResponse.class);
                        if (!confirmResponse.success) {
                            String reason = (String) confirmResponse.data;
                            RegisterConfirmMessageEvent event = new RegisterConfirmMessageEvent();
                            event.setType(RegisterConfirmMessageEvent.TYPE_FAILURE);
                            event.setResult(reason);
                            EventBus.getDefault().post(event);
                        } else {
                            LinkedTreeMap map = (LinkedTreeMap) confirmResponse.data;
                            String userToken = (String) map.get("token");
                            int userId = ((Double) map.get("userId")).intValue();
                            UserDao userDao = new UserDao();
                            userDao.setUserId(userId);
                            userDao.setToken(userToken);
                            userDao.setUsername(username);
                            userDao.setPassword(password);
                            userDao.setEmail(mail);
                            userDao.setLogin(true);
                            userDao.save();
                            ContentValues values = new ContentValues();
                            values.put("isLogin", false);
                            DataSupport.updateAll(UserDao.class, values, "userId != ?", userId + "");
                            HomeActivity.startActivity(context);
                        }
                    }
                }
            });
        }
    }

    public static RegisterConfirmUtility getInstance(Context context){
        if(utility == null){
            synchronized (RegisterConfirmUtility.class){
                utility = new RegisterConfirmUtility(context);
            }
        }
        return utility;
    }

}
