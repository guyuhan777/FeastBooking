package com.iplay.feastbooking.ui.order.delegate;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.ui.order.data.FootStateData;
import com.iplay.feastbooking.ui.order.data.basic.OrderBasicData;

import java.util.List;

/**
 * Created by Guyuhan on 2017/10/29.
 */

public class LoadingAdapter extends AdapterDelegate<List<OrderBasicData>> {

    private LayoutInflater inflater;

    public LoadingAdapter(Activity activity) {
        inflater = LayoutInflater.from(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<OrderBasicData> items, int position) {
        return items.get(position) instanceof FootStateData && ((FootStateData) items.get(position)).getType() == FootStateData.TYPE_LOADING;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new UnderLoadingViewHolder(inflater.inflate(R.layout.progress_bar, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<OrderBasicData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ((UnderLoadingViewHolder) holder).pb.setIndeterminate(true);
    }

    private static class UnderLoadingViewHolder extends RecyclerView.ViewHolder{

        ProgressBar pb;

        public UnderLoadingViewHolder(View itemView) {
            super(itemView);
            pb = (ProgressBar) itemView.findViewById(R.id.refresh_progress_bar);
        }
    }
}
