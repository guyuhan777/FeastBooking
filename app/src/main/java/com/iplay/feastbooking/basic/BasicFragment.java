package com.iplay.feastbooking.basic;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by admin on 2017/10/2.
 */

public class BasicFragment extends Fragment {

    protected volatile boolean isRegisteredNeed = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isRegisteredNeed) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isRegisteredNeed){
            EventBus.getDefault().unregister(this);
        }
    }
}
