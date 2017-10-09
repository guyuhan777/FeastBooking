package com.iplay.feastbooking.ui.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.messageEvent.activityFinish.ActivityFinishMessageEvent;
import com.iplay.feastbooking.ui.login.LoginActivity;
import com.iplay.feastbooking.ui.login.RegisterActivity;
import com.iplay.feastbooking.ui.login.RegisterConfirmActivity;
import com.iplay.feastbooking.ui.recommendedHotel.RecommendedHotelFragment;
import com.iplay.feastbooking.ui.self.SelfFragment;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class HomeActivity extends BasicActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener {

    private static final String TAG = "HomeActivity";

    public static final String KEY_USER = "user_key";

    public BottomNavigationBar bottom_bar;

    private RecommendedHotelFragment recommendedFragment;

    private  SelfFragment selfFragment;

    private Fragment currentFragment;

    public void switchFragment(Fragment targetFragment){
        if(currentFragment == targetFragment){
            return;
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(currentFragment != null){
            transaction.hide(currentFragment);
        }
        if(!targetFragment.isAdded()){
            transaction.add(R.id.home_fragment_area, targetFragment);
        }else {
            transaction.show(targetFragment);
        }
        transaction.commit();
        currentFragment = targetFragment;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.home_layout);
    }

    @Override
    public void findViews() {
        bottom_bar = (BottomNavigationBar) findViewById(R.id.home_bnb);
        bottom_bar.setMode(BottomNavigationBar.MODE_FIXED);
        bottom_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottom_bar.setActiveColor(R.color.app_title_color);
        bottom_bar.addItem(new BottomNavigationItem(R.drawable.wine_glass,"酒店"))
                .addItem(new BottomNavigationItem(R.drawable.me,"我"))
                .setFirstSelectedPosition(0).initialise();
        bottom_bar.setTabSelectedListener(this);
        bottom_bar.selectTab(0);
    }

    @Override
    public void getData() {
        List<UserDao> userDaos = DataSupport.where("isLogin = ?","" + 1).find(UserDao.class);
        if(userDaos.size() == 1){
            Log.d("currentUser",userDaos.get(0).toString());
        }
    }

    @Override
    public void showContent() {

    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        if(context instanceof LoginActivity){
            ActivityFinishMessageEvent event = new ActivityFinishMessageEvent();
            event.put(LoginActivity.TAG);
            EventBus.getDefault().post(event);
        }else if(context instanceof RegisterConfirmActivity){
            ActivityFinishMessageEvent event = new ActivityFinishMessageEvent();
            event.put(RegisterConfirmActivity.TAG);
            event.put(RegisterActivity.TAG);
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (position){
            case 0:
                if(recommendedFragment == null){
                    recommendedFragment = new RecommendedHotelFragment();
                }
                switchFragment(recommendedFragment);
                break;
            case 1:
                if (selfFragment == null){
                    selfFragment = new SelfFragment();
                }
                switchFragment(selfFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // todo
        }
    }
}
