package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.SecurityUtility;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.dto.UserDto;
import com.iplay.feastbooking.gson.selfInfo.SelfInfo;
import com.iplay.feastbooking.ui.home.HomeActivity;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by admin on 2017/10/16.
 */

public class WelcomeActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "WelcomeActivity";

    private volatile boolean isLogin;

    private ImageView background_iv;

    private TimerHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        handler = new TimerHandler(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.welcome_layout);
    }


    public static void start(Context context){
        context.startActivity(new Intent(context,WelcomeActivity.class));
    }

    @Override
    public void findViews() {
        background_iv = (ImageView) findViewById(R.id.welcome_bg);
        findViewById(R.id.root_view).setOnClickListener(this);
        Glide.with(this).load(R.drawable.welcome).into(background_iv);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {
        handler.sendEmptyMessageDelayed(3,3000);
    }

    @Override
    public void onClick(View v) {
        logIn();
    }

    public void logIn(){
        if(!isLogin){
            isLogin = true;
            HomeActivity.startActivity(WelcomeActivity.this);
            overridePendingTransition(R.anim.fade,R.anim.hold);
            this.finish();
        }
    }

    private static class TimerHandler extends Handler{

        private WeakReference<WelcomeActivity> activityWeakReference;

        public TimerHandler(WelcomeActivity activity){
            this.activityWeakReference = new WeakReference<WelcomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 3){
               WelcomeActivity activity = activityWeakReference.get();
                activity.logIn();
            }
        }
    }
}
