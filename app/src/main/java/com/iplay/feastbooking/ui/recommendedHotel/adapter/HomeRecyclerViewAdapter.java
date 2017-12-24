package com.iplay.feastbooking.ui.recommendedHotel.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.recommendHotelUtil.RecommendHotelListUtility;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.recommendedHotel.data.AdvertisementHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.ClickToLoadMoreHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.HotelHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.PlaceHolderHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.RecommendHotelHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.TitleHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.UnderLoadingHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.delegates.AdvertisementAdapterDelegate;
import com.iplay.feastbooking.ui.recommendedHotel.delegates.AllLoadedAdapterDelegate;
import com.iplay.feastbooking.ui.recommendedHotel.delegates.ClickToLoadMoreAdapterDelegate;
import com.iplay.feastbooking.ui.recommendedHotel.delegates.HotelAdapterDelegate;
import com.iplay.feastbooking.ui.recommendedHotel.delegates.PlaceHolderAdapterDelegate;
import com.iplay.feastbooking.ui.recommendedHotel.delegates.RecommendHotelAdapterDelegate;
import com.iplay.feastbooking.ui.recommendedHotel.delegates.TitleAdapterDelegate;
import com.iplay.feastbooking.ui.recommendedHotel.delegates.UnderLoadingAdapterDelegate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ADS = 1;

    private static final int TYPE_ALL_LOADED = 2;

    private static final int TYPE_HOTEL = 3;

    private static final int TYPE_PLACE_HOLDER = 4;

    private static final int TYPE_RECOMMEND_HOTEL = 5;

    private static final int TYPE_TITLE = 6;

    private static final int TYPE_UNDER_LOADING = 7;

    private static final int TYPE_CLICK_TO_LOAD_MORE = 8;

    private boolean isLoading = false;

    private static final int numPerPage = 10;

    private static final int AD_INDEX = 0;

    private static final int SP_RECOMMEND_PH_INDEX = 2;

    private int lastIndexOfSpecialRecommendToInsert = -1;

    private int numOfHotels = 0;

    private List<BasicData> items = new ArrayList<>();

    private AdapterDelegatesManager<List<BasicData>> delegatesManager;

    private WeakReference<Context> contextWeakReference;

    public HomeRecyclerViewAdapter(Activity activity) {
        contextWeakReference = new WeakReference<Context>(activity);
        delegatesManager = new AdapterDelegatesManager<>();
        delegatesManager.addDelegate(TYPE_ADS, new AdvertisementAdapterDelegate(activity));
        delegatesManager.addDelegate(TYPE_ALL_LOADED, new AllLoadedAdapterDelegate(activity));
        delegatesManager.addDelegate(TYPE_HOTEL, new HotelAdapterDelegate(activity));
        delegatesManager.addDelegate(TYPE_PLACE_HOLDER, new PlaceHolderAdapterDelegate(activity));
        delegatesManager.addDelegate(TYPE_RECOMMEND_HOTEL, new RecommendHotelAdapterDelegate(activity));
        delegatesManager.addDelegate(TYPE_TITLE, new TitleAdapterDelegate(activity));
        delegatesManager.addDelegate(TYPE_UNDER_LOADING, new UnderLoadingAdapterDelegate(activity));
        delegatesManager.addDelegate(TYPE_CLICK_TO_LOAD_MORE, new ClickToLoadMoreAdapterDelegate(activity, this));
        init();
    }

    private void init(){
        PlaceHolderHomeData adPhData = new PlaceHolderHomeData();
        adPhData.setHeight(250);
        items.add(adPhData);

        TitleHomeData titleRecommend = new TitleHomeData();
        titleRecommend.setTitle("特别推介");
        items.add(titleRecommend);

        PlaceHolderHomeData rhPhData = new PlaceHolderHomeData();
        rhPhData.setHeight(600);
        items.add(rhPhData);

        TitleHomeData titleAll = new TitleHomeData();
        titleAll.setTitle("所有酒楼");
        items.add(titleAll);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(items, position, holder);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(items, position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0:items.size();
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            GridLayoutManager gridManager = (GridLayoutManager) manager;
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)){
                        case TYPE_RECOMMEND_HOTEL:
                            return 3;
                        default:
                            return 6;
                    }
                }
            });
        }
    }

    public void addData(BasicData data){
        if(data instanceof AdvertisementHomeData){
            items.remove(AD_INDEX);
            items.add(AD_INDEX,data);
            notifyItemChanged(AD_INDEX);
        }else if(data instanceof RecommendHotelHomeData){
            if(items.get(SP_RECOMMEND_PH_INDEX) instanceof PlaceHolderHomeData){
                items.remove(SP_RECOMMEND_PH_INDEX);
                lastIndexOfSpecialRecommendToInsert = SP_RECOMMEND_PH_INDEX;
            }
            if(lastIndexOfSpecialRecommendToInsert != -1){
                items.add(lastIndexOfSpecialRecommendToInsert, data);
                if(lastIndexOfSpecialRecommendToInsert == SP_RECOMMEND_PH_INDEX){
                    notifyItemChanged(SP_RECOMMEND_PH_INDEX);
                }else{
                    notifyItemInserted(lastIndexOfSpecialRecommendToInsert);
                }
                lastIndexOfSpecialRecommendToInsert++;
            }
        }else if(data instanceof HotelHomeData){
            numOfHotels++;
            items.add(data);
            notifyItemChanged(items.size() - 1);
        }else {
            items.add(data);
            notifyItemChanged(items.size()-1);
        }
    }

    public synchronized void loadMoreData(RecommendHotelListUtility utility){
        if(utility == null){
            return;
        }
        setLoading();
        addData(new UnderLoadingHomeData());
        int page = numOfHotels/numPerPage;
        utility.loadMore(page,contextWeakReference.get());
    }

    public boolean isLoading(){
        return isLoading;
    }

    public void setLoading(){
        isLoading = true;
    }

    public void setLoaded(){
        isLoading = false;
    }

    public boolean isClickToLoadMoreExist(){
        return !(items == null || items.size() == 0) && (items.get(items.size() - 1) instanceof ClickToLoadMoreHomeData);
    }

    public boolean isAllLoaded(){
        return numOfHotels!=0 && numOfHotels%numPerPage!=0;
    }

    public void initListenerOnCLM(TextView clickToLoadMore, Context context){
        clickToLoadMore.setOnClickListener(new ClickToLoadMoreListener(context));
    }

    public synchronized void cancelLoading(){
        if(items.get(items.size()-1) instanceof UnderLoadingHomeData){
            items.remove(items.size()-1);
        }
    }

    private class ClickToLoadMoreListener implements View.OnClickListener{

        private WeakReference<Context> contextWeakReference;

        private RecommendHotelListUtility utility;

        ClickToLoadMoreListener(Context context){
            contextWeakReference = new WeakReference<>(context);
            utility = RecommendHotelListUtility.getInstance(contextWeakReference.get());
        }

        @Override
        public void onClick(View v) {
            if(!NetProperties.isNetworkConnected(contextWeakReference.get())){
                Toast.makeText(contextWeakReference.get(),"網絡不給力",Toast.LENGTH_SHORT).show();
                return;
            }
            int lastIndex = items.size() - 1;
            items.remove(lastIndex);
            loadMoreData(utility);
        }
    }
}
