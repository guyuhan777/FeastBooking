package com.iplay.feastbooking.net.utilImpl.feast;

import android.content.Context;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.feast.FeastDetail;
import com.iplay.feastbooking.messageEvent.feast.FeastMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2017/10/14.
 */

public class FeastUtility {
    private static volatile FeastUtility utility;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String findFeastsByIdAPI;

    private FeastUtility(Context context){
        properties = ProperTies.getProperties(context);
        urlSeperator = properties.getProperty("urlSeperator");
        serverUrl = properties.getProperty("serverUrl");
        findFeastsByIdAPI = properties.getProperty("findFeastsById");
    }

    public void initFeast(int feastId){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        final Request request = new Request.Builder().url(serverUrl + urlSeperator + findFeastsByIdAPI + feastId).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    FeastDetail feastDetail = gson.fromJson(response.body().string(), FeastDetail.class);
                    FeastMessageEvent event = new FeastMessageEvent();
                    event.setFeastDetail(feastDetail);
                    EventBus.getDefault().post(event);
                }
            }
        });
    }

    public static FeastUtility getInstance(Context context){
        if(utility == null){
            synchronized (FeastUtility.class){
                if(utility == null) {
                    utility = new FeastUtility(context);
                }
            }
        }
        return utility;
    }
}
