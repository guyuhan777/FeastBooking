package com.iplay.feastbooking.ui.self;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.ui.home.HomeActivity;

import org.litepal.crud.DataSupport;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 2017/10/19.
 */

public class SelfInfoActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "SelfInfoActivity";

    private LinearLayout bottom_sheet_ll;

    private BottomSheetBehavior bottomSheetBehavior;

    private CircleImageView portrait_civ;

    @Override
    public void setContentView() {
        setContentView(R.layout.self_info_detail);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        findViewById(R.id.log_out_btn).setOnClickListener(this);
        RelativeLayout root_view = (RelativeLayout) findViewById(R.id.root_view);
        root_view.setOnClickListener(this);
        root_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        findViewById(R.id.cancel_choose_tv).setOnClickListener(this);
        findViewById(R.id.choose_from_album_tv).setOnClickListener(this);
        bottom_sheet_ll = (LinearLayout) findViewById(R.id.photo_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_ll);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setHideable(true);
        portrait_civ = (CircleImageView) findViewById(R.id.portrait_civ);
        portrait_civ.setOnClickListener(this);
    }

    public static void start(Context context){
        context.startActivity(new Intent(context,SelfInfoActivity.class));
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_iv:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
                break;
            case R.id.log_out_btn:
                DataSupport.deleteAll(UserDao.class,"isLogin = ?","" + 1);
                LoginUserHolder.getInstance().removeCurrentUser();
                HomeActivity.startActivity(this);
                finish();
                break;
            case R.id.portrait_civ:
                showBottomSheet();
                break;
            case R.id.cancel_choose_tv:
                hideBottomSheet();
                break;
            case R.id.root_view:
                hideBottomSheet();
                break;
            default:
                break;
        }
    }

    private void hideBottomSheet(){
        if(bottomSheetBehavior != null) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                    bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    }

    private void showBottomSheet() {
        if (bottomSheetBehavior != null) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    }
}
