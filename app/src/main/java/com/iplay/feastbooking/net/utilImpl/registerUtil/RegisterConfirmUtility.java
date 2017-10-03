package com.iplay.feastbooking.net.utilImpl.registerUtil;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.gson.register.RegisterConfirmRequest;
import com.iplay.feastbooking.gson.register.RegisterConfirmResponse;
import com.iplay.feastbooking.net.message.UtilMessage;
import com.iplay.feastbooking.ui.home.HomeActivity;
import com.iplay.feastbooking.ui.login.RegisterConfirmActivity;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2017/9/29.
 */

public class RegisterConfirmUtility {

    private static volatile RegisterConfirmUtility utility;

    private final Properties properties;

    private final String serverUrl;

    private final String urlSeperator;

    private final String registerAPI;

    private final String tokenPrefix;

    private final Context mContext;

    private RegisterConfirmUtility(Context context){
        properties = ProperTies.getProperties(context);
        serverUrl = properties.getProperty("serverUrl");
        urlSeperator = properties.getProperty("urlSeperator");
        registerAPI = properties.getProperty("register");
        tokenPrefix = properties.getProperty("tokenPrefix");
        mContext = context;
    }

    public void register(final RegisterConfirmRequest request, String token, final String mail){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        final Gson gson = new Gson();
        String json = gson.toJson(request);
        RequestBody body = RequestBody.create(UtilMessage.JSON, json);
        final Request req = new Request.Builder().url(serverUrl + urlSeperator + registerAPI).header("Authorization",tokenPrefix + " " + token)
                .post(body).build();
        final RegisterConfirmActivity activity = (RegisterConfirmActivity) mContext;
        client.newCall(req).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                if(e.getCause().equals(SocketTimeoutException.class)){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "连接超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"未知错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                activity.setNext_btn_state(ProperTies.TYPE_BTN_ACTIVE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RegisterConfirmResponse confirmResponse = gson.fromJson(response.body().string(),RegisterConfirmResponse.class);
                if(!confirmResponse.success){
                    final String reason = (String) confirmResponse.data;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, reason, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    LinkedTreeMap map = (LinkedTreeMap) confirmResponse.data;
                    String userToken = (String) map.get("token");
                    int userId =  ((Double)map.get("userId")).intValue();
                    UserDao userDao = new UserDao();
                    userDao.setUserId(userId);
                    userDao.setToken(userToken);
                    userDao.setUsername(request.username);
                    userDao.setPassword(request.password);
                    userDao.setEmail(mail);
                    userDao.setLogin(true);
                    userDao.save();
                    ContentValues values = new ContentValues();
                    values.put("isLogin",false);
                    DataSupport.updateAll(UserDao.class, values, "userId != ?", userId + "");
                    HomeActivity.startActivity(mContext);
                }
                activity.setNext_btn_state(ProperTies.TYPE_BTN_ACTIVE);
            }
        });
    }

    public static RegisterConfirmUtility getInstance(Context context){
        if(utility == null){
            synchronized (RegisterConfirmUtility.class){
                utility = new RegisterConfirmUtility(context);
            }
        }
        return utility;
    }

}
