package com.iplay.feastbooking.net.utilImpl.contract;

import android.content.Context;
import android.util.Log;

import com.iplay.feastbooking.assistance.ImageCompressUtility;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.messageEvent.contract.PhotoUpdateMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.consult.ConsultUtility;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Guyuhan on 2017/11/12.
 */

public class ContractUtility {
    private static volatile ContractUtility instance;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String tokenPrefix;

    private final String uploadContractSuffix;

    private final String uploadPaymentSuffix;

    private final String postOrderAPI;

    private final int connectSeconds;

    private final String resource;

    private ContractUtility(Context context){
        properties = ProperTies.getProperties(context);
        urlSeperator = properties.getProperty("urlSeperator");
        serverUrl = properties.getProperty("serverUrl");
        tokenPrefix = properties.getProperty("tokenPrefix");
        postOrderAPI = properties.getProperty("postOrder");
        uploadContractSuffix = properties.getProperty("uploadContractSuffix");
        resource = properties.getProperty("resource");
        uploadPaymentSuffix = properties.getProperty("uploadPaymentSuffix");
        connectSeconds = 120;
    }

    public  String getImageResourcePath(){
        return serverUrl + urlSeperator + resource + urlSeperator;
    }

    public static ContractUtility getInstance(Context context){
        if(instance == null){
            synchronized (ConsultUtility.class){
                if(instance == null){
                    instance = new ContractUtility(context);
                }
            }
        }
        return instance;
    }

    private List<File> generateFilesToDelete(List<File> files){
        List<File> filesToDelete = new ArrayList<>();
        if(files != null){
            for(int i=0; i<files.size(); i++){
                File file = files.get(i);
                if(file.getName().contains(ImageCompressUtility.CROP_PREFIX)){
                    filesToDelete.add(file);
                }
            }
        }
        return filesToDelete;
    };

    public void upLoadPayment(int orderId, String filesToDelete, final List<File> files, Context context, double payment){
        if(!NetProperties.isNetworkConnected(context)){
            PhotoUpdateMessageEvent event = new PhotoUpdateMessageEvent(PhotoUpdateMessageEvent.TYPE.TYPE_FAILURE, "網絡不給力");
            EventBus.getDefault().post(event);
        }else {
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(connectSeconds, TimeUnit.SECONDS)
                    .readTimeout(connectSeconds, TimeUnit.SECONDS)
                    .writeTimeout(connectSeconds, TimeUnit.SECONDS)
                    .build();
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            Log.d("deleted", filesToDelete);
            builder.addFormDataPart("deleted", filesToDelete);
            builder.addFormDataPart("amountPaid", payment + "");
            if(files != null){
                for(int i=0; i<files.size(); i++){
                    File file;
                    if((file = files.get(i)) != null){
                        RequestBody fileBody = RequestBody.create(null, file);
                        builder.addFormDataPart("files", "picture" + i +".jpg", fileBody);
                    }
                }
            }
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .header("Authorization", token)
                    .url(serverUrl + urlSeperator +  postOrderAPI + urlSeperator
                            + orderId + urlSeperator + uploadPaymentSuffix)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("upload", "over time" + e);
                    PhotoUpdateMessageEvent event = new PhotoUpdateMessageEvent(PhotoUpdateMessageEvent.TYPE.TYPE_FAILURE, "網絡不給力");
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    PhotoUpdateMessageEvent event;
                    if(!response.isSuccessful()){
                        Log.d("upload", response.body().string());
                        event = new PhotoUpdateMessageEvent(PhotoUpdateMessageEvent.TYPE.TYPE_FAILURE, "未知錯誤");
                    }else {
                        event = new PhotoUpdateMessageEvent(PhotoUpdateMessageEvent.TYPE.TYPE_SUCCESS);
                        event.setFilesToDelete(generateFilesToDelete(files));
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }

    public void upLoadContracts(int orderId, String filesToDelete, final List<File> files, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            PhotoUpdateMessageEvent event = new PhotoUpdateMessageEvent(PhotoUpdateMessageEvent.TYPE.TYPE_FAILURE, "網絡不給力");
            EventBus.getDefault().post(event);
        }else {
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            token = tokenPrefix + " " +  token;
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(connectSeconds, TimeUnit.SECONDS)
                    .readTimeout(connectSeconds, TimeUnit.SECONDS)
                    .writeTimeout(connectSeconds, TimeUnit.SECONDS)
                    .build();
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("deleted", filesToDelete);
            if(files != null){
                for(int i=0; i<files.size(); i++){
                    File file;
                    if((file = files.get(i)) != null){
                        RequestBody fileBody = RequestBody.create(null, file);
                        builder.addFormDataPart("files", "picture" + i +".jpg", fileBody);
                    }
                }
            }
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .header("Authorization", token)
                    .url(serverUrl + urlSeperator +  postOrderAPI + urlSeperator
                            + orderId + urlSeperator + uploadContractSuffix)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("upload", "over time" + e);
                    PhotoUpdateMessageEvent event = new PhotoUpdateMessageEvent(PhotoUpdateMessageEvent.TYPE.TYPE_FAILURE, "網絡不給力");
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    PhotoUpdateMessageEvent event;
                    if(!response.isSuccessful()){
                        Log.d("upload", response.body().string());
                        event = new PhotoUpdateMessageEvent(PhotoUpdateMessageEvent.TYPE.TYPE_FAILURE, "未知錯誤");
                    }else {
                        event = new PhotoUpdateMessageEvent(PhotoUpdateMessageEvent.TYPE.TYPE_SUCCESS);
                        event.setFilesToDelete(generateFilesToDelete(files));
                    }
                    EventBus.getDefault().post(event);
                }
            });
        }
    }
}
