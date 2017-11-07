package com.iplay.feastbooking.net.utilImpl.registerUtil;

import android.content.Context;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.register.TotpConfirm;
import com.iplay.feastbooking.gson.register.VerifyResponse;
import com.iplay.feastbooking.messageEvent.register.CodeValidMessageEvent;
import com.iplay.feastbooking.messageEvent.register.RegisterMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.message.UtilMessage;
import com.iplay.feastbooking.ui.login.RegisterConfirmActivity;

import org.greenrobot.eventbus.EventBus;

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
 * Created by admin on 2017/9/27.
 */

public class RegisterValidUtility {

    private static volatile RegisterValidUtility utility;

    private final Properties properties;

    private final String serverUrl;

    private final String applyForRegistrationEmailAPI;

    private final String urlSeperator;

    private final String verify ;

    private RegisterValidUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        applyForRegistrationEmailAPI = properties.getProperty("applyForRegistrationEmail");
        urlSeperator = properties.getProperty("urlSeperator");
        verify = properties.getProperty("verify");
    }

    public void applyForRegistrationEmail(String email, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            CodeValidMessageEvent event = new CodeValidMessageEvent();
            event.setType(CodeValidMessageEvent.TYPE_NO_INTERNET);
            EventBus.getDefault().post(event);
        }else {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url(serverUrl + urlSeperator + applyForRegistrationEmailAPI + "?email=" + email).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    CodeValidMessageEvent event = new CodeValidMessageEvent();
                    event.setType(CodeValidMessageEvent.TYPE_CONNECT_TIME_OVER);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        CodeValidMessageEvent event = new CodeValidMessageEvent();
                        event.setType(CodeValidMessageEvent.TYPE_UNKNOWN_ERROR);
                        EventBus.getDefault().post(event);
                    }else {
                        if(response.body().string().equals("false")){
                            CodeValidMessageEvent event = new CodeValidMessageEvent();
                            event.setType(CodeValidMessageEvent.TYPE_EMAIL_ALREADY_EXIST);
                            EventBus.getDefault().post(event);
                        }else {
                            CodeValidMessageEvent event = new CodeValidMessageEvent();
                            event.setType(CodeValidMessageEvent.TYPE_SUCCESS);
                            EventBus.getDefault().post(event);
                        }
                    }
                }
            });
        }
    }

    public void verify(final String email, String totp, final Context context){
        if(!NetProperties.isNetworkConnected(context)){
            RegisterMessageEvent event = new RegisterMessageEvent();
            event.setType(RegisterMessageEvent.TYPE_NO_INTERNET);
            EventBus.getDefault().post(event);
        }else {
            TotpConfirm totpConfirm = new TotpConfirm();
            totpConfirm.email = email;
            totpConfirm.totp = totp;
            final Gson gson = new Gson();
            String json = gson.toJson(totpConfirm);
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            RequestBody body = RequestBody.create(UtilMessage.JSON, json);
            Request request = new Request.Builder().url(serverUrl + urlSeperator + verify).post(body).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    RegisterMessageEvent event = new RegisterMessageEvent();
                    event.setType(RegisterMessageEvent.TYPE_CONNECT_TIME_OUT);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    RegisterMessageEvent event = new RegisterMessageEvent();
                    if(!response.isSuccessful()){
                        event.setType(RegisterMessageEvent.TYPE_UNKNOWN_ERROR);
                    }else {
                        VerifyResponse verifyResponse = gson.fromJson(response.body().string(),VerifyResponse.class);
                        if(verifyResponse.totpValid){
                            RegisterConfirmActivity.start(context,verifyResponse.token,email);
                        }else {
                            event.setType(RegisterMessageEvent.TYPE_FAILURE);
                        }
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }

    public static RegisterValidUtility getInstance(Context context){
        if(utility == null){
            synchronized (RegisterValidUtility.class){
                utility = new RegisterValidUtility(context);
            }
        }
        return utility;
    }

}
