package com.iplay.feastbooking.ui.hotelRoom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.component.view.gridview.UnScrollableGridView;
import com.iplay.feastbooking.gson.hotelDetail.BanquetHall;
import com.iplay.feastbooking.gson.hotelDetail.HotelDetail;
import com.iplay.feastbooking.gson.room.BanquetHallDetail;
import com.iplay.feastbooking.messageEvent.banquetHall.BanquetHallMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.banquetHall.BanquetHallUtility;
import com.iplay.feastbooking.ui.consult.ConsultActivity;
import com.iplay.feastbooking.ui.hotelRoom.adapter.RoomConfigAdapter;
import com.iplay.feastbooking.ui.login.LoginActivity;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by admin on 2017/9/21.
 */

public class HotelRoomActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "HotelRoomActivity";

    private boolean isNetOn;

    private BanquetHall banquetHall;

    private BanquetHallDetail detail;

    private ImageView back_iv;

    public static final String KEY_ROOM = "key_room";

    private View status_bar_fix_tile;

    private BanquetHallUtility utility;

    private ScrollView room_sv;

    private TextView room_name, area, height, lizhu, roomShape, tableClothColor, welcomeArea, tableAvailable, minCost;

    private ImageView single_icon;

    private RollPagerView icons_pager;

    private UnScrollableGridView config_gv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        if(!NetProperties.isNetworkConnected(this)){
            setContentView(R.layout.hotel_no_internet);
        }else {
            isNetOn = true;
            setContentView(R.layout.new_room_detail);
        }
    }

    @Override
    public void findViews() {
        status_bar_fix_tile = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix_tile.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        back_iv = (ImageView) findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
        if(isNetOn){
            findViewById(R.id.consult_btn).setOnClickListener(this);
            room_sv = (ScrollView) findViewById(R.id.room_sv);
            room_name = (TextView) findViewById(R.id.room_name);
            area = (TextView) findViewById(R.id.area);
            height = (TextView) findViewById(R.id.height);
            lizhu = (TextView) findViewById(R.id.li_zhu_qing_kuang);
            roomShape = (TextView) findViewById(R.id.da_ting_xing_zhuang);
            welcomeArea = (TextView) findViewById(R.id.ying_bing_qu_u);
            tableClothColor = (TextView) findViewById(R.id.zhuo_bu_yan_se);
            tableAvailable = (TextView) findViewById(R.id.ke_ding_zhuo_shu);
            minCost = (TextView) findViewById(R.id.zui_di_xiao_fei);
            single_icon = (ImageView) findViewById(R.id.single_icon);
            icons_pager = (RollPagerView) findViewById(R.id.room_icons_roll_pager);
            config_gv = (UnScrollableGridView) findViewById(R.id.config_gv);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBanquetHallMessageEvent(BanquetHallMessageEvent event){
        detail = event.getBanquetHallDetail();
        Log.d(TAG, detail.toString());
        if(detail.pictureUrls == null || detail.pictureUrls.size() == 0){
            single_icon.setVisibility(View.VISIBLE);
            icons_pager.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.ph_long).into(single_icon);
        }else if(detail.pictureUrls.size() == 1){
            single_icon.setVisibility(View.VISIBLE);
            icons_pager.setVisibility(View.GONE);
            Glide.with(this).load(Uri.parse(detail.pictureUrls.get(0))).placeholder(R.drawable.loading).into(single_icon);
        }else if(detail.pictureUrls.size() > 1){
            icons_pager.setAdapter(new RoomIconLooperAdapter(icons_pager));
        }
        room_name.setText(detail.name);
        area.setText("場地面積: " + detail.area + "㎡");
        height.setText("樓層高度: " + detail.height + "m");
        lizhu.setText("立柱情況: " + detail.columns);
        roomShape.setText("大廳形狀: " + detail.shape);
        welcomeArea.setText("迎賓區域: " + detail.actualArea);
        tableClothColor.setText("桌布顏色: " + detail.colorOfTablecloth);
        tableAvailable.setText("可訂桌數: " + detail.tableRange[0] + "-" + detail.tableRange[1]+"桌");
        minCost.setText("最低消費: $" + detail.minimumPrice);
        config_gv.setAdapter(new RoomConfigAdapter(this,R.layout.config_bar,detail.extraInfos));
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        banquetHall = (BanquetHall) intent.getSerializableExtra(KEY_ROOM);
        utility = BanquetHallUtility.getInstance(this);
    }

    @Override
    public void showContent() {
        utility.initBanquetHall(banquetHall.id);
    }

    public static void start(Context context, BanquetHall room){
        Intent intent = new Intent(context,HotelRoomActivity.class);
        intent.putExtra(KEY_ROOM,room);
        context.startActivity(intent);
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
            case R.id.consult_btn:
                if(LoginUserHolder.getInstance().getCurrentUser() == null){
                    LoginActivity.startOnBackActivity(this);
                    overridePendingTransition(R.anim.bottom2top,R.anim.hold);
                }else{
                    HotelDetail hotelDetail = new HotelDetail();
                    hotelDetail.banquetHalls = new ArrayList<>();
                    hotelDetail.banquetHalls.add(banquetHall);
                    ConsultActivity.start(this,hotelDetail);
                }
                break;
            default:
                break;
        }
    }

    private  class RoomIconLooperAdapter extends LoopPagerAdapter {

        public RoomIconLooperAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            String picUrl = detail.pictureUrls.get(position);
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WeakReference<ImageView> imageViewWeakReference = new WeakReference<ImageView>(imageView);
            ImageView view = imageViewWeakReference.get();
            Glide.with(container.getContext()).load(Uri.parse(picUrl)).placeholder(R.drawable.loading).into(view);
            return imageView;
        }

        @Override
        public int getRealCount() {
            return detail.pictureUrls.size();
        }
    }
}
