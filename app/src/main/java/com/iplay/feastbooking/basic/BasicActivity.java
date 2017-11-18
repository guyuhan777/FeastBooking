package com.iplay.feastbooking.basic;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by admin on 2017/7/11.
 */

public abstract class BasicActivity extends AppCompatActivity {

    protected volatile boolean isRegistered = false;

    @Override
    protected void onStart() {
        super.onStart();
        if(isRegistered){
            EventBus.getDefault().register(this);
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setTranslucentStatus(true);
        }
        init();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void init(){
        setContentView();
        /*if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        getData();
        findViews();
        showContent();
    }

    private void setTranslucentStatus(boolean on){
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if(on){
            winParams.flags |= bits;
        }else{
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isRegistered) {
            EventBus.getDefault().unregister(this);
        }
    }

    public abstract void setContentView();

    public abstract void findViews();

    public abstract void getData();

    public abstract void showContent();
}
