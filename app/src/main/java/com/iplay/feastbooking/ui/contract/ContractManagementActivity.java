package com.iplay.feastbooking.ui.contract;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.component.view.gridview.UnScrollableGridView;
import com.iplay.feastbooking.entity.IdentityMatrix;
import com.iplay.feastbooking.messageEvent.contract.PhotoUpdateMessageEvent;
import com.iplay.feastbooking.net.utilImpl.contract.ContractUtility;
import com.iplay.feastbooking.ui.contract.adapter.PhotoGridViewAdapter;
import com.iplay.feastbooking.ui.contract.data.ContractPhotoPath;
import com.iplay.feastbooking.ui.order.OrderDetailActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by Guyuhan on 2017/11/7.
 */

public class ContractManagementActivity extends BasicActivity implements View.OnClickListener{

    private final String TAG = "ContractManagement";

    private static final String order_id_key = "order_id_key";

    private static final String contract_list_key = "contract_list_key";

    private static final String im_key = "im_key";

    private int orderId;

    private List<ContractPhotoPath> contractPhotoPath;

    private IdentityMatrix identityMatrix;

    private UnScrollableGridView photo_gv;

    private PhotoGridViewAdapter adapter;

    private LinearLayout bottom_sheet_ll;

    private TextView submit_tv;

    private BottomSheetBehavior bottomSheetBehavior;

    private int photoMaxSize = 10;

    private ContractUtility utility;

    private RelativeLayout loading_rl;

    private LinearLayout content_ll;

    private TextView load_tv;

    private TextView back_tv;

    private ProgressBar refresh_progress_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
        utility = ContractUtility.getInstance(this);
    }

    public static void start(Context context, int orderId, ArrayList<String> contractPhotoUrls, IdentityMatrix identityMatrix){
        Intent intent = new Intent(context, ContractManagementActivity.class);
        intent.putExtra(order_id_key, orderId);
        intent.putStringArrayListExtra(contract_list_key, contractPhotoUrls);
        intent.putExtra(im_key, identityMatrix);
        context.startActivity(intent);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.contract_management_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        findViewById(R.id.cancel_choose_tv).setOnClickListener(this);
        findViewById(R.id.choose_from_album_tv).setOnClickListener(this);
        findViewById(R.id.submit_tv).setOnClickListener(this);
        photo_gv = (UnScrollableGridView) findViewById(R.id.photo_gv);
        adapter = new PhotoGridViewAdapter(this, R.layout.photo_grid, contractPhotoPath, identityMatrix);
        photo_gv.setAdapter(adapter);
        bottom_sheet_ll = (LinearLayout) findViewById(R.id.photo_bottom_sheet);
        load_tv = (TextView) findViewById(R.id.load_tv);
        back_tv = (TextView) findViewById(R.id.back_tv);
        back_tv.setOnClickListener(this);
        loading_rl = (RelativeLayout) findViewById(R.id.loading_rl);
        refresh_progress_bar = (ProgressBar) findViewById(R.id.refresh_progress_bar);
        content_ll = (LinearLayout) findViewById(R.id.content_ll);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_ll);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setHideable(true);
        submit_tv = (TextView) findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        orderId = intent.getIntExtra(order_id_key, -1);
        contractPhotoPath = new ArrayList<>();
        List<String> photoUrls = getIntent().getStringArrayListExtra(contract_list_key);
        contractPhotoPath = new ArrayList<>();
        if(photoUrls != null){
            for(int i=0; i<photoUrls.size(); i++){
                ContractPhotoPath path = new ContractPhotoPath(ContractPhotoPath.TYPE_FROM_INTERNET, photoUrls.get(i));
                contractPhotoPath.add(path);
            }
        }
        identityMatrix = (IdentityMatrix) intent.getSerializableExtra(im_key);
        if(identityMatrix != null){
            if(identityMatrix.isCustomer()) {
                contractPhotoPath.add(null);
            }
        }
    }

    @Override
    public void showContent() {
        if(identityMatrix == null ||
                (identityMatrix != null && !identityMatrix.isCustomer())){
            submit_tv.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoUpdateMessageEvent(final PhotoUpdateMessageEvent event){
        if(event.type == PhotoUpdateMessageEvent.TYPE.TYPE_FAILURE){
            cancelLoading("上傳失敗");
        }else if(event.type == PhotoUpdateMessageEvent.TYPE.TYPE_SUCCESS){
            cancelLoading("操作成功");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    deleteCropImage(event.getFilesToDelete());
                }
            }).start();
        }
    }

    private void deleteCropImage(List<File> files){
        if(files != null){
            for (int i=0; i<files.size(); i++){
                File file = files.get(i);
                if(file.exists()){
                    file.delete();
                }
            }
        }
    }

    private void showLoading(){
        content_ll.setVisibility(View.INVISIBLE);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_tv:
                OrderDetailActivity.reload(this, orderId);
                break;
            case R.id.back_iv:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
                break;
            case R.id.add_photo_iv:
                showBottomSheet();
                break;
            case R.id.cancel_choose_tv:
                hideBottomSheet();
                break;
            case R.id.choose_from_album_tv:
                int remain = photoMaxSize + 1 - (contractPhotoPath == null? 0 : contractPhotoPath.size());
                if(remain <= 0){
                    Toast.makeText(this,"最多選擇10張照片",Toast.LENGTH_SHORT).show();
                    return;
                }
                PhotoPicker.builder()
                        .setPhotoCount(remain)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(this);
                break;
            case R.id.submit_tv:
                hideBottomSheet();
                if(!adapter.isModified()){
                    Toast.makeText(this, "尚未修改", Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            adapter.upLoadFileList(orderId, utility);
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cancelLoading("上傳失敗");
                                }
                            });
                        }
                    }
                }).start();
                break;
            default:
                break;
        }
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
                    if(photos != null) {
                        for (int i = 0; i < photos.size(); i++) {
                            ContractPhotoPath path = new ContractPhotoPath(ContractPhotoPath.TYPE_FROM_DISK, photos.get(i));
                            adapter.addPhoto(path);
                        }
                    }
                    hideBottomSheet();
                    break;
            }
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
