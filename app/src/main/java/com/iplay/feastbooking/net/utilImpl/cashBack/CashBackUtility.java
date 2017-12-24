package com.iplay.feastbooking.net.utilImpl.cashBack;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.cashBack.CashBackMessageEvent;
import com.iplay.feastbooking.gson.cashBack.OrderCashBackMessageEvent;
import com.iplay.feastbooking.gson.consult.CommonStateResponse;
import com.iplay.feastbooking.gson.selfInfo.ChangePasswordRequest;
import com.iplay.feastbooking.messageEvent.selfInfo.ChangePasswordMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.message.UtilMessage;
import com.iplay.feastbooking.net.utilImpl.reviewUtil.ReviewUtility;
import com.iplay.feastbooking.net.utilImpl.selfDetail.ChangeSelfInfoUtility;

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
 * Created by gu_y-pc on 2017/12/16.
 */

public class CashBackUtility {

    private static volatile CashBackUtility instance;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String tokenPrefix;

    private final String user;

    private final String cashBack;

    private final String orders;

    private CashBackUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
        tokenPrefix = properties.getProperty("tokenPrefix");
        user = properties.getProperty("user");
        cashBack = properties.getProperty("cashBack");
        orders = properties.getProperty("postOrder");
    }

    public static CashBackUtility getInstance(Context context){
        if(instance == null){
            synchronized (CashBackUtility.class){
                if(instance == null){
                    instance = new CashBackUtility(context);
                }
            }
        }
        return instance;
    }

    public void getCashBackForCertainOrder(int orderId, Context context){
        if(NetProperties.isNetworkConnected(context)){
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if (token == null || token.equals("")) {
                return;
            }
            token = tokenPrefix + " " + token;
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            Request request = new Request
                    .Builder()
                    .url(serverUrl + urlSeperator + orders + urlSeperator
                            + orderId + urlSeperator + cashBack)
                    .header("Authorization", token).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("reason", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        Gson gson = new Gson();
                        OrderCashBackMessageEvent event = gson.fromJson(response.body().string(), OrderCashBackMessageEvent.class);
                        EventBus.getDefault().post(event);
                    }else {
                        Log.d("reason", response.body().string());
                    }
                }
            });
        }
    }

    public void getCashBack(Context context) {
        if (NetProperties.isNetworkConnected(context)) {
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if (token == null || token.equals("")) {
                return;
            }
            token = tokenPrefix + " " + token;
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            Request request = new Request
                    .Builder()
                    .url(serverUrl + urlSeperator + user + urlSeperator
                            + cashBack)
                    .header("Authorization", token).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        CashBackMessageEvent event = gson.fromJson(response.body().string(), CashBackMessageEvent.class);
                        EventBus.getDefault().post(event);
                    }
                }
            });
        }
    }

}
