package com.iplay.feastbooking.net.utilImpl.orderdetail;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.orderDetail.OrderDetail;
import com.iplay.feastbooking.messageEvent.order.OrderListMessageEvent;
import com.iplay.feastbooking.messageEvent.orderdetail.OrderDetailMessageEvent;
import com.iplay.feastbooking.net.NetProperties;

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
 * Created by Guyuhan on 2017/11/5.
 */

public class OrderDetailUtility {
    private static volatile OrderDetailUtility utility;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String findOrderByIdAPI;

    private final String tokenPrefix;

    private OrderDetailUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
        findOrderByIdAPI = properties.getProperty("postOrder");
        tokenPrefix = properties.getProperty("tokenPrefix");
    }

    public static OrderDetailUtility getInstance(Context context){
        if(utility == null){
            synchronized (OrderDetailUtility.class){
                if(utility == null) {
                    utility = new OrderDetailUtility(context);
                }
            }
        }
        return utility;
    }

    public void initOrderDetail(int id, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            OrderDetailMessageEvent messageEvent = new OrderDetailMessageEvent();
            messageEvent.setType(OrderListMessageEvent.TYPE_FAILURE);
            messageEvent.setFailureResult("網絡不給力");
            EventBus.getDefault().post(messageEvent);
        }else{
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;
            Request request = new Request.Builder().
                    url(serverUrl + urlSeperator + findOrderByIdAPI + urlSeperator + id).
                    header("Authorization", token).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    OrderDetailMessageEvent messageEvent = new OrderDetailMessageEvent();
                    messageEvent.setType(OrderDetailMessageEvent.TYPE_FAILURE);
                    messageEvent.setFailureResult("未知錯誤");
                    EventBus.getDefault().post(messageEvent);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    OrderDetailMessageEvent event = new OrderDetailMessageEvent();
                    if(!response.isSuccessful()){
                        event.setType(OrderDetailMessageEvent.TYPE_FAILURE);
                        int code = response.code();
                        Log.d("code", code + "");
                        Log.d("body", response.body().string());
                        if(code == 401){
                            event.setFailureResult("尚未登錄");
                        }else {
                            event.setFailureResult("未知錯誤");
                        }
                    }else {
                        event.setType(OrderDetailMessageEvent.TYPE_SUCCESS);
                        Gson gson = new Gson();
                        OrderDetail orderDetail = gson.fromJson(response.body().string(), OrderDetail.class);
                        event.setOrderDetail(orderDetail);
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }
}
