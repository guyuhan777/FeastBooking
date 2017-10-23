package com.iplay.feastbooking.ui.recommendedHotel.delegates;

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
import com.iplay.feastbooking.ui.recommendedHotel.adapter.HomeRecyclerViewAdapter;
import com.iplay.feastbooking.ui.recommendedHotel.data.ClickToLoadMoreHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.basic.BasicHomeData;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class ClickToLoadMoreAdapterDelegate extends AdapterDelegate<List<BasicHomeData>> {
    private LayoutInflater inflater;

    private WeakReference<Context> contextWeakReference;

    private WeakReference<HomeRecyclerViewAdapter> adapterWeakReference;

    public ClickToLoadMoreAdapterDelegate(Activity activity, HomeRecyclerViewAdapter adapter){
        inflater = LayoutInflater.from(activity);
        contextWeakReference = new WeakReference<Context>(activity);
        adapterWeakReference = new WeakReference<>(adapter);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicHomeData> items, int position) {
        return items.get(position) instanceof ClickToLoadMoreHomeData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ClickToLoadMoreViewHolder(inflater.inflate(R.layout.click_to_load_more, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicHomeData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        TextView tv = ((ClickToLoadMoreViewHolder) holder).click_to_load_tv;
        adapterWeakReference.get().initListenerOnCLM(tv, contextWeakReference.get());
    }

    private static class ClickToLoadMoreViewHolder extends RecyclerView.ViewHolder{

        TextView click_to_load_tv;

        ClickToLoadMoreViewHolder(View itemView) {
            super(itemView);
            click_to_load_tv = (TextView) itemView.findViewById(R.id.click_to_load_more_tv);
        }
    }
}
