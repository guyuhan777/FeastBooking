package com.iplay.feastbooking.net.utilImpl.banquetHall;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.room.BanquetHallDetail;
import com.iplay.feastbooking.messageEvent.banquetHall.BanquetHallMessageEvent;

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

public class BanquetHallUtility {
    private static volatile BanquetHallUtility utility;

    private Context mContext;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String findBanquetHallByIdAPI;

    private BanquetHallUtility(Context context){
        mContext = context;
        properties = ProperTies.getProperties(context);
        urlSeperator = properties.getProperty("urlSeperator");
        serverUrl = properties.getProperty("serverUrl");
        findBanquetHallByIdAPI = properties.getProperty("findBanquetHallById");
    }

    public void initBanquetHall(int id){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(serverUrl + urlSeperator + findBanquetHallByIdAPI + id).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    String json = response.body().string();
                    BanquetHallDetail detail = gson.fromJson(json, BanquetHallDetail.class);
                    Log.d("detail",detail.toString());
                    BanquetHallMessageEvent event = new BanquetHallMessageEvent();
                    event.setBanquetHallDetail(detail);
                    EventBus.getDefault().post(event);
                }
            }
        });
    }

    public static BanquetHallUtility getInstance(Context context){
        if(utility == null){
            synchronized (BanquetHallUtility.class){
                utility = new BanquetHallUtility(context);
            }
        }
        return utility;
    }
}
