package com.iplay.feastbooking.ui.review;

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
import com.iplay.feastbooking.gson.order.OrderListItem;
import com.iplay.feastbooking.messageEvent.review.ReviewListMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.order.OrderListUtility;
import com.iplay.feastbooking.net.utilImpl.reviewUtil.ReviewUtility;
import com.iplay.feastbooking.ui.order.adapter.OrderRecyclerViewAdapter;
import com.iplay.feastbooking.ui.order.data.FootStateData;
import com.iplay.feastbooking.ui.order.data.OrderItemData;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.review.adapter.ReviewRecyclerViewAdapter;
import com.iplay.feastbooking.ui.review.data.ReviewData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gu_y-pc on 2017/11/27.
 */

public class HotelReviewActivity extends BasicActivity implements View.OnClickListener{

    private static final String TAG = "HotelReviewActivity";

    private int hotelId;

    private static final String HOTEL_KEY = "HOTEL_KEY";

    private TextView load_state;

    private ProgressBar progressBar;

    private RecyclerView comment_list_rv;

    private RelativeLayout load_state_rl;

    private RecyclerView review_rv;

    private int visibleThreshold= 1;

    private int totalItemCount;

    private int lastVisibleItemPosition;

    private ReviewRecyclerViewAdapter adapter;

    private  volatile boolean isInit = false;

    private Handler postHandler = new Handler();

    @Override
    public void setContentView() {
        setContentView(R.layout.hotel_review_layout);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        load_state = (TextView) findViewById(R.id.load_tv);
        load_state.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.refresh_progress_bar) ;
        comment_list_rv = (RecyclerView) findViewById(R.id.comment_list_rv);
        load_state_rl = (RelativeLayout) findViewById(R.id.load_state_rl);
        review_rv = (RecyclerView) findViewById(R.id.comment_list_rv);
        progressBar.setIndeterminate(true);
    }

    public static void start(Context context, int orderId){
        Intent intent = new Intent(context, HotelReviewActivity.class);
        intent.putExtra(HOTEL_KEY, orderId);
        context.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReviewListMessageEvent(ReviewListMessageEvent event){
        if(event.isInit()){
            if(event.getType() == ReviewListMessageEvent.TYPE_FAILURE){
                initFailure(event.getFailureReason());
            }else if(event.getType() == ReviewListMessageEvent.TYPE_SUCCESS){
                List<ReviewData> reviews = event.getReviews();
                List<BasicData> reviewDatas = new ArrayList<>();
                reviewDatas.addAll(reviews);
                if(reviews == null || reviews.size() == 0){
                    review_rv.setVisibility(View.GONE);
                    load_state_rl.setVisibility(View.VISIBLE);
                    Log.d(TAG, "here");
                    load_state.setEnabled(false);
                    load_state.setText("暫無評論");
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                }else {
                    load_state.setEnabled(false);
                    load_state_rl.setVisibility(View.GONE);
                    review_rv.setVisibility(View.VISIBLE);
                    if(adapter == null){
                        adapter = new ReviewRecyclerViewAdapter(this, reviewDatas, hotelId);
                        final LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                        review_rv.setLayoutManager(manager);
                        review_rv.setAdapter(adapter);
                        isInit = true;
                        review_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                List<ReviewData> reviews;
                if(event.getReviews() == null){
                    return;
                }else {
                    reviews = event.getReviews();
                    if(reviews == null || reviews.size() == 0){
                        FootStateData footStateData = new FootStateData();
                        footStateData.setType(FootStateData.TYPE_ALL_LOADED);
                        adapter.addData(footStateData);
                    }else {
                        for(int i=0; i<reviews.size(); i++){
                            adapter.addData(reviews.get(i));
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

    private void initFailure(String failureReason){
        review_rv.setVisibility(View.GONE);
        load_state_rl.setVisibility(View.VISIBLE);
        load_state.setEnabled(true);
        load_state.setText(failureReason + ", 點我重試");
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getData() {
        hotelId = getIntent().getIntExtra(HOTEL_KEY, -1);
    }

    @Override
    public void showContent() {
        if(!NetProperties.isNetworkConnected(this)){
            review_rv.setVisibility(View.GONE);
            load_state_rl.setVisibility(View.VISIBLE);
            load_state.setEnabled(true);
            load_state.setText("網絡不給力,點我重試");
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.INVISIBLE);
        }else {
            ReviewUtility.getInstance(this).load(0, true, hotelId);
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
                ReviewUtility.getInstance(this).load(0, true, hotelId);
                break;
        }
    }
}
