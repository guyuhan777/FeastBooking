package com.iplay.feastbooking.ui.order.delegate;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.ui.order.adapter.OrderRecyclerViewAdapter;
import com.iplay.feastbooking.ui.order.data.FootStateData;
import com.iplay.feastbooking.ui.order.data.basic.OrderBasicData;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Guyuhan on 2017/10/29.
 */

public class ClickToLoadMoreAdapterDelegate extends AdapterDelegate<List<OrderBasicData>> {

    private LayoutInflater inflater;

    private WeakReference<Context> contextWeakReference;

    private WeakReference<OrderRecyclerViewAdapter> adapterWeakReference;

    public ClickToLoadMoreAdapterDelegate(Activity activity, OrderRecyclerViewAdapter adapter) {
        inflater = LayoutInflater.from(activity);
        contextWeakReference = new WeakReference<Context>(activity);
        adapterWeakReference = new WeakReference<>(adapter);
    }

    @Override
    protected boolean isForViewType(@NonNull List<OrderBasicData> items, int position) {
        return items.get(position) instanceof FootStateData && ((FootStateData) items.get(position)).getType() == FootStateData.TYPE_CLICK_TO_LOAD_MORE;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ClickToLoadMoreViewHolder(inflater.inflate(R.layout.click_to_load_more, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<OrderBasicData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ClickToLoadMoreViewHolder clickToLoadMoreVH = (ClickToLoadMoreViewHolder) holder;
        adapterWeakReference.get().initListenerOnCLM(clickToLoadMoreVH.click_to_load_tv, contextWeakReference.get());
    }

    private static class ClickToLoadMoreViewHolder extends RecyclerView.ViewHolder{

        TextView click_to_load_tv;

        ClickToLoadMoreViewHolder(View itemView) {
            super(itemView);
            click_to_load_tv = (TextView) itemView.findViewById(R.id.click_to_load_more_tv);
        }
    }
}
