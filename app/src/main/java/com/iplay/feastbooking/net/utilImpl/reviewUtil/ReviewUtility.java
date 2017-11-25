package com.iplay.feastbooking.net.utilImpl.reviewUtil;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.gson.comment.CommentVO;
import com.iplay.feastbooking.messageEvent.common.CommonMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.message.UtilMessage;
import com.iplay.feastbooking.net.utilImpl.consult.ConsultUtility;
import com.iplay.feastbooking.net.utilImpl.registerUtil.RegisterConfirmUtility;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by gu_y-pc on 2017/11/25.
 */

public class ReviewUtility {

    private static volatile ReviewUtility instance;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String tokenPrefix;

    private final String postCommentAPI;

    private final String orders;

    private ReviewUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
        tokenPrefix = properties.getProperty("tokenPrefix");
        postCommentAPI = properties.getProperty("reviews");
        orders = properties.getProperty("postOrder");
    }

    public void postComment(int orderId, @NonNull String comment, double rating, Context context){
        if(!NetProperties.isNetworkConnected(context)){
            CommonMessageEvent<Integer> messageEvent = new CommonMessageEvent<>(CommonMessageEvent.TYPE.TYPE_FAILURE);
            messageEvent.setFailureResult("網絡不給力");
            EventBus.getDefault().post(messageEvent);
        }else {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
            String token = LoginUserHolder.getInstance().getCurrentUser().getToken();
            if(token == null || token.equals("")){
                return;
            }
            CommentVO vo = new CommentVO(rating, comment);
            final Gson gson = new Gson();
            String json = gson.toJson(vo);
            RequestBody body = RequestBody.create(UtilMessage.JSON, json);
            token = tokenPrefix + " " +  token;
            Request request = new Request.Builder()
                    .url(serverUrl + urlSeperator
                            + orders + urlSeperator
                            + postCommentAPI)
                    .post(body)
                    .header("Authorization", token).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    CommonMessageEvent<Integer> messageEvent = new CommonMessageEvent<>(CommonMessageEvent.TYPE.TYPE_FAILURE);
                    messageEvent.setFailureResult("網絡不給力");
                    EventBus.getDefault().post(messageEvent);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    CommonMessageEvent<Integer> messageEvent = new CommonMessageEvent<>();
                    if(!response.isSuccessful()){
                        int statusCode = response.code();
                        messageEvent.setType(CommonMessageEvent.TYPE.TYPE_FAILURE);
                        if(statusCode == 403){
                            messageEvent.setFailureResult("您以評論過此訂單");
                        }else if(statusCode == 404){
                            messageEvent.setFailureResult("訂單不存在或您無權限");
                        }else {
                            messageEvent.setFailureResult("未知錯誤");
                        }
                    }else {
                        messageEvent.setType(CommonMessageEvent.TYPE.TYPE_SUCCESS);
                        ResponseBody responseBody = response.body();
                        int id = responseBody == null ? -1 : Integer.parseInt(responseBody.string());
                        messageEvent.setSuccessResult(id);
                        responseBody.close();
                    }
                    EventBus.getDefault().post(messageEvent);
                }
            });
        }
    }
    public static ReviewUtility getInstance(Context context){
        if(instance == null){
            synchronized (ReviewUtility.class){
                if(instance == null){
                    instance = new ReviewUtility(context);
                }
            }
        }
        return instance;
    }


}
