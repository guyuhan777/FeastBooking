package com.iplay.feastbooking.ui.hotelDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.net.NetProperties;

/**
 * Created by admin on 2017/9/11.
 */

public class NewHotelDetailActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "NewHotelDetailActivity";

    public static final String HOTEL_KEY = "HOTEL_KEY";

    private View status_bar_fix_tile;

    private RecommendHotelGO hotel;

    private LinearLayout title_bar;

    private ImageView back_iv;

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
        /*final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content_view.setLayoutManager(manager);
        content_view.setAdapter(adapter);
        content_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentY += dy;
                if(currentY <= 0){
                    title_bar.setBackgroundColor(Color.argb(0,255,130,171));
                    Glide.with(NewHotelDetailActivity.this).load(R.drawable.back_2).into(back_iv);
                }else if(currentY >0 && currentY <= height){
                    float scale = (float) currentY/height;
                    float alpha = scale * 255;
                    title_bar.setBackgroundColor(Color.argb((int)alpha,255,130,171));
                }else{
                    title_bar.setBackgroundColor(Color.argb(255,255,130,171));
                    //Glide.with(NewHotelDetailActivity.this).load(R.drawable.back_4).into(back_iv);
                }
            }
        });*/
    }

    @Override
    public void getData() {
        hotel = (RecommendHotelGO) getIntent().getSerializableExtra(HOTEL_KEY);
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
