package com.iplay.feastbooking.ui.recommendedHotel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicFragment;
import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.entity.RecommendGrid;
import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.messageEvent.home.AdvertisementMessageEvent;
import com.iplay.feastbooking.messageEvent.home.HotelListMessageEvent;
import com.iplay.feastbooking.messageEvent.home.HotelListNoInternetMessageEvent;
import com.iplay.feastbooking.messageEvent.home.RecommendGridMessageEvent;
import com.iplay.feastbooking.net.utilImpl.recommendHotelUtil.RecommendHotelListUtility;
import com.iplay.feastbooking.ui.recommendedHotel.adapter.HomeRecyclerViewAdapter;
import com.iplay.feastbooking.ui.recommendedHotel.data.AdvertisementHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.AllLoadedHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.ClickToLoadMoreHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.HotelHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.RecommendHotelHomeData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class RecommendedHotelFragment extends BasicFragment{

    private RecommendHotelListUtility utility;

    public static final String TAG = "recommendHotelFragment";

    private  volatile boolean isInit = false;

    private View statusView;

    private View view;

    private Context mContext;

    private RecyclerView mainView;

    private int visibleThreshold= 1;

    private int totalItemCount;

    private int lastVisibleItemPosition;

    private HomeRecyclerViewAdapter adapter;

    private boolean isListInit = false;

    private Handler postHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.special_recommend_layout,container,false);

        statusView = view.findViewById(R.id.status_bar_fix);
        statusView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(getActivity())));

        mainView = (RecyclerView) view.findViewById(R.id.main_recycler_view);

        final GridLayoutManager manager = new GridLayoutManager(mainView.getContext(),6,GridLayoutManager.VERTICAL,false);
        mainView.setLayoutManager(manager);
        mainView.setAdapter(adapter);

        mainView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(adapter.isAllLoaded() || !isInit || !isListInit || adapter.isClickToLoadMoreExist()){
                    return;
                }
                totalItemCount = manager.getItemCount();
                lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if(!adapter.isLoading() && totalItemCount <= lastVisibleItemPosition + visibleThreshold){
                    adapter.setLoading();
                    postHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.loadMoreData(utility);
                        }
                    });
                }
            }
        });
        return view;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        isRegisteredNeed = true;
        super.onCreate(savedInstanceState);
        if(isInit){
            return;
        }else{
            mContext = getActivity();
            adapter = new HomeRecyclerViewAdapter(getActivity());
            utility = RecommendHotelListUtility.getInstance(mContext);
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
        AdvertisementHomeData data = new AdvertisementHomeData();
        data.setAdvertisements(advertisements);
        adapter.addData(data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecommendGridMessageEvent(RecommendGridMessageEvent event){
        List<RecommendGrid> recommendGrids = event.getRecommendGrids();
        if(recommendGrids == null || recommendGrids.size() == 0){
            return;
        }
        for(int i=0; i<recommendGrids.size(); i++){
            RecommendHotelHomeData data = new RecommendHotelHomeData();
            data.setRecommendGrid(recommendGrids.get(i));
            adapter.addData(data);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHotelListNoInternetMessageEvent(HotelListNoInternetMessageEvent event){
        int type = event.getType();
        if(type == HotelListNoInternetMessageEvent.TYPE_INIT){
            isListInit = true;
            adapter.addData(new ClickToLoadMoreHomeData());
            adapter.notifyDataSetChanged();
        }else if(type == HotelListNoInternetMessageEvent.TYPE_LOAD_MORE){
            Toast.makeText(mContext,"網絡不給力",Toast.LENGTH_SHORT).show();
            adapter.cancelLoading();
            adapter.addData(new ClickToLoadMoreHomeData());
            adapter.notifyDataSetChanged();
            adapter.setLoaded();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHotelListMessageEvent(final HotelListMessageEvent event){
        int type = event.getType();
        if(type == HotelListMessageEvent.TYPE_INIT){
            List<RecommendHotelGO> hotels = event.getHotels();
            Log.d(TAG, hotels.toString());
            if(hotels == null || hotels.size() == 0){
                return;
            }
            for(int i=0;i<hotels.size();i++ ){
                HotelHomeData data = new HotelHomeData();
                data.setHotel(hotels.get(i));
                adapter.addData(data);
            }
            if(adapter.isAllLoaded() ){
                adapter.addData(new AllLoadedHomeData());
            }
            isListInit = true;
        }else if(type == HotelListMessageEvent.TYPE_LOAD){
            adapter.cancelLoading();
            List<RecommendHotelGO> hotels = event.getHotels();
            Log.d(TAG,"loadMore" + hotels.toString());
            if(hotels == null || hotels.size() == 0){
                adapter.addData(new AllLoadedHomeData());
            }else {
                for(int i=0; i<hotels.size(); i++){
                    HotelHomeData data = new HotelHomeData();
                    data.setHotel(hotels.get(i));
                    adapter.addData(data);
                }
                if(adapter.isAllLoaded()){
                    adapter.addData(new AllLoadedHomeData());
                }
            }
            adapter.setLoaded();
        }

    }

}
