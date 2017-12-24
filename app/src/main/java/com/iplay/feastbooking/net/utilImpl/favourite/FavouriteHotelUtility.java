package com.iplay.feastbooking.net.utilImpl.favourite;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.favourite.FavouriteHotelRequest;
import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.messageEvent.common.CommonMessageEvent;
import com.iplay.feastbooking.messageEvent.favourite.FavouriteIfExistMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.message.UtilMessage;
import com.iplay.feastbooking.net.utilImpl.consult.ConsultUtility;

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
 * Created by gu_y-pc on 2017/12/24.
 */

public class FavouriteHotelUtility {

    private static volatile FavouriteHotelUtility instance;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String tokenPrefix;

    private final String hotels;

    private final String favourites;

    private final String user;

    private final String restUrl;

    private FavouriteHotelUtility(Context context){
        properties = ProperTies.getProperties(context);
        urlSeperator = properties.getProperty("urlSeperator");
        serverUrl = properties.getProperty("serverUrl");
        tokenPrefix = properties.getProperty("tokenPrefix");
        hotels = properties.getProperty("listHotelsForUser");
        favourites = properties.getProperty("favourites");
        user = properties.getProperty("user");
        restUrl = serverUrl + urlSeperator
                + user + urlSeperator + favourites + urlSeperator + hotels;
    }

    public static FavouriteHotelUtility getInstance(Context context){
        if(instance == null){
            synchronized (ConsultUtility.class){
                if(instance == null){
                    instance = new FavouriteHotelUtility(context);
                }
            }
        }
        return instance;
    }

    public void favouriteHotel(Context context, int hotelId, boolean isDelete){
        if(NetProperties.isNetworkConnected(context)){
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;
            final Gson gson = new Gson();
            FavouriteHotelRequest favouriteHotelRequest = new FavouriteHotelRequest();
            favouriteHotelRequest.setId(hotelId);
            String json = gson.toJson(favouriteHotelRequest);
            RequestBody body = RequestBody.create(UtilMessage.JSON, json);

            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
            Request.Builder builder = new Request.Builder().header("Authorization", token)
                    .url(restUrl + urlSeperator);
            if(isDelete){
                builder.delete(body);
            }else {
                builder.post(body);
            }
            Request request = builder.build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // do nothing
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // do nothing
                }
            });
        }
    }

    public void checkIfFavourites(Context context, int hotelId){
        if(NetProperties.isNetworkConnected(context)){
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().header("Authorization", token)
                    .url(restUrl + urlSeperator + "?id=" + hotelId).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        Gson gson = new Gson();
                        RecommendHotelGO recommendHotelGO = gson.fromJson(response.body().string(), RecommendHotelGO.class);
                        FavouriteIfExistMessageEvent event = new FavouriteIfExistMessageEvent();
                        event.setExist(recommendHotelGO != null);
                        EventBus.getDefault().post(event);
                    }
                }
            });
        }
    }
}
