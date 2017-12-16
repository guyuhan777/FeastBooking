package com.iplay.feastbooking.net.utilImpl.selfDetail;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.dto.UserDto;
import com.iplay.feastbooking.gson.common.CommonSingleStringRequest;
import com.iplay.feastbooking.gson.consult.CommonStateResponse;
import com.iplay.feastbooking.gson.register.TotpConfirm;
import com.iplay.feastbooking.gson.register.VerifyResponse;
import com.iplay.feastbooking.gson.selfInfo.ChangePasswordRequest;
import com.iplay.feastbooking.gson.selfInfo.SelfInfo;
import com.iplay.feastbooking.messageEvent.register.RegisterMessageEvent;
import com.iplay.feastbooking.messageEvent.selfInfo.ChangeEmailConfirmMessageEvent;
import com.iplay.feastbooking.messageEvent.selfInfo.ChangePasswordMessageEvent;
import com.iplay.feastbooking.messageEvent.selfInfo.ChangePhoneMessageEvent;
import com.iplay.feastbooking.messageEvent.selfInfo.ChangePortraitMessageEvent;
import com.iplay.feastbooking.messageEvent.selfInfo.SelfInfoMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.message.UtilMessage;
import com.iplay.feastbooking.net.utilImpl.reviewUtil.ReviewUtility;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
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

    private final String mailChange;

    private final String passwordChange;

    private final String phoneChange;

    private final String verify;

    private final int connectSeconds;

    private ChangeSelfInfoUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
        tokenPrefix = properties.getProperty("tokenPrefix");
        user = properties.getProperty("user");
        avatar = properties.getProperty("avatar");
        profile = properties.getProperty("profile");
        mailChange = properties.getProperty("email");
        passwordChange = properties.getProperty("password");
        phoneChange = properties.getProperty("phone");
        verify = properties.getProperty("verify");
        connectSeconds = 120;
    }

    public static ChangeSelfInfoUtility getInstance(Context context){
        if(instance == null){
            synchronized (ChangeSelfInfoUtility.class){
                if(instance == null){
                    instance = new ChangeSelfInfoUtility(context);
                }
            }
        }
        return instance;
    }

    public void updatePassword(final String newPassword, String password, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            ChangePasswordMessageEvent event = new ChangePasswordMessageEvent();
            event.setType(ChangePasswordMessageEvent.TYPE.FAILURE);
            event.setFailureResult("網絡不給力");
            EventBus.getDefault().post(event);
        }else {
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;

            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(password, newPassword);
            final Gson gson = new Gson();
            String json = gson.toJson(changePasswordRequest);
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            RequestBody body = RequestBody.create(UtilMessage.JSON, json);
            Request request = new Request
                    .Builder()
                    .url(serverUrl + urlSeperator +  user + urlSeperator
                            + profile + urlSeperator + passwordChange)
                    .put(body)
                    .header("Authorization", token).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ChangePasswordMessageEvent event = new ChangePasswordMessageEvent();
                    event.setType(ChangePasswordMessageEvent.TYPE.FAILURE);
                    event.setFailureResult("網絡不給力");
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ChangePasswordMessageEvent event = new ChangePasswordMessageEvent();
                    if(!response.isSuccessful()){
                        event.setType(ChangePasswordMessageEvent.TYPE.FAILURE);
                        event.setFailureResult("未知錯誤");
                    }else {
                        CommonStateResponse commonStateRespone = gson.fromJson(response.body().string(), CommonStateResponse.class);
                        if(commonStateRespone.success){
                            event.setType(ChangePasswordMessageEvent.TYPE.SUCCESS);
                            event.setPassword(newPassword);
                        }else {
                            event.setType(ChangePasswordMessageEvent.TYPE.FAILURE);
                            event.setFailureResult("原密碼錯誤");
                        }
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }

    public void updatePhone(final String phone, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            ChangePhoneMessageEvent event = new ChangePhoneMessageEvent();
            event.setType(ChangePhoneMessageEvent.TYPE.FAILURE);
            event.setFailureResult("網絡不給力");
            EventBus.getDefault().post(event);
        }else {
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;

            CommonSingleStringRequest singleStringRequest = new CommonSingleStringRequest();
            singleStringRequest.setValue(phone);
            final Gson gson = new Gson();
            String json = gson.toJson(singleStringRequest);
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            final RequestBody body = RequestBody.create(UtilMessage.JSON, json);
            final Request request = new Request
                    .Builder()
                    .url(serverUrl + urlSeperator +  user + urlSeperator
                            + profile + urlSeperator + phoneChange)
                    .put(body)
                    .header("Authorization", token).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ChangePhoneMessageEvent event = new ChangePhoneMessageEvent();
                    event.setType(ChangePhoneMessageEvent.TYPE.FAILURE);
                    event.setFailureResult("網絡不給力");
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ChangePhoneMessageEvent event = new ChangePhoneMessageEvent();
                    if(!response.isSuccessful()){
                        Log.d("reason", response.body().string());
                        event.setType(ChangePhoneMessageEvent.TYPE.FAILURE);
                        event.setFailureResult("未知錯誤");
                    }else {
                        CommonStateResponse commonStateRespone = gson.fromJson(response.body().string(), CommonStateResponse.class);
                        if(commonStateRespone.success){
                            event.setType(ChangePhoneMessageEvent.TYPE.SUCCESS);
                            event.setPhone(phone);
                        }else {
                            event.setType(ChangePhoneMessageEvent.TYPE.FAILURE);
                            Log.d("reason", response.body().string());
                            event.setFailureResult("未知錯誤");
                        }
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }

    public void confirmToken(final String emailConfirmToken, final String email, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            ChangeEmailConfirmMessageEvent event = new ChangeEmailConfirmMessageEvent();
            event.setType(ChangeEmailConfirmMessageEvent.TYPE.FAILURE);
            event.setFailureResult("網絡不給力");
            EventBus.getDefault().post(event);
        }else {
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;

            CommonSingleStringRequest singleStringRequest = new CommonSingleStringRequest();
            singleStringRequest.setValue(tokenPrefix + " " +  emailConfirmToken);
            final Gson gson = new Gson();
            String json = gson.toJson(singleStringRequest);
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            RequestBody body = RequestBody.create(UtilMessage.JSON, json);
            Request request = new Request
                    .Builder()
                    .url(serverUrl + urlSeperator +  user + urlSeperator
                    + profile + urlSeperator + mailChange)
                    .put(body)
                    .header("Authorization", token).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ChangeEmailConfirmMessageEvent event = new ChangeEmailConfirmMessageEvent();
                    event.setType(ChangeEmailConfirmMessageEvent.TYPE.FAILURE);
                    event.setFailureResult("網絡不給力");
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ChangeEmailConfirmMessageEvent event = new ChangeEmailConfirmMessageEvent();
                    if(!response.isSuccessful()){
                        event.setType(ChangeEmailConfirmMessageEvent.TYPE.FAILURE);
                        event.setFailureResult("未知錯誤");
                    }else {
                        event.setType(ChangeEmailConfirmMessageEvent.TYPE.SUCCESS);
                        event.setEmail(email);
                    }
                    EventBus.getDefault().post(event);
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
                        Log.d("reason", response.body().string());
                        event.setType(RegisterMessageEvent.TYPE_UNKNOWN_ERROR);
                    }else {
                        VerifyResponse verifyResponse = gson.fromJson(response.body().string(),VerifyResponse.class);
                        if(verifyResponse.totpValid){
                            event.setType(RegisterMessageEvent.TYPE_SUCCESS);
                            event.setToken(verifyResponse.token);
                            event.setEmail(email);
                        }else {
                            event.setType(RegisterMessageEvent.TYPE_FAILURE);
                        }
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
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
                        if(selfInfo == null){
                            return;
                        }
                        UserDto userDto = DataSupport.where("userId = ?", selfInfo.userId + "").findFirst(UserDto.class);
                        if(userDto == null){
                            userDto = new UserDto();
                            userDto.setUserId(selfInfo.userId);
                        }
                        userDto.setAvatarUrl(selfInfo.avatar);
                        userDto.setEmail(selfInfo.email);
                        userDto.setUsername(selfInfo.username);
                        userDto.setPhone(selfInfo.phone);
                        userDto.save();

                        event.setSelfInfo(selfInfo);
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }

    public void upLoadPortrait(final File file, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            ChangePortraitMessageEvent event = new ChangePortraitMessageEvent();
            event.setType(ChangePortraitMessageEvent.TYPE_FAILURE);
            event.setFile(file);
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
                    ChangePortraitMessageEvent event = new ChangePortraitMessageEvent();
                    event.setType(ChangePortraitMessageEvent.TYPE_FAILURE);
                    event.setFile(file);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ChangePortraitMessageEvent event = new ChangePortraitMessageEvent();
                    event.setFile(file);
                    if(!response.isSuccessful()){
                        Log.d("upload", response.body().string());
                        event.setType(ChangePortraitMessageEvent.TYPE_FAILURE);
                    }else {
                        event.setType(ChangePortraitMessageEvent.TYPE_SUCCESS);
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }
}
