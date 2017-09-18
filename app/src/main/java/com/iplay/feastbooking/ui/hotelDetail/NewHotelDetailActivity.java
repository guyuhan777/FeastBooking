package com.iplay.feastbooking.ui.hotelDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.entity.Hotel;
import com.iplay.feastbooking.factory.HotelFactory;
import com.iplay.feastbooking.ui.hotelDetail.adapter.HotelDetailAdapter;

/**
 * Created by admin on 2017/9/11.
 */

public class NewHotelDetailActivity extends BasicActivity implements View.OnClickListener{

    private View status_bar_fix_tile;

    private HotelDetailAdapter adapter;

    private RecyclerView content_view;

    private Hotel hotel;

    private LinearLayout title_bar;

    private int currentY;

    private int height = 480;

    private ImageView back_iv;

    public static void start(Context startActivity){
        Intent intent = new Intent(startActivity,NewHotelDetailActivity.class);
        startActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.new_hotel_detail);
    }

    @Override
    public void findViews() {
        title_bar = (LinearLayout) findViewById(R.id.title_bar);
        status_bar_fix_tile = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix_tile.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        back_iv = (ImageView) findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
        Glide.with(this).load(R.drawable.back_2).into(back_iv);
        content_view = (RecyclerView) findViewById(R.id.hotel_content_recycler_view);
        adapter = new HotelDetailAdapter(this);
        adapter.setHotel(hotel);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content_view.setLayoutManager(manager);
        content_view.setAdapter(adapter);
        content_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentY += dy;
                if(currentY <= 0){
                    title_bar.setBackgroundColor(Color.argb(0,250,129,177));
                    Glide.with(NewHotelDetailActivity.this).load(R.drawable.back_2).into(back_iv);
                }else if(currentY >0 && currentY <= height){
                    float scale = (float) currentY/height;
                    float alpha = scale * 255;
                    title_bar.setBackgroundColor(Color.argb((int)alpha,250,129,177));
                    /*if(currentY >= height * 0.8){
                        Glide.with(NewHotelDetailActivity.this).load(R.drawable.back_4).into(back_iv);
                    }else{
                        Glide.with(NewHotelDetailActivity.this).load(R.drawable.back_2).into(back_iv);
                    }*/
                }else{
                    title_bar.setBackgroundColor(Color.argb(255,250,129,177));
                    //Glide.with(NewHotelDetailActivity.this).load(R.drawable.back_4).into(back_iv);
                }
            }
        });
    }

    @Override
    public void getData() {
        hotel = HotelFactory.getSampleHotel();
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
        }
    }
}
