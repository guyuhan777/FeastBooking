package com.iplay.feastbooking.net.utilImpl.consult;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.consult.ConsultResult;
import com.iplay.feastbooking.gson.consult.ConsultVO;
import com.iplay.feastbooking.messageEvent.consult.ConsultOrderMessageEvent;
import com.iplay.feastbooking.net.message.UtilMessage;

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
 * Created by gu_y-pc on 2017/10/23.
 */

public class ConsultUtility {

    private static volatile ConsultUtility instance;

    private Context mContext;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String postOrderAPI;

    private final String tokenPrefix;

    private ConsultUtility(Context context){
        mContext = context;
        properties = ProperTies.getProperties(context);
        urlSeperator = properties.getProperty("urlSeperator");
        serverUrl = properties.getProperty("serverUrl");
        postOrderAPI = properties.getProperty("postOrder");
        tokenPrefix = properties.getProperty("tokenPrefix");
    }

    public static ConsultUtility getInstance(Context context){
        if(instance == null){
            synchronized (ConsultUtility.class){
                if(instance == null){
                    instance = new ConsultUtility(context);
                }
            }
        }
        return instance;
    }

    public void createConsultOrder(ConsultVO consultVO){
        if(consultVO == null || LoginUserHolder.getInstance().getCurrentUser() == null){
            return;
        }
        String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
        if(token == null || token.equals("")){
            return;
        }

        token = tokenPrefix + " " +  token;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
        final Gson gson = new Gson();
        String json = gson.toJson(consultVO);
        RequestBody body = RequestBody.create(UtilMessage.JSON, json);
        final Request request = new Request.Builder().post(body).header("Authorization", token).url(serverUrl + urlSeperator + postOrderAPI ).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ConsultOrderMessageEvent event = new ConsultOrderMessageEvent();
                event.setType(ConsultOrderMessageEvent.TYPE_FAILURE);
                event.setFailureReason("連接超時");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Log.d("status", response.code() + "");
                    Log.d("body",response.body().string());
                    ConsultOrderMessageEvent event = new ConsultOrderMessageEvent();
                    event.setType(ConsultOrderMessageEvent.TYPE_FAILURE);
                    event.setFailureReason("未知错误");
                    EventBus.getDefault().post(event);
                }else {
                    ConsultOrderMessageEvent event = new ConsultOrderMessageEvent();
                    ConsultResult result = gson.fromJson(response.body().string(), ConsultResult.class);
                    if(result.success){
                        Log.d("success",((Double) result.data) + "");
                    }
                    event.setType(ConsultOrderMessageEvent.TYPE_SUCCESS);
                    event.setResult(result);
                    EventBus.getDefault().post(event);
                }
            }
        });
    }
}
