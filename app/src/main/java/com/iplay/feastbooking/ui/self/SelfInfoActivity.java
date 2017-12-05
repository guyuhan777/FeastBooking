package com.iplay.feastbooking.ui.self;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.ImageCompressUtility;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.messageEvent.common.CommonMessageEvent;
import com.iplay.feastbooking.messageEvent.contract.PhotoUpdateMessageEvent;
import com.iplay.feastbooking.net.utilImpl.reviewUtil.ReviewUtility;
import com.iplay.feastbooking.net.utilImpl.selfDetail.ChangeSelfInfoUtility;
import com.iplay.feastbooking.ui.contract.data.PhotoPath;
import com.iplay.feastbooking.ui.home.HomeActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by admin on 2017/10/19.
 */

public class SelfInfoActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "SelfInfoActivity";

    private LinearLayout bottom_sheet_ll;

    private BottomSheetBehavior bottomSheetBehavior;

    private CircleImageView portrait_civ;

    private ScrollView self_info_sv;

    private RelativeLayout loading_rl;

    private TextView load_tv;

    private ProgressBar refresh_progress_bar;

    private TextView back_tv;

    private RelativeLayout root_view;

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
        self_info_sv = (ScrollView) findViewById(R.id.self_info_sv);
        self_info_sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideBottomSheet();
                return false;
            }
        });
        root_view = (RelativeLayout) findViewById(R.id.root_view);
        root_view.setOnClickListener(this);
        findViewById(R.id.cancel_choose_tv).setOnClickListener(this);
        findViewById(R.id.choose_from_album_tv).setOnClickListener(this);
        bottom_sheet_ll = (LinearLayout) findViewById(R.id.photo_bottom_sheet);
        bottom_sheet_ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_ll);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setHideable(true);
        portrait_civ = (CircleImageView) findViewById(R.id.portrait_civ);
        portrait_civ.setOnClickListener(this);

        load_tv = (TextView) findViewById(R.id.load_tv);
        back_tv = (TextView) findViewById(R.id.back_tv);
        back_tv.setOnClickListener(this);
        loading_rl = (RelativeLayout) findViewById(R.id.loading_rl);
        refresh_progress_bar = (ProgressBar) findViewById(R.id.refresh_progress_bar);
    }

    public static void start(Context context){
        context.startActivity(new Intent(context,SelfInfoActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PhotoPicker.REQUEST_CODE :
                    List<String> photos = null;
                    if (data != null) {
                        photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    }
                    if(photos != null && photos.size() == 1) {
                        final String path = photos.get(0);
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                File file = null;
                                try {
                                    file = ImageCompressUtility.getCompressedImageFile(SelfInfoActivity.this, path);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if(file != null){
                                    ChangeSelfInfoUtility.getInstance(SelfInfoActivity.this).upLoadPortrait(file, SelfInfoActivity.this);
                                }
                            }
                        }).start();
                    }
                    hideBottomSheet();
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonMessageEvent(final CommonMessageEvent<File> event){
        if(event.getType() == CommonMessageEvent.TYPE.TYPE_FAILURE){
            cancelLoading("上傳失敗");
        }else if(event.getType() == CommonMessageEvent.TYPE.TYPE_SUCCESS){
            cancelLoading("操作成功");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageCompressUtility.deleteCropImage(event.getSuccessResult());
            }
        }).start();
    }

    private void showLoading(){
        root_view.setVisibility(View.INVISIBLE);
        loading_rl.setVisibility(View.VISIBLE);
        back_tv.setVisibility(View.INVISIBLE);
        load_tv.setText("上傳中");
        refresh_progress_bar.setIndeterminate(true);
    }

    private void cancelLoading(String result){
        refresh_progress_bar.setIndeterminate(false);
        refresh_progress_bar.setVisibility(View.INVISIBLE);
        load_tv.setText(result);
        back_tv.setVisibility(View.VISIBLE);
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
            case R.id.back_tv:
                break;
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
            case R.id.choose_from_album_tv:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(this);
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
            case R.id.self_info_sv:
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
