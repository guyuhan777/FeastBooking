package com.iplay.feastbooking.net.utilImpl.hotelDetail;

import android.content.Context;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.hotelDetail.HotelDetail;
import com.iplay.feastbooking.messageEvent.hotelDetail.HotelDetailMessageEvent;

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
 * Created by admin on 2017/10/13.
 */

public class HotelDetailUtility {
    private static volatile HotelDetailUtility utility;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String getHotelByIdAPI;

    private HotelDetailUtility(Context context){
        properties = ProperTies.getProperties(context);
        urlSeperator = properties.getProperty("urlSeperator");
        serverUrl = properties.getProperty("serverUrl");
        getHotelByIdAPI = properties.getProperty("findHotelsById");
    }

    public void initHotelDetail(int hotelId){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        final Request request = new Request.Builder().url(serverUrl + urlSeperator + getHotelByIdAPI + hotelId).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    HotelDetail hotelDetail = gson.fromJson(response.body().string(),HotelDetail.class);
                    HotelDetailMessageEvent messageEvent = new HotelDetailMessageEvent();
                    messageEvent.setHotelDetail(hotelDetail);
                    EventBus.getDefault().post(messageEvent);
                }
            }
        });
    }

    public static HotelDetailUtility getInstance(Context context){
        if(utility == null){
            synchronized (HotelDetailUtility.class){
                if(utility == null) {
                    utility = new HotelDetailUtility(context);
                }
            }
        }
        return utility;
    }
}
