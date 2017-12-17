package com.iplay.feastbooking.ui.order.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;
import com.iplay.feastbooking.gson.order.OrderListRequireConfig;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.order.OrderListUtility;
import com.iplay.feastbooking.ui.order.data.FootStateData;
import com.iplay.feastbooking.ui.order.data.OrderItemData;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.order.delegate.ClickToLoadMoreAdapterDelegate;
import com.iplay.feastbooking.ui.order.delegate.FooterStateAdapterDelegate;
import com.iplay.feastbooking.ui.order.delegate.LoadingAdapter;
import com.iplay.feastbooking.ui.order.delegate.OrderItemAdapterDelegate;
import com.iplay.feastbooking.ui.uiListener.InitCLMListener;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Guyuhan on 2017/10/29.
 */

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter implements InitCLMListener{

    private boolean unfinished;

    private List<BasicData> orderBasics;

    private AdapterDelegatesManager<List<BasicData>> delegatesManager;

    private boolean isLoading = false;

    private int numOfOrders = 0;

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
        if(!(orderBasics == null || orderBasics.size() == 0) && orderBasics.get(orderBasics.size() - 1) instanceof FootStateData){
            return ((FootStateData) orderBasics.get(orderBasics.size() - 1)).getType() == FootStateData.TYPE_CLICK_TO_LOAD_MORE;
        }
        return false;
    }

    public OrderRecyclerViewAdapter(Activity activity, List<BasicData> orderBasics, boolean unfinished) {
        this.unfinished = unfinished;
        delegatesManager = new AdapterDelegatesManager<>();
        delegatesManager.addDelegate(new OrderItemAdapterDelegate(activity));
        delegatesManager.addDelegate(new FooterStateAdapterDelegate(activity));
        delegatesManager.addDelegate(new LoadingAdapter(activity));
        delegatesManager.addDelegate(new ClickToLoadMoreAdapterDelegate(activity, this));
        this.orderBasics = orderBasics;
        numOfOrders = orderBasics.size();
        if(numOfOrders < numPerPage){
            FootStateData data = new FootStateData();
            data.setType(FootStateData.TYPE_ALL_LOADED);
            orderBasics.add(data);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(orderBasics, position, holder);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(orderBasics, position);
    }

    @Override
    public int getItemCount() {
        return orderBasics == null ? 0 : orderBasics.size();
    }

    public void addData(BasicData data){
        orderBasics.add(data);
        if(data instanceof OrderItemData){
            numOfOrders++;
        }
        notifyItemChanged(orderBasics.size() - 1);
    }

    public synchronized void cancelLoading(){
        BasicData data;
        if((data = orderBasics.get(orderBasics.size()-1)) instanceof FootStateData){
            if(((FootStateData) data).getType() == FootStateData.TYPE_LOADING){
                orderBasics.remove(orderBasics.size()-1);
            }
        }
    }

    @Override
    public void initListenerOnCLM(TextView clickToLoadMore, Context context){
        clickToLoadMore.setOnClickListener(new ClickToLoadMoreListener(context));
    }

    public synchronized void loadMoreData(OrderListUtility utility){
        if(utility == null){
            return;
        }
        setLoading();
        FootStateData data = new FootStateData();
        data.setType(FootStateData.TYPE_LOADING);
        addData(data);
        int page = numOfOrders/numPerPage;
        OrderListRequireConfig.STATUS status = unfinished ?
                OrderListRequireConfig.STATUS.UNFINISHED : OrderListRequireConfig.STATUS.FINISHED;
        OrderListRequireConfig config = new OrderListRequireConfig();
        config.setStatus(status);
        config.setPage(page);
        config.setInit(false);
        utility.loadMore(config);
    }

    public boolean isAllLoaded(){
        return numOfOrders!=0 && numOfOrders%numPerPage!=0;
    }

    private class ClickToLoadMoreListener implements View.OnClickListener{

        private WeakReference<Context> contextWeakReference;

        private OrderListUtility utility;

        ClickToLoadMoreListener(Context context){
            contextWeakReference = new WeakReference<>(context);
            utility = OrderListUtility.getInstance(contextWeakReference.get());
        }

        @Override
        public void onClick(View v) {
            if(!NetProperties.isNetworkConnected(contextWeakReference.get())){
                Toast.makeText(contextWeakReference.get(),"網絡不給力",Toast.LENGTH_SHORT).show();
                return;
            }
            int lastIndex = orderBasics.size() - 1;
            orderBasics.remove(lastIndex);
            loadMoreData(utility);
        }
    }
}
