package com.iplay.feastbooking.net.utilImpl.order;

import android.content.Context;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.order.OrderListItem;
import com.iplay.feastbooking.messageEvent.order.OrderListMessageEvent;

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
 * Created by Guyuhan on 2017/10/28.
 */

public class OrderListUtility {

    private static volatile OrderListUtility utility;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String getOrderListAPI;

    private final String tokenPrefix;

    private OrderListUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
        getOrderListAPI = properties.getProperty("getOrderList");
        tokenPrefix = properties.getProperty("tokenPrefix");
    }

    public static OrderListUtility getInstance(Context context){
        if(utility == null){
            synchronized (OrderListUtility.class) {
                if (utility == null) utility = new OrderListUtility(context);
            }
        }
        return utility;
    }

    public void loadMore(int page, final boolean isInit){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
        String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
        if(token == null || token.equals("")){
            return;
        }
        token = tokenPrefix + " " +  token;

        Request request = new Request.Builder().
                url(serverUrl + urlSeperator + getOrderListAPI + "?status=UNFINISHED&page=" + page).
                header("Authorization", token).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OrderListMessageEvent event = new OrderListMessageEvent();
                if(isInit) {
                    event.setInit(true);
                }
                event.setType(OrderListMessageEvent.TYPE_NO_INTERNET);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                OrderListMessageEvent event = new OrderListMessageEvent();
                if(isInit) {
                    event.setInit(true);
                }
                if(!response.isSuccessful()){
                    event.setType(OrderListMessageEvent.TYPE_FAILURE);
                    if(response.code() == 401){
                        event.setFailureReason("尚未登錄");
                    }else {
                        event.setFailureReason("未知錯誤");
                    }
                }else {
                    event.setType(OrderListMessageEvent.TYPE_SUCCESS);
                    Gson gson = new Gson();
                    OrderListItem orderListItem = gson.fromJson(response.body().string(), OrderListItem.class);
                    event.setOrderListItemList(orderListItem);
                }
                EventBus.getDefault().post(event);
            }
        });
    }

    public void initOrderList(){
        loadMore(0,true);
    }
}
