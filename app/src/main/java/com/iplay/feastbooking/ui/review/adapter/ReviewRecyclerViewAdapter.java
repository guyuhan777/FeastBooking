package com.iplay.feastbooking.ui.review.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;
import com.iplay.feastbooking.ui.order.adapter.OrderRecyclerViewAdapter;
import com.iplay.feastbooking.ui.order.data.FootStateData;
import com.iplay.feastbooking.ui.order.data.OrderItemData;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.order.delegate.ClickToLoadMoreAdapterDelegate;
import com.iplay.feastbooking.ui.order.delegate.FooterStateAdapterDelegate;
import com.iplay.feastbooking.ui.order.delegate.LoadingAdapter;
import com.iplay.feastbooking.ui.order.delegate.OrderItemAdapterDelegate;
import com.iplay.feastbooking.ui.review.data.ReviewData;
import com.iplay.feastbooking.ui.review.delegate.ReviewItemAdapterDelegate;
import com.iplay.feastbooking.ui.uiListener.InitCLMListener;

import java.util.List;

/**
 * Created by gu_y-pc on 2017/11/28.
 */

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter implements InitCLMListener {

    private List<BasicData> reviewBasics;

    private AdapterDelegatesManager<List<BasicData>> delegatesManager;

    private boolean isLoading = false;

    private int numOfReviews = 0;

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

    public boolean isClickToLoadMoreExist(){
        if(!(reviewBasics == null || reviewBasics.size() == 0) && reviewBasics.get(reviewBasics.size() - 1) instanceof FootStateData){
            return ((FootStateData) reviewBasics.get(reviewBasics.size() - 1)).getType() == FootStateData.TYPE_CLICK_TO_LOAD_MORE;
        }
        return false;
    }

    public ReviewRecyclerViewAdapter(Activity activity, List<BasicData> reviewBasics) {
        delegatesManager = new AdapterDelegatesManager<>();
        delegatesManager.addDelegate(new ReviewItemAdapterDelegate(activity));
        delegatesManager.addDelegate(new FooterStateAdapterDelegate(activity));
        delegatesManager.addDelegate(new LoadingAdapter(activity));
        delegatesManager.addDelegate(new ClickToLoadMoreAdapterDelegate(activity, this));
        this.reviewBasics = reviewBasics;
        numOfReviews = reviewBasics.size();
        if(numOfReviews < numPerPage){
            FootStateData data = new FootStateData();
            data.setType(FootStateData.TYPE_ALL_LOADED);
            reviewBasics.add(data);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(reviewBasics, position, holder);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(reviewBasics, position);
    }

    @Override
    public int getItemCount() {
        return reviewBasics == null ? 0 : reviewBasics.size();
    }

    public void addData(BasicData data){
        reviewBasics.add(data);
        if(data instanceof ReviewData){
            numOfReviews++;
        }
        notifyItemChanged(reviewBasics.size() - 1);
    }

    public synchronized void cancelLoading(){
        BasicData data;
        if((data = reviewBasics.get(reviewBasics.size()-1)) instanceof FootStateData){
            if(((FootStateData) data).getType() == FootStateData.TYPE_LOADING){
                reviewBasics.remove(reviewBasics.size()-1);
            }
        }
    }

    @Override
    public void initListenerOnCLM(TextView clickToLoadMore, Context context) {
    }
}
