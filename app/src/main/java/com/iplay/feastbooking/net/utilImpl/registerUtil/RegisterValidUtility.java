package com.iplay.feastbooking.net.utilImpl.registerUtil;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.Token;
import com.iplay.feastbooking.gson.register.TotpConfirm;
import com.iplay.feastbooking.net.message.UtilMessage;
import com.iplay.feastbooking.ui.login.RegisterActivity;
import com.iplay.feastbooking.ui.login.RegisterConfirmActivity;

import java.io.IOException;
import java.net.SocketTimeoutException;
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

    private final String totpPostAPI;

    private final Context mContext;

    private RegisterValidUtility(Context context){
        mContext = context;
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        applyForRegistrationEmailAPI = properties.getProperty("applyForRegistrationEmail");
        urlSeperator = properties.getProperty("urlSeperator");
        totpPostAPI = properties.getProperty("totpPost");
    }

    public void applyForRegistrationEmail(String email){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        final RegisterActivity registerActivity = (RegisterActivity) mContext;

        final Handler handler = registerActivity.validHandler;
        handler.sendEmptyMessage(100);

        Request request = new Request.Builder().url(serverUrl + urlSeperator + applyForRegistrationEmailAPI + "?email="+email).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e.getCause().equals(SocketTimeoutException.class)){
                    registerActivity.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"连接超时",Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }else{
                    registerActivity.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"未知错误",Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
                registerActivity.setValid_btn_state(ProperTies.TYPE_BTN_ACTIVE);
                handler.sendEmptyMessage(61);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    registerActivity.setValid_btn_state(ProperTies.TYPE_BTN_ACTIVE);
                    registerActivity.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"未知错误",Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    handler.sendEmptyMessage(61);
                }else {
                    Gson gson = new Gson();
                    boolean isRegistered = gson.fromJson(response.body().string(),Boolean.class);
                    if(!isRegistered){
                        registerActivity.setValid_btn_state(ProperTies.TYPE_BTN_ACTIVE);
                        registerActivity.runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"邮箱已存在",Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                        handler.sendEmptyMessage(61);
                    }else{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(int i=60; i>=0; i--){
                                    handler.sendEmptyMessage(i);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                registerActivity.setValid_btn_state(ProperTies.TYPE_BTN_ACTIVE);
                                handler.sendEmptyMessage(61);
                            }
                        }).start();
                    }
                }
            }
        });

    }

    public void verify(TotpConfirm confirm){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        final RegisterActivity registerActivity = (RegisterActivity) mContext;
        final String mail = confirm.email;
        final Gson gson = new Gson();
        String json = gson.toJson(confirm);
        RequestBody body = RequestBody.create(UtilMessage.JSON, json);
        Request request = new Request.Builder().url(serverUrl + urlSeperator + totpPostAPI).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e.getCause().equals(SocketTimeoutException.class)){
                    registerActivity.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"连接超时",Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }else{
                    registerActivity.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"未知错误",Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
                registerActivity.setNext_btn_state(ProperTies.TYPE_BTN_ACTIVE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    registerActivity.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"未知错误",Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }else {
                    Token token = gson.fromJson(response.body().string(),Token.class);
                    if (!token.totpValid){
                        registerActivity.runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"验证码错误",Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }else{
                        RegisterConfirmActivity.start(mContext, token.token, mail);
                    }
                }
                registerActivity.setNext_btn_state(ProperTies.TYPE_BTN_ACTIVE);
            }
        });
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
