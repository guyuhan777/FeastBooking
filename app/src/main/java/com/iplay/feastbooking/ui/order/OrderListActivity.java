package com.iplay.feastbooking.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.iplay.feastbooking.gson.order.OrderListRequireConfig;
import com.iplay.feastbooking.messageEvent.order.OrderListMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.order.OrderListUtility;
import com.iplay.feastbooking.ui.order.adapter.OrderRecyclerViewAdapter;
import com.iplay.feastbooking.ui.order.data.FootStateData;
import com.iplay.feastbooking.ui.order.data.OrderItemData;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guyuhan on 2017/10/28.
 */

public class OrderListActivity extends BasicActivity implements View.OnClickListener{

    private static final String STATUS_KEY = "STATUS_KEY";

    private boolean unfinished;

    private RelativeLayout load_state_rl;

    private TextView load_tv;

    private ProgressBar refresh_progress_bar;

    private RecyclerView order_list_rv;

    private TextView list_title_tv;

    private int visibleThreshold= 1;

    private int totalItemCount;

    private int lastVisibleItemPosition;

    private OrderRecyclerViewAdapter adapter;

    private  volatile boolean isInit = false;

    private Handler postHandler = new Handler();

    public static void start(Context context, boolean unfinished){
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.putExtra(STATUS_KEY, unfinished);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.order_list_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        load_state_rl = (RelativeLayout) findViewById(R.id.load_state_rl);

        list_title_tv = (TextView) findViewById(R.id.order_list_title_tv);
        list_title_tv.setText(unfinished ? "進行中的訂單" : "歷史訂單");
        load_tv = (TextView) findViewById(R.id.load_tv);
        refresh_progress_bar = (ProgressBar) findViewById(R.id.refresh_progress_bar);
        refresh_progress_bar.setIndeterminate(true);
        findViewById(R.id.back_iv).setOnClickListener(this);
        order_list_rv = (RecyclerView) findViewById(R.id.order_list_rv);
    }

    @Override
    public void getData() {
        unfinished = getIntent().getBooleanExtra(STATUS_KEY, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderListMessageEvent(OrderListMessageEvent event){
        if(event.isInit()){
            if(event.getType() == OrderListMessageEvent.TYPE_NO_INTERNET){
                order_list_rv.setVisibility(View.GONE);
                load_state_rl.setVisibility(View.VISIBLE);
                refresh_progress_bar.setVisibility(View.GONE);
                load_tv.setText("網絡不給力");
            }else if(event.getType() == OrderListMessageEvent.TYPE_FAILURE){
                order_list_rv.setVisibility(View.GONE);
                load_state_rl.setVisibility(View.VISIBLE);
                refresh_progress_bar.setVisibility(View.GONE);
                load_tv.setText(event.getFailureReason());
            }else {
                OrderListItem orderListItem = event.getOrderListItemList();
                if(orderListItem == null || orderListItem.content == null || orderListItem.content.size() == 0){
                    order_list_rv.setVisibility(View.GONE);
                    order_list_rv.setVisibility(View.VISIBLE);
                    refresh_progress_bar.setVisibility(View.GONE);
                    load_tv.setText("暫無數據");
                }else {
                    order_list_rv.setVisibility(View.VISIBLE);
                    load_state_rl.setVisibility(View.GONE);
                    if(adapter == null){
                        List<BasicData> dataList = new ArrayList<>();
                        for(int i=0; i<orderListItem.content.size(); i++){
                            OrderItemData orderItemData = new OrderItemData();
                            orderItemData.setContent(orderListItem.content.get(i));
                            dataList.add(orderItemData);
                        }
                        adapter = new OrderRecyclerViewAdapter(this, dataList, unfinished);
                        final LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                        order_list_rv.setLayoutManager(manager);
                        order_list_rv.setAdapter(adapter);
                        isInit = true;
                        order_list_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                            adapter.loadMoreData(OrderListUtility.getInstance(getApplicationContext()));
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        }else {
            int type = event.getType();
            if(type != OrderListMessageEvent.TYPE_SUCCESS){
                if(type == OrderListMessageEvent.TYPE_NO_INTERNET) {
                    Toast.makeText(this, "網絡不給力", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, event.getFailureReason(), Toast.LENGTH_SHORT).show();
                }
                adapter.cancelLoading();
                FootStateData data = new FootStateData();
                data.setType(FootStateData.TYPE_CLICK_TO_LOAD_MORE);
                adapter.addData(data);
                adapter.notifyDataSetChanged();
                adapter.setLoaded();
            }else{
                adapter.cancelLoading();
                List<OrderListItem.Content> contents;
                if(event.getOrderListItemList() == null){
                    return;
                }else {
                    contents = event.getOrderListItemList().content;
                    if(contents == null || contents.size() == 0){
                        FootStateData footStateData = new FootStateData();
                        footStateData.setType(FootStateData.TYPE_ALL_LOADED);
                        adapter.addData(footStateData);
                    }else {
                        for(int i=0; i<contents.size(); i++){
                            OrderItemData orderItemData = new OrderItemData();
                            orderItemData.setContent(contents.get(i));
                            adapter.addData(orderItemData);
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
    public void showContent() {
        if(!NetProperties.isNetworkConnected(this)){
            order_list_rv.setVisibility(View.GONE);
            load_state_rl.setVisibility(View.VISIBLE);
            load_tv.setText("網絡不給力");
            refresh_progress_bar.setVisibility(View.GONE);
        }else {
            OrderListRequireConfig.STATUS status = unfinished ?
                    OrderListRequireConfig.STATUS.UNFINISHED : OrderListRequireConfig.STATUS.FINISHED;
            OrderListUtility.getInstance(this).initOrderList(status);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back_iv){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            });
        }
    }
}
