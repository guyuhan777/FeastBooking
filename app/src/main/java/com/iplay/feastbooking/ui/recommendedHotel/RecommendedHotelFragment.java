package com.iplay.feastbooking.ui.recommendedHotel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicFragment;
import com.iplay.feastbooking.basic.BasicRecyclerViewAdapter;
import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.entity.RecommendGrid;
import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.messageEvent.AdvertisementMessageEvent;
import com.iplay.feastbooking.messageEvent.HotelListMessageEvent;
import com.iplay.feastbooking.messageEvent.HotelListNoInternetMessageEvent;
import com.iplay.feastbooking.messageEvent.RecommendGridMessageEvent;
import com.iplay.feastbooking.net.utilImpl.recommendHotelUtil.RecommendHotelListUtility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class RecommendedHotelFragment extends BasicFragment implements View.OnClickListener{

    private RecommendHotelListUtility utility;

    public static final String TAG = "recommendHotelFragment";

    private static final int numPerPage = 10;

    private int currentPage = 0;

    private  volatile boolean isInit = false;

    private View statusView;

    private View view;

    private Context mContext;

    private RecyclerView mainView;

    private int visibleThreshold= 1;

    private int totalItemCount;

    private int lastVisibleItemPosition;

    private volatile boolean isLoading;

    private boolean isAllLoaded = false;

    private BasicRecyclerViewAdapter adapter;

    private Handler postHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        isRegisteredNeed = true;

        view = inflater.inflate(R.layout.special_recommend_layout,container,false);
        mContext = getActivity();

        statusView = (View) view.findViewById(R.id.status_bar_fix);
        statusView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(getActivity())));

        adapter = new BasicRecyclerViewAdapter(getActivity());
        utility = RecommendHotelListUtility.getInstance(mContext);

        mainView = (RecyclerView) view.findViewById(R.id.main_recycler_view);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mainView.setLayoutManager(manager);
        mainView.setAdapter(adapter);

        mainView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isAllLoaded){
                    return;
                }
                totalItemCount = manager.getItemCount();
                lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if(!isLoading && totalItemCount <= lastVisibleItemPosition + visibleThreshold){
                    postHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadMoreData();
                        }
                    });
                }
            }
        });

        return view;
    }

    private void loadMoreData(){
        isLoading = true;
        adapter.addData(BasicRecyclerViewAdapter.TYPE_LOADING,null);
        utility.loadMore(++currentPage);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isInit){
            return;
        }else {
            isInit = true;
            utility.asyncInit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAdvertisementMessageEvent(AdvertisementMessageEvent event){
        List<Advertisement> advertisements = event.getAdvertisements();
        if (advertisements == null || advertisements.size() ==0){
            return;
        }
        adapter.addData(BasicRecyclerViewAdapter.TYPE_ADS, advertisements);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecommendGridMessageEvent(RecommendGridMessageEvent event){
        List<RecommendGrid> recommendGrids = event.getRecommendGrids();
        if(recommendGrids == null || recommendGrids.size() == 0){
            return;
        }
        if(recommendGrids.size()%2 != 0){
            recommendGrids.add(null);
        }

        for(int i=0; i<recommendGrids.size()/2; i++){
            RecommendGrid recommendGrid[] = new RecommendGrid[2];
            recommendGrid[0] = recommendGrids.get(i*2);
            recommendGrid[1] = recommendGrids.get(i*2 + 1);
            adapter.addData(BasicRecyclerViewAdapter.TYPE_GRID, recommendGrid);
        }
    }

    public void onHotelListNoInternetMessageEvent(HotelListNoInternetMessageEvent event){
        int type = event.getType();
        if(type == HotelListNoInternetMessageEvent.TYPE_INIT){

        }else if(type == HotelListNoInternetMessageEvent.TYPE_LOAD_MORE){
            Toast.makeText(mContext,"網絡不給力",Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHotelListMessageEvent(final HotelListMessageEvent event){
        int type = event.getType();
        if(type == HotelListMessageEvent.TYPE_INIT){
            List<RecommendHotelGO> hotels = event.getHotels();
            if(hotels == null || hotels.size() == 0){
                return;
            }
            for(int i=0;i<hotels.size();i++ ){
                adapter.addData(BasicRecyclerViewAdapter.TYPE_HOTEL_INIT,hotels.get(i));
            }
        }else if(type == HotelListMessageEvent.TYPE_LOAD){
            Message msg = new Message();
            msg.obj = event;
            adapter.cancelLoading();
            List<RecommendHotelGO> hotels = event.getHotels();
            if(hotels == null || hotels.size() == 0){
                adapter.addData(BasicRecyclerViewAdapter.TYPE_ALL_LOADED,null);
                isAllLoaded = true;
            }else {
                for(int i=0; i<hotels.size(); i++){
                    adapter.addData(BasicRecyclerViewAdapter.TYPE_HOTEL_LOADMORE,hotels.get(i));
                }
                if(hotels.size()<numPerPage ){
                    adapter.addData(BasicRecyclerViewAdapter.TYPE_ALL_LOADED,null);
                    isAllLoaded = true;
                }
            }
            isLoading = false;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}
