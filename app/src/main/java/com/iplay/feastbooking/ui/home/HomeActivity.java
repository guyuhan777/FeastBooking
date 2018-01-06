package com.iplay.feastbooking.ui.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.SecurityUtility;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.dto.UserDto;
import com.iplay.feastbooking.ui.login.LoginActivity;
import com.iplay.feastbooking.ui.recommendedHotel.RecommendedHotelFragment;
import com.iplay.feastbooking.ui.self.SelfFragment;

import org.litepal.crud.DataSupport;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by admin on 2017/7/14.
 */

public class HomeActivity extends BasicActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener {

    private static final String TAG = "HomeActivity";

    public BottomNavigationBar bottom_bar;

    private static final String KEY_HOME =  "KEY_HOME";

    private static final String KEY_SELF_PAGE = "KEY_SELF_PAGE";

    public enum START_TYPE{HOME, SELF}

    private RecommendedHotelFragment recommendedFragment;

    private  SelfFragment selfFragment;

    private Fragment currentFragment;

    private RelativeLayout login_hint;

    private TextView hint_cancel_tv;

    private TextView hint_log_in_tv;

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
        login_hint = (RelativeLayout) findViewById(R.id.login_hint);
        Glide.with(this).load(R.drawable.self_bg).into((ImageView) findViewById(R.id.hint_bg_iv));
        hint_cancel_tv = (TextView) findViewById(R.id.hint_cancel);
        hint_cancel_tv.setOnClickListener(this);
        hint_log_in_tv = (TextView) findViewById(R.id.click_to_log_in);
        hint_log_in_tv.setOnClickListener(this);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {
        if(LoginUserHolder.getInstance().getCurrentUser() != null){
            login_hint.setVisibility(View.GONE);
        }else {
            login_hint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(LoginUserHolder.getInstance().getCurrentUser() != null){
            login_hint.setVisibility(View.GONE);
        }else {
            login_hint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if(intent.getBooleanExtra(KEY_HOME, false)){
            bottom_bar.selectTab(0);
        }else if(intent.getBooleanExtra(KEY_SELF_PAGE, false)){
            bottom_bar.selectTab(1);
        }
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        UserDto currentUser = UserDao.getInstance().getLoginUser();
        if(currentUser != null){
            String md5Password = SecurityUtility.md5(currentUser.getPassword());
            JMessageClient.login(currentUser.getUsername(), md5Password, null);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startHomeActivity(Context context, START_TYPE type){
        Intent intent = new Intent(context, HomeActivity.class);
        if(type == START_TYPE.HOME) {
            intent.putExtra(KEY_HOME, true);
        }else if(type == START_TYPE.SELF){
            intent.putExtra(KEY_SELF_PAGE, true);
        }
        context.startActivity(intent);
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
                if(LoginUserHolder.getInstance().getCurrentUser() == null){
                    LoginActivity.startActivity(this);
                    overridePendingTransition(R.anim.bottom2top,R.anim.hold);
                }else {
                    if (selfFragment == null) {
                        selfFragment = new SelfFragment();
                    }
                    switchFragment(selfFragment);
                }
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
            case R.id.hint_cancel:
                login_hint.setVisibility(View.GONE);
                break;
            case R.id.click_to_log_in:
                LoginActivity.startActivity(this);
                overridePendingTransition(R.anim.bottom2top,R.anim.hold);
                break;
            default:
                break;
        }
    }
}
