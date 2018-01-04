package com.iplay.feastbooking.ui.recommendedHotel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicFragment;
import com.iplay.feastbooking.component.view.tab.ComplexSortTab;
import com.iplay.feastbooking.component.view.tab.FilterTab;
import com.iplay.feastbooking.component.view.tab.OrderSortTab;
import com.iplay.feastbooking.component.view.tab.PriceSortTab;
import com.iplay.feastbooking.component.view.tab.SortTab;
import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.entity.RecommendGrid;
import com.iplay.feastbooking.exception.DataInconsistentException;
import com.iplay.feastbooking.gson.homepage.hotelList.HotelListFilterRequireConfig;
import com.iplay.feastbooking.gson.homepage.hotelList.HotelListRequireConfig;
import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.messageEvent.home.AdvertisementMessageEvent;
import com.iplay.feastbooking.messageEvent.home.FilterMessageEvent;
import com.iplay.feastbooking.messageEvent.home.HotelListMessageEvent;
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/7/14.
 */

public class RecommendedHotelFragment extends BasicFragment implements OnSortLabelClickListener{

    private  volatile boolean isInit = false;

    private View view;

    private Context mContext;

    private RecyclerView mainView;

    private int visibleThreshold= 1;

    private int totalItemCount;

    private int lastVisibleItemPosition;

    private HomeRecyclerViewAdapter adapter;

    private HotelListRequireConfig config;

    private boolean isListInit = false;

    private Handler postHandler = new Handler();

    private ComplexSortTab complexSortTab;

    private OrderSortTab orderSortTab;

    private PriceSortTab priceSortTab;

    private FilterTab filterTab;

    private Set<SortTab> sortTabGroup = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.special_recommend_layout,container,false);
        view.findViewById(R.id.status_bar_fix)
                .setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        WindowAttr.getStatusBarHeight(getActivity())));

        mainView = (RecyclerView) view.findViewById(R.id.main_recycler_view);

        complexSortTab = (ComplexSortTab) view.findViewById(R.id.complex_sort_tab);
        complexSortTab.setConfig(config);
        complexSortTab.setListener(this);
        sortTabGroup.add(complexSortTab);

        orderSortTab = (OrderSortTab) view.findViewById(R.id.order_sort_tab);
        orderSortTab.setConfig(config);
        orderSortTab.setListener(this);
        sortTabGroup.add(orderSortTab);

        priceSortTab = (PriceSortTab) view.findViewById(R.id.price_sort_tab);
        priceSortTab.setConfig(config);
        priceSortTab.setListener(this);
        sortTabGroup.add(priceSortTab);

        filterTab = (FilterTab) view.findViewById(R.id.filter_tab);
        filterTab.setConfig(config);
        filterTab.setListener(this);

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
                            adapter.loadMoreData(false);
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
            config = HotelListRequireConfig.getDefaultRequireConfig();
            mContext = getActivity();
            adapter = new HomeRecyclerViewAdapter(getActivity(), config);
            RecommendHotelListUtility.getInstance(mContext).asyncInit(mContext, config);
            isInit = true;
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
    public void onHotelListMessageEvent(HotelListMessageEvent event){
        if(event.isInit()){
            if(event.getStatus() == HotelListMessageEvent.Status.SUCCESS) {
                onLoadMoreActionSuccess(event.getHotels(), true);
            }else if(event.getStatus() == HotelListMessageEvent.Status.FAILURE){
                onLoadMoreActionFailure(event.getFailureReason());
            }
        }else {
            if(event.getStatus() == HotelListMessageEvent.Status.SUCCESS){
                onLoadMoreActionSuccess(event.getHotels(), false);
            }else if(event.getStatus() == HotelListMessageEvent.Status.FAILURE){
                onLoadMoreActionFailure(event.getFailureReason());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFilterMessageEvent(FilterMessageEvent event){
        HotelListFilterRequireConfig filterRequireConfig = config.getFilterRequireConfig();
        if (filterRequireConfig != null){
            filterTab.activeFilter();
            filterRequireConfig.setMaxPrice(event.getMaxPrice());
            filterRequireConfig.setMinPrice(event.getMinPrice());
            filterRequireConfig.setMinRating(event.getMinRate());
            if(postClick()){
                afterClick();
            }
        }
    }

    private void onLoadMoreActionFailure(String failureReason){
        adapter.cancelLoading();
        Toast.makeText(getActivity(), failureReason, Toast.LENGTH_SHORT).show();
        adapter.addData(new ClickToLoadMoreHomeData());
        adapter.setLoaded();
    }

    private void onLoadMoreActionSuccess(List<RecommendHotelGO> hotels, boolean initList){
        adapter.cancelLoading();
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
        isListInit = initList;
    }

    @Override
    public boolean postClick() {
        if(adapter.isLoading()){
            return false;
        }else {
            isListInit = false;
            try {
                adapter.clear();
            }catch (DataInconsistentException e){
                return false;
            }
            return true;
        }
    }

    @Override
    public boolean afterClick() {
        adapter.loadMoreData(true);
        return true;
    }

    @Override
    public void unSelectOther(SortTab sortTab) {
        Iterator<SortTab> iterator = sortTabGroup.iterator();
        while (iterator.hasNext()){
            SortTab currentTab = iterator.next();
            if(currentTab != sortTab){
                currentTab.unSelectSort();
            }
        }
    }
}
