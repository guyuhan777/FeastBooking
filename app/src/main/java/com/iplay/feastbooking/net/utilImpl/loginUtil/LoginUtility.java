package com.iplay.feastbooking.net.utilImpl.loginUtil;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.dto.UserDto;
import com.iplay.feastbooking.gson.login.LoginRequest;
import com.iplay.feastbooking.gson.login.LoginResponse;
import com.iplay.feastbooking.messageEvent.home.LoginMessageEvent;
import com.iplay.feastbooking.messageEvent.home.NoInternetMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.message.UtilMessage;

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
 * Created by admin on 2017/10/7.
 */

public class LoginUtility {

    private static volatile LoginUtility utility;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String loginAPI;

    private LoginUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
        loginAPI = properties.getProperty("createAuthenticationToken");
    }

    public void login(final String username, final String password, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            NoInternetMessageEvent event = new NoInternetMessageEvent();
            event.setType(NoInternetMessageEvent.TYPE_LOGIN);
            EventBus.getDefault().post(event);
        }else{
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.password = password;
            loginRequest.username = username;
            final Gson gson = new Gson();
            String json = gson.toJson(loginRequest);
            RequestBody body = RequestBody.create(UtilMessage.JSON, json);
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
            final Request request = new Request.Builder().url(serverUrl + urlSeperator + loginAPI).post(body).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LoginMessageEvent event = new LoginMessageEvent();
                    event.setType(LoginMessageEvent.TYPE_CONNECT_TIME_OUT);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    LoginMessageEvent event = new LoginMessageEvent();
                    if(!response.isSuccessful()){
                        event.setType(LoginMessageEvent.TYPE_FAILURE);
                    }else {
                        LoginResponse loginResponse = gson.fromJson(response.body().string(),LoginResponse.class);
                        handleResponse(loginResponse,username,password);
                        event.setType(LoginMessageEvent.TYPE_SUCCESS);
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }

    private void handleResponse(LoginResponse response,String username,String password){
        LoginResponse.User user = response.user;
        if(user != null){
            UserDto userDto = DataSupport.where("userId = ?", user.id + "").findFirst(UserDto.class);
            if(userDto == null) {
                userDto = new UserDto();
                userDto.setUserId(user.id);
                userDto.setUsername(username);
                userDto.setPassword(password);
                userDto.setToken(response.token);
                userDto.setLogin(true);
                userDto.save();
            }
            ContentValues values = new ContentValues();
            values.put("isLogin",false);
            DataSupport.updateAll(UserDto.class, values, "userId != ?", user.id + "");
        }
    }

    public static LoginUtility getInstance(Context context){
        if(utility == null){
            synchronized (LoginUtility.class){
                utility = new LoginUtility(context);
            }
        }
        return utility;
    }
}
