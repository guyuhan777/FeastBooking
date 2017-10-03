package com.iplay.feastbooking.ui.recommendedHotel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicFragment;
import com.iplay.feastbooking.basic.BasicRecyclerViewAdapter;
import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.entity.RecommendGrid;
import com.iplay.feastbooking.messageEvent.AdvertisementMessageEvent;
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

    private boolean isInit = false;

    private View statusView;

    private View view;

    private Context mContext;

    private RecyclerView mainView;

    private int visibleThreshold= 1;

    private int totalItemCount;

    private int lastVisibleItemPosition;

    private boolean isLoading;

    private Handler handler = new Handler();

    private BasicRecyclerViewAdapter adapter;

    private void setLoaded(){
        isLoading = false;
    }

    public void loadMoreData(){

    }

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
                totalItemCount = manager.getItemCount();
                lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if(!isLoading && totalItemCount <= lastVisibleItemPosition + visibleThreshold){
                    isLoading = true;
                    //oadMoreData();
                }
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        utility.asyncInit();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }


}
