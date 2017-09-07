package com.iplay.feastbooking.ui.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.ui.recommendedHotel.RecommendedHotelFragment;
import com.iplay.feastbooking.ui.self.SelfFragment;

/**
 * Created by admin on 2017/7/14.
 */

public class HomeActivity extends BasicActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener {

    private static final String TAG = "HomeActivity";

    public BottomNavigationBar bottom_bar;

    //private HomePageFragment homePageFragment;

    //public RecommendedHotelFragment recommendedHotelFragment;

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

    }

    @Override
    public void showContent() {

    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(fragment!=null){
            transaction.replace(R.id.home_fragment_area,fragment);
            transaction.commit();
        }
    }


    public void replaceFragment(Fragment fragment,String tag){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_area,fragment,tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        while(fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStackImmediate();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (position){
            case 0:
                RecommendedHotelFragment recommendedFragment = new RecommendedHotelFragment();
                transaction.replace(R.id.home_fragment_area,recommendedFragment,RecommendedHotelFragment.TAG);
                break;
            case 1:
                SelfFragment selfFragment = new SelfFragment();
                transaction.replace(R.id.home_fragment_area,selfFragment,SelfFragment.TAG);
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
        }
    }
}
