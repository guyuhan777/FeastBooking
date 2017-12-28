package com.iplay.feastbooking.net.utilImpl.recommendHotelUtil;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.dto.AdvertisementDto;
import com.iplay.feastbooking.dto.RecommendGridDto;
import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.entity.RecommendGrid;
import com.iplay.feastbooking.gson.homepage.RecommendGridGO;
import com.iplay.feastbooking.gson.homepage.advertisement.AdvertisementGO;
import com.iplay.feastbooking.gson.homepage.hotelList.HotelListRequireConfig;
import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.messageEvent.home.AdvertisementMessageEvent;
import com.iplay.feastbooking.messageEvent.home.HotelListMessageEvent;
import com.iplay.feastbooking.messageEvent.home.HotelListNoInternetMessageEvent;
import com.iplay.feastbooking.messageEvent.home.RecommendGridMessageEvent;
import com.iplay.feastbooking.net.NetProperties;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2017/10/1.
 */

public class RecommendHotelListUtility {

    private static volatile RecommendHotelListUtility utility;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String advertisementAPI;

    private final String recommendGridAPI;

    private final String resourcePath;

    private final String basicPath;

    private final String listHotelsForUserAPI;

    private final String hotelListUrl;

    private RecommendHotelListUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        advertisementAPI = properties.getProperty("advertisement");
        recommendGridAPI = properties.getProperty("recommendations");
        listHotelsForUserAPI = properties.getProperty("listHotelsForUser");
        urlSeperator = properties.getProperty("urlSeperator");
        resourcePath = properties.getProperty("resource");
        basicPath = serverUrl + urlSeperator + resourcePath + urlSeperator;
        hotelListUrl = serverUrl + urlSeperator + listHotelsForUserAPI;
    }

    public void asyncInit(Context context, HotelListRequireConfig config){
        asyncInitAdvertisements(context);
        asyncInitSpecialRecommend(context);
        asyncInitAllHotel(context, config);
    }

    private void asyncInitAdvertisements(Context context){
        if(!NetProperties.isNetworkConnected(context)){
            List<Advertisement> advertisements = Advertisement.transFromDOs(getAdvertisementsWithoutNetwork(), serverUrl + urlSeperator + resourcePath , urlSeperator);
            AdvertisementMessageEvent event = new AdvertisementMessageEvent();
            event.setAdvertisements(advertisements);
            EventBus.getDefault().post(event);
        }else{
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url(serverUrl + urlSeperator + advertisementAPI).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    List<Advertisement> advertisements = Advertisement.transFromDOs(getAdvertisementsWithoutNetwork(), serverUrl + urlSeperator + resourcePath , urlSeperator);
                    AdvertisementMessageEvent event = new AdvertisementMessageEvent();
                    event.setAdvertisements(advertisements);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        List<Advertisement> advertisements = Advertisement.transFromDOs(getAdvertisementsWithoutNetwork(), serverUrl + urlSeperator + resourcePath , urlSeperator);
                        AdvertisementMessageEvent event = new AdvertisementMessageEvent();
                        event.setAdvertisements(advertisements);
                        EventBus.getDefault().post(event);
                    }else {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<AdvertisementGO>>(){}.getType();
                        List<AdvertisementGO> gos =  gson.fromJson(response.body().string(),type);
                        List<Advertisement> advertisements = Advertisement.transFromGOs(gos);

                        DataSupport.deleteAll(AdvertisementDto.class);
                        for(int i=0; i<gos.size(); i++){
                            AdvertisementDto dao = AdvertisementDto.transFromGO(gos.get(i),basicPath);
                            dao.save();
                        }
                        AdvertisementMessageEvent event = new AdvertisementMessageEvent();
                        event.setAdvertisements(advertisements);
                        EventBus.getDefault().post(event);
                    }
                }
            });
        }
    }

    private void asyncInitSpecialRecommend(Context context){
        if(!NetProperties.isNetworkConnected(context)){
            List<RecommendGrid> recommendGrids = RecommendGrid.transFromDOs(getRecommendGridsWithoutNetWork(),serverUrl + urlSeperator + resourcePath , urlSeperator);
            RecommendGridMessageEvent event = new RecommendGridMessageEvent();
            event.setRecommendGrids(recommendGrids);
            EventBus.getDefault().post(event);
        }else {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            final Request request = new Request.Builder().url(serverUrl + urlSeperator + recommendGridAPI).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    List<RecommendGrid> recommendGrids = RecommendGrid.transFromDOs(getRecommendGridsWithoutNetWork(),serverUrl + urlSeperator + resourcePath , urlSeperator);
                    RecommendGridMessageEvent event = new RecommendGridMessageEvent();
                    event.setRecommendGrids(recommendGrids);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        List<RecommendGrid> recommendGrids = RecommendGrid.transFromDOs(getRecommendGridsWithoutNetWork(),serverUrl + urlSeperator + resourcePath , urlSeperator);
                        RecommendGridMessageEvent event = new RecommendGridMessageEvent();
                        event.setRecommendGrids(recommendGrids);
                        EventBus.getDefault().post(event);
                    }else {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<RecommendGridGO>>(){}.getType();
                        List<RecommendGridGO> gos =  gson.fromJson(response.body().string(),type);
                        List<RecommendGrid> recommendGrids = RecommendGrid.transFromGOs(gos);
                        DataSupport.deleteAll(RecommendGridDto.class);
                        for(int i=0; i<gos.size(); i++){
                            RecommendGridDto dao = RecommendGridDto.transFromGO(gos.get(i),basicPath);
                            dao.save();
                        }
                        RecommendGridMessageEvent event = new RecommendGridMessageEvent();
                        event.setRecommendGrids(recommendGrids);
                        EventBus.getDefault().post(event);
                    }
                }
            });
        }
    }

    private void asyncInitAllHotel(Context context, HotelListRequireConfig config){
        load(context, true, config);
    }

    public void load(final Context context, final boolean isInit, HotelListRequireConfig config){
        if(!NetProperties.isNetworkConnected(context)){
            HotelListMessageEvent event = new HotelListMessageEvent();
            event.setInit(isInit);
            event.setStatus(HotelListMessageEvent.Status.FAILURE);
            event.setFailureReason(context.getResources().getString(R.string.failure_reason_no_internet));
            EventBus.getDefault().post(event);
        }else{
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
            String requireUrl = hotelListUrl + config.getQueryString();
            Request request = new Request.Builder().url(requireUrl).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    HotelListMessageEvent event = new HotelListMessageEvent();
                    event.setInit(isInit);
                    event.setStatus(HotelListMessageEvent.Status.FAILURE);
                    event.setFailureReason(context.getResources().getString(R.string.failure_reason_no_internet));
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    HotelListMessageEvent event = new HotelListMessageEvent();
                    event.setInit(isInit);
                    if(!response.isSuccessful()){
                        event.setStatus(HotelListMessageEvent.Status.FAILURE);
                        event.setFailureReason(context.getResources().getString(R.string.failure_reason_unknown_reason));
                    }else {
                        event.setStatus(HotelListMessageEvent.Status.SUCCESS);
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<RecommendHotelGO>>(){}.getType();
                        List<RecommendHotelGO> gos =  gson.fromJson(response.body().string(),type);
                        event.setHotels(gos);
                    }
                }
            });
        }
    }

    private List<AdvertisementDto> getAdvertisementsWithoutNetwork(){
        List<AdvertisementDto> advertisementDtos = DataSupport.findAll(AdvertisementDto.class);
        return advertisementDtos;
    }

    private List<RecommendGridDto> getRecommendGridsWithoutNetWork(){
        List<RecommendGridDto> recommendGridDtos = DataSupport.findAll(RecommendGridDto.class);
        return recommendGridDtos;
    }

    public static RecommendHotelListUtility getInstance(Context context){
        if(utility == null){
            synchronized (RecommendHotelListUtility.class){
                if(utility == null) {
                    utility = new RecommendHotelListUtility(context);
                }
            }
        }
        return utility;
    }
}
