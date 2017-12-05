package com.iplay.feastbooking.net.utilImpl.selfDetail;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.comment.ReviewListResponse;
import com.iplay.feastbooking.gson.selfInfo.SelfInfo;
import com.iplay.feastbooking.messageEvent.common.CommonMessageEvent;
import com.iplay.feastbooking.messageEvent.contract.PhotoUpdateMessageEvent;
import com.iplay.feastbooking.messageEvent.selfInfo.SelfInfoMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.reviewUtil.ReviewUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gu_y-pc on 2017/12/5.
 */

public class ChangeSelfInfoUtility {
    private static volatile ChangeSelfInfoUtility instance;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String tokenPrefix;

    private final String user;

    private final String avatar;

    private final String profile;

    private final String email;

    private final String password;

    private final String phone;

    private final int connectSeconds;

    private ChangeSelfInfoUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
        tokenPrefix = properties.getProperty("tokenPrefix");
        user = properties.getProperty("user");
        avatar = properties.getProperty("avatar");
        profile = properties.getProperty("profile");
        email = properties.getProperty("email");
        password = properties.getProperty("password");
        phone = properties.getProperty("phone");
        connectSeconds = 120;
    }

    public static ChangeSelfInfoUtility getInstance(Context context){
        if(instance == null){
            synchronized (ReviewUtility.class){
                if(instance == null){
                    instance = new ChangeSelfInfoUtility(context);
                }
            }
        }
        return instance;
    }


    public void updateSelfInfo(Context context){
        if(!NetProperties.isNetworkConnected(context)){
            SelfInfoMessageEvent event = new SelfInfoMessageEvent();
            EventBus.getDefault().post(event);
        }else {
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;
            Request request = new Request.Builder().
                    url(serverUrl + urlSeperator + user + urlSeperator + profile).
                    header("Authorization", token).build();
            OkHttpClient client = new OkHttpClient.Builder().build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    SelfInfoMessageEvent event = new SelfInfoMessageEvent();
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    SelfInfoMessageEvent event = new SelfInfoMessageEvent();
                    EventBus.getDefault().post(event);
                    if(response.isSuccessful()){
                        Gson gson = new Gson();
                        SelfInfo selfInfo =  gson.fromJson(response.body().string(),
                                SelfInfo.class);
                        event.setSelfInfo(selfInfo);
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }



    public void upLoadPortrait(final File file, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            CommonMessageEvent event = new CommonMessageEvent(CommonMessageEvent.TYPE.TYPE_FAILURE, "網絡不給力");
            event.setSuccessResult(file);
            EventBus.getDefault().post(event);
        }else {
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(connectSeconds, TimeUnit.SECONDS)
                    .readTimeout(connectSeconds, TimeUnit.SECONDS)
                    .writeTimeout(connectSeconds, TimeUnit.SECONDS)
                    .build();
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if(file != null){
                RequestBody fileBody = RequestBody.create(null, file);
                builder.addFormDataPart("file", "portrait.jpg", fileBody);
            }
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .header("Authorization", token)
                    .url(serverUrl + urlSeperator +  user + urlSeperator
                            + profile + urlSeperator + avatar)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("upload", "over time" + e);
                    CommonMessageEvent<File> event = new CommonMessageEvent<>(CommonMessageEvent.TYPE.TYPE_FAILURE, "網絡不給力");
                    event.setSuccessResult(file);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    CommonMessageEvent event;
                    if(!response.isSuccessful()){
                        Log.d("upload", response.body().string());
                        event = new CommonMessageEvent(CommonMessageEvent.TYPE.TYPE_FAILURE, "未知錯誤");
                    }else {
                        event = new CommonMessageEvent(CommonMessageEvent.TYPE.TYPE_SUCCESS);
                    }
                    event.setSuccessResult(file);
                    EventBus.getDefault().post(event);
                }
            });
        }
    }
}
