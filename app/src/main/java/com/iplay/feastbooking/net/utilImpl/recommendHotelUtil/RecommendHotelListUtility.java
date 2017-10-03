package com.iplay.feastbooking.net.utilImpl.recommendHotelUtil;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.dao.AdvertisementDao;
import com.iplay.feastbooking.dao.RecommendGridDao;
import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.entity.RecommendGrid;
import com.iplay.feastbooking.gson.RecommendGridGO;
import com.iplay.feastbooking.gson.advertisement.AdvertisementGO;
import com.iplay.feastbooking.messageEvent.AdvertisementMessageEvent;
import com.iplay.feastbooking.messageEvent.RecommendGridMessageEvent;
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

    private Context mContext;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String advertisementAPI;

    private final String recommendGridAPI;

    private final String resourcePath;

    private final String basicPath;

    private RecommendHotelListUtility(Context context){
        mContext = context;
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        advertisementAPI = properties.getProperty("advertisement");
        recommendGridAPI = properties.getProperty("recommendations");
        urlSeperator = properties.getProperty("urlSeperator");
        resourcePath = properties.getProperty("resource");
        basicPath = serverUrl + urlSeperator + resourcePath + urlSeperator;
    }

    public void asyncInit(){
        asyncInitAdvertisements();
        asyncInitSpecialRecommend();
        asyncInitAllHotel();
    }

    private void asyncInitAdvertisements(){
        if(!NetProperties.isNetworkConnected(mContext)){
            List<Advertisement> advertisements = Advertisement.transFromDOs(getAdvertisementsWithoutNetwork(), serverUrl + urlSeperator + resourcePath , urlSeperator);
            AdvertisementMessageEvent event = new AdvertisementMessageEvent();
            event.setAdvertisements(advertisements);
            EventBus.getDefault().post(event);
        }else{
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
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

                        DataSupport.deleteAll(AdvertisementDao.class);
                        for(int i=0; i<gos.size(); i++){
                            AdvertisementDao dao = AdvertisementDao.transFromGO(gos.get(i),basicPath);
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

    private void asyncInitSpecialRecommend(){
        if(!NetProperties.isNetworkConnected(mContext)){
            List<RecommendGrid> recommendGrids = RecommendGrid.transFromDOs(getRecommendGridsWithoutNetWork(),serverUrl + urlSeperator + resourcePath , urlSeperator);
            RecommendGridMessageEvent event = new RecommendGridMessageEvent();
            event.setRecommendGrids(recommendGrids);
            EventBus.getDefault().post(event);
        }else {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
            final Request request = new Request.Builder().url(serverUrl + urlSeperator + recommendGridAPI).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    List<RecommendGrid> recommendGrids = RecommendGrid.transFromDOs(getRecommendGridsWithoutNetWork(),serverUrl + urlSeperator + resourcePath , urlSeperator);
                    Log.d("failure","");
                    RecommendGridMessageEvent event = new RecommendGridMessageEvent();
                    event.setRecommendGrids(recommendGrids);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        List<RecommendGrid> recommendGrids = RecommendGrid.transFromDOs(getRecommendGridsWithoutNetWork(),serverUrl + urlSeperator + resourcePath , urlSeperator);
                        Log.d("not success","");
                        RecommendGridMessageEvent event = new RecommendGridMessageEvent();
                        event.setRecommendGrids(recommendGrids);
                        EventBus.getDefault().post(event);
                    }else {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<RecommendGridGO>>(){}.getType();
                        List<RecommendGridGO> gos =  gson.fromJson(response.body().string(),type);
                        List<RecommendGrid> recommendGrids = RecommendGrid.transFromGOs(gos);

                        //Log.d("success", recommendGrids.toString());

                        DataSupport.deleteAll(RecommendGridDao.class);
                        for(int i=0; i<gos.size(); i++){
                            RecommendGridDao dao = RecommendGridDao.transFromGO(gos.get(i),basicPath);
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

    private void asyncInitAllHotel(){
        if(!NetProperties.isNetworkConnected(mContext)){
            //Todo
        }else {
            //Todo
        }
    }

    private List<AdvertisementDao> getAdvertisementsWithoutNetwork(){
        List<AdvertisementDao> advertisementDaos = DataSupport.findAll(AdvertisementDao.class);
        return advertisementDaos;
    }

    private List<RecommendGridDao> getRecommendGridsWithoutNetWork(){
        List<RecommendGridDao> recommendGridDaos = DataSupport.findAll(RecommendGridDao.class);
        return recommendGridDaos;
    }

    public static RecommendHotelListUtility getInstance(Context context){
        if(utility == null){
            synchronized (RecommendHotelListUtility.class){
                utility = new RecommendHotelListUtility(context);
            }
        }
        return utility;
    }
}
