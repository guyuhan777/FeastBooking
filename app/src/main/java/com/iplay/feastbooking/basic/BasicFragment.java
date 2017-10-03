package com.iplay.feastbooking.basic;

import android.app.Fragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by admin on 2017/10/2.
 */

public class BasicFragment extends Fragment {

    protected boolean isRegisteredNeed = false;

    @Override
    public void onStart() {
        super.onStart();
        if(isRegisteredNeed) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isRegisteredNeed){
            EventBus.getDefault().unregister(this);
        }
    }
}
