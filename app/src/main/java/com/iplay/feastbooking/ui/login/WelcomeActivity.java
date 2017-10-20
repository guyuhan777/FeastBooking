package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.ui.home.HomeActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by admin on 2017/10/16.
 */

public class WelcomeActivity extends BasicActivity {

    public static final String TAG = "WelcomeActivity";

    private ImageView background_iv;

    private TimerHandler handler = new TimerHandler();

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
        Glide.with(this).load(R.drawable.welcome).into(background_iv);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {
        handler.sendEmptyMessageDelayed(3,3000);
    }

    private class TimerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 3){
                List<UserDao> userDaos = DataSupport.where("isLogin = ?","" + 1).find(UserDao.class);
                if(userDaos.size() == 1){
                    HomeActivity.startActivity(WelcomeActivity.this);
                    overridePendingTransition(R.anim.fade,R.anim.hold);
                    WelcomeActivity.this.finish();
                }
                if(userDaos.size() == 0){
                    HomeActivity.startActivity(WelcomeActivity.this);
                    overridePendingTransition(R.anim.fade,R.anim.hold);
                    WelcomeActivity.this.finish();
                }

            }
        }
    }
}
