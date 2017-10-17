package com.iplay.feastbooking.ui.hotelDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.component.view.bar.RatingBar;
import com.iplay.feastbooking.component.view.gridview.UnScrollableGridView;
import com.iplay.feastbooking.component.view.scrollview.ObservableScrollView;
import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.gson.hotelDetail.HotelDetail;
import com.iplay.feastbooking.messageEvent.hotelDetail.HotelDetailMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.hotelDetail.HotelDetailUtility;
import com.iplay.feastbooking.ui.hotelDetail.adapter.HotelFeastGridAdapter;
import com.iplay.feastbooking.ui.hotelDetail.adapter.HotelRoomGridAdapter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

/**
 * Created by admin on 2017/9/11.
 */

public class NewHotelDetailActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "NewHotelDetailActivity";

    public static final String HOTEL_KEY = "HOTEL_KEY";

    private HotelDetailUtility utility;

    private View status_bar_fix_tile;

    private RecommendHotelGO hotel;

    private LinearLayout title_bar;

    private ImageView back_iv;

    private RollPagerView hotel_icon_pager;

    private ObservableScrollView hotel_sv;

    private ImageView single_pic_iv;

    private TextView hotel_name_tv;

    private TextView hotel_describe;

    private RatingBar ratingBar;

    private boolean isNetOn = false;

    private HotelDetail hotelDetail;

    private View hotel_room_ph;

    private UnScrollableGridView hotel_room_list;

    private UnScrollableGridView hotel_feast_list;

    private int color_change_height = 200;

    public static void start(Context startActivity,RecommendHotelGO hotel){
        Intent intent = new Intent(startActivity,NewHotelDetailActivity.class);
        intent.putExtra(HOTEL_KEY,hotel);
        startActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        if(NetProperties.isNetworkConnected(this)) {
            isNetOn = true;
            setContentView(R.layout.new_hotel_detail);
        }else {
            setContentView(R.layout.hotel_no_internet);
        }
    }

    @Override
    public void findViews() {
        title_bar = (LinearLayout) findViewById(R.id.title_bar);
        status_bar_fix_tile = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix_tile.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        back_iv = (ImageView) findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
        if(isNetOn){
            hotel_sv = (ObservableScrollView) findViewById(R.id.hotel_sv);
            hotel_icon_pager = (RollPagerView) findViewById(R.id.hotel_icons_roll_pager);
            hotel_name_tv = (TextView) findViewById(R.id.hotel_name);
            hotel_describe = (TextView) findViewById(R.id.hotel_describe);
            ratingBar = (RatingBar) findViewById(R.id.hotel_rate);
            single_pic_iv = (ImageView) findViewById(R.id.single_icon);
            hotel_room_ph = (View) findViewById(R.id.hotel_room_placeholder);
            hotel_room_list = (UnScrollableGridView) findViewById(R.id.hotel_room_list);
            hotel_feast_list = (UnScrollableGridView) findViewById(R.id.hotel_feast_list);
            hotel_sv.setOnScollChangedListener(new ObservableScrollView.OnScrollChangedListener(){
                @Override
                public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                    if(y<=0) {
                        title_bar.setBackgroundColor(Color.argb(0, 255, 255, 255));
                    }else if(y<color_change_height){
                        float scale = (float) y / color_change_height;
                        float alpha = scale * 255;
                        title_bar.setBackgroundColor(Color.argb((int)alpha, 255, 255, 255));
                    }else{
                        title_bar.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHotelDetailMessageEvent(HotelDetailMessageEvent event){
        hotelDetail = event.getHotelDetail();
        hotel_name_tv.setText(hotelDetail.name);
        hotel_describe.setText(hotelDetail.description);
        ratingBar.setRate(hotelDetail.rating);
        if(hotelDetail.pictureUrls == null || hotelDetail.pictureUrls.size() == 0){
            hotel_icon_pager.setVisibility(View.GONE);
            single_pic_iv.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.ph_long).into(single_pic_iv);
        } else if(hotelDetail.pictureUrls.size() > 1){
            hotel_icon_pager.setAdapter(new HotelIconLooperAdapter(hotel_icon_pager));
        }else if(hotelDetail.pictureUrls.size() == 1){
            hotel_icon_pager.setVisibility(View.GONE);
            single_pic_iv.setVisibility(View.VISIBLE);
            Glide.with(this).load(Uri.parse(hotelDetail.pictureUrls.get(0))).placeholder(R.drawable.loading).into(single_pic_iv);
        }

        if(hotelDetail.banquetHalls.size() != 0){
            hotel_room_ph.setVisibility(View.GONE);
            hotel_room_list.setVisibility(View.VISIBLE);
            HotelRoomGridAdapter adapter = new HotelRoomGridAdapter(this,R.layout.room_desribe,hotelDetail.banquetHalls);
            hotel_room_list.setAdapter(adapter);
        }

        if(hotelDetail.feasts.size()!=0){
            hotel_feast_list.setAdapter(new HotelFeastGridAdapter(this,R.layout.feast_detail_check,hotelDetail.feasts));
        }
        hotel_sv.smoothScrollTo(0,20);
    }

    @Override
    public void getData() {
        isRegistered = true;
        hotel = (RecommendHotelGO) getIntent().getSerializableExtra(HOTEL_KEY);
        utility = HotelDetailUtility.getInstance(this);
    }

    @Override
    public void showContent() {

        utility.initHotelDetail(hotel.id);
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
        }
    }

    private  class HotelIconLooperAdapter extends LoopPagerAdapter{

        public HotelIconLooperAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            String picUrl = hotelDetail.pictureUrls.get(position);
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
            return hotelDetail.pictureUrls.size();
        }
    }
}
