package com.iplay.feastbooking.ui.favourite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.iplay.feastbooking.messageEvent.favourite.FavouriteHotelMessageEvent;
import com.iplay.feastbooking.messageEvent.favourite.FavouriteListUpdateMessageEvent;
import com.iplay.feastbooking.messageEvent.review.ReviewListMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.favourite.FavouriteHotelUtility;
import com.iplay.feastbooking.ui.favourite.adapter.FavouriteHotelAdapter;
import com.iplay.feastbooking.ui.order.data.FootStateData;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.recommendedHotel.data.HotelHomeData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gu_y-pc on 2017/12/24.
 */

public class FavouriteHotelActivity extends BasicActivity implements View.OnClickListener{

    private TextView load_state;

    private ProgressBar progressBar;

    private RelativeLayout load_state_rl;

    private RecyclerView hotel_rv;

    private int visibleThreshold= 1;

    private int totalItemCount;

    private int lastVisibleItemPosition;

    private FavouriteHotelAdapter adapter;

    private  volatile boolean isInit = false;

    private Handler postHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    public static void start(Context context){
        Intent intent = new Intent(context, FavouriteHotelActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.favourite_hotel_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        load_state = (TextView) findViewById(R.id.load_tv);
        load_state.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.refresh_progress_bar) ;
        load_state_rl = (RelativeLayout) findViewById(R.id.load_state_rl);
        hotel_rv = (RecyclerView) findViewById(R.id.hotel_list_rv);
        progressBar.setIndeterminate(true);
    }

    private void showLoading(){
        isInit = false;
        hotel_rv.setVisibility(View.GONE);
        load_state_rl.setVisibility(View.VISIBLE);
        load_state.setText(getResources().getText(R.string.loading_hint));
        load_state.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFavouriteListUpdateMessageEvent(FavouriteListUpdateMessageEvent event){
        showLoading();
        adapter = null;
        FavouriteHotelUtility.getInstance(this).load(0, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFavouriteHotelMessageEvent(FavouriteHotelMessageEvent event){
        if(event.isInit()){
            if(event.getType() == FavouriteHotelMessageEvent.TYPE_FAILURE){
                initFailure(event.getFailureReason());
            }else if(event.getType() == FavouriteHotelMessageEvent.TYPE_SUCCESS){
                List<HotelHomeData> hotels = event.getHomeDatas();
                List<BasicData> hotelDatas = new ArrayList<>();
                hotelDatas.addAll(hotels);
                if(hotels == null || hotels.size() == 0){
                    hotel_rv.setVisibility(View.GONE);
                    load_state_rl.setVisibility(View.VISIBLE);
                    load_state.setEnabled(false);
                    load_state.setText("暫無收藏");
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                }else {
                    load_state.setEnabled(false);
                    load_state_rl.setVisibility(View.GONE);
                    hotel_rv.setVisibility(View.VISIBLE);
                    if(adapter == null){
                        adapter = new FavouriteHotelAdapter(this, hotelDatas);
                        final LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                        hotel_rv.setLayoutManager(manager);
                        hotel_rv.setAdapter(adapter);
                        isInit = true;
                        hotel_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if(adapter.isAllLoaded() || !isInit || adapter.isClickToLoadMoreExist()){
                                    return;
                                }
                                totalItemCount = manager.getItemCount();
                                lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                                if(!adapter.isLoading() && totalItemCount <= lastVisibleItemPosition + visibleThreshold){
                                    adapter.setLoading();
                                    postHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.loadMoreData();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        }else{
            if(event.getType() == ReviewListMessageEvent.TYPE_FAILURE){
                Toast.makeText(this, event.getFailureReason(), Toast.LENGTH_SHORT).show();
                adapter.cancelLoading();
                FootStateData data = new FootStateData();
                data.setType(FootStateData.TYPE_CLICK_TO_LOAD_MORE);
                adapter.addData(data);
                adapter.notifyDataSetChanged();
                adapter.setLoaded();
            }else {
                adapter.cancelLoading();
                List<HotelHomeData> hotelHomeDatas;
                if(event.getHomeDatas() == null){
                    return;
                }else {
                    hotelHomeDatas = event.getHomeDatas();
                    if(hotelHomeDatas == null || hotelHomeDatas.size() == 0){
                        FootStateData footStateData = new FootStateData();
                        footStateData.setType(FootStateData.TYPE_ALL_LOADED);
                        adapter.addData(footStateData);
                    }else {
                        for(int i=0; i<hotelHomeDatas.size(); i++){
                            adapter.addData(hotelHomeDatas.get(i));
                        }
                        if(adapter.isAllLoaded()){
                            FootStateData footStateData = new FootStateData();
                            footStateData.setType(FootStateData.TYPE_ALL_LOADED);
                            adapter.addData(footStateData);
                        }
                    }
                }
                adapter.setLoaded();
            }
        }
    }

    @Override
    public void getData() {

    }

    private void initFailure(String failureReason){
        hotel_rv.setVisibility(View.GONE);
        load_state_rl.setVisibility(View.VISIBLE);
        load_state.setEnabled(true);
        load_state.setText(failureReason + ", 點我重試");
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showContent() {
        if(!NetProperties.isNetworkConnected(this)){
            hotel_rv.setVisibility(View.GONE);
            load_state_rl.setVisibility(View.VISIBLE);
            load_state.setEnabled(true);
            load_state.setText("網絡不給力,點我重試");
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.INVISIBLE);
        }else {
            FavouriteHotelUtility.getInstance(this).load(0, true);
        }
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
            case R.id.load_tv:
                load_state.setEnabled(false);
                load_state.setText("正在努力加載中");
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                FavouriteHotelUtility.getInstance(this).load(0, true);
                break;
            default:
                break;
        }
    }
}
