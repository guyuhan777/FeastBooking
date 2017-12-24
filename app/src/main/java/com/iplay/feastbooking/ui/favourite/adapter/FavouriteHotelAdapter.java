package com.iplay.feastbooking.ui.favourite.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.favourite.FavouriteHotelUtility;
import com.iplay.feastbooking.net.utilImpl.order.OrderListUtility;
import com.iplay.feastbooking.net.utilImpl.reviewUtil.ReviewUtility;
import com.iplay.feastbooking.ui.order.data.FootStateData;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.order.delegate.ClickToLoadMoreAdapterDelegate;
import com.iplay.feastbooking.ui.order.delegate.FooterStateAdapterDelegate;
import com.iplay.feastbooking.ui.order.delegate.LoadingAdapter;
import com.iplay.feastbooking.ui.recommendedHotel.data.HotelHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.delegates.HotelAdapterDelegate;
import com.iplay.feastbooking.ui.review.adapter.ReviewRecyclerViewAdapter;
import com.iplay.feastbooking.ui.review.data.ReviewData;
import com.iplay.feastbooking.ui.uiListener.InitCLMListener;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by gu_y-pc on 2017/12/24.
 */

public class FavouriteHotelAdapter extends RecyclerView.Adapter implements InitCLMListener {

    private List<BasicData> hotelBasics;

    private AdapterDelegatesManager<List<BasicData>> delegatesManager;

    private boolean isLoading = false;

    private int numOfHotels = 0;

    private static final int numPerPage = 10;

    public boolean isLoading(){
        return isLoading;
    }

    public void setLoading(){
        isLoading = true;
    }

    public void setLoaded(){
        isLoading = false;
    }

    private WeakReference<Activity> contextWeakReference;

    public boolean isClickToLoadMoreExist(){
        if(!(hotelBasics == null || hotelBasics.size() == 0) && hotelBasics.get(hotelBasics.size() - 1) instanceof FootStateData){
            return ((FootStateData) hotelBasics.get(hotelBasics.size() - 1)).getType() == FootStateData.TYPE_CLICK_TO_LOAD_MORE;
        }
        return false;
    }

    public FavouriteHotelAdapter(Activity activity, List<BasicData> hotelBasics) {
        contextWeakReference = new WeakReference<>(activity);
        delegatesManager = new AdapterDelegatesManager<>();
        delegatesManager.addDelegate(new HotelAdapterDelegate(activity));
        delegatesManager.addDelegate(new FooterStateAdapterDelegate(activity));
        delegatesManager.addDelegate(new LoadingAdapter(activity));
        delegatesManager.addDelegate(new ClickToLoadMoreAdapterDelegate(activity, this));
        this.hotelBasics = hotelBasics;
        numOfHotels = hotelBasics.size();
        if(numOfHotels < numPerPage){
            FootStateData data = new FootStateData();
            data.setType(FootStateData.TYPE_ALL_LOADED);
            hotelBasics.add(data);
        }
    }

    public synchronized void loadMoreData(){
        Activity activity = contextWeakReference.get();
        if(activity == null){
            return;
        }
        FavouriteHotelUtility utility = FavouriteHotelUtility.getInstance(activity);
        setLoading();
        FootStateData data = new FootStateData();
        data.setType(FootStateData.TYPE_LOADING);
        addData(data);
        int page = numOfHotels/numPerPage;
        utility.load(page, false);
    }

    public void addData(BasicData data){
        hotelBasics.add(data);
        if(data instanceof HotelHomeData){
            numOfHotels++;
        }
        notifyItemChanged(hotelBasics.size() - 1);
    }

    public synchronized void cancelLoading(){
        BasicData data;
        if((data = hotelBasics.get(hotelBasics.size()-1)) instanceof FootStateData){
            if(((FootStateData) data).getType() == FootStateData.TYPE_LOADING){
                hotelBasics.remove(hotelBasics.size()-1);
            }
        }
    }

    @Override
    public void initListenerOnCLM(TextView clickToLoadMore, Context context) {
        clickToLoadMore.setOnClickListener(new FavouriteHotelAdapter.ClickToLoadMoreListener(context));
    }

    public boolean isAllLoaded(){
        return numOfHotels!=0 && numOfHotels%numPerPage!=0;
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(hotelBasics, position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(hotelBasics, position, holder);
    }

    @Override
    public int getItemCount() {
        return hotelBasics == null ? 0 : hotelBasics.size();
    }

    private class ClickToLoadMoreListener implements View.OnClickListener{

        private WeakReference<Context> contextWeakReference;

        ClickToLoadMoreListener(Context context){
            contextWeakReference = new WeakReference<>(context);
        }

        @Override
        public void onClick(View v) {
            if(!NetProperties.isNetworkConnected(contextWeakReference.get())){
                Toast.makeText(contextWeakReference.get(),"網絡不給力",Toast.LENGTH_SHORT).show();
                return;
            }
            int lastIndex = hotelBasics.size() - 1;
            hotelBasics.remove(lastIndex);
            loadMoreData();
        }
    }
}
