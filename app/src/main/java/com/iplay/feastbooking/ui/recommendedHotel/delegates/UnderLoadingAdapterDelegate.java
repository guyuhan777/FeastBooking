package com.iplay.feastbooking.ui.recommendedHotel.delegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.ui.recommendedHotel.data.UnderLoadingHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.basic.BasicHomeData;

import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class UnderLoadingAdapterDelegate extends AdapterDelegate<List<BasicHomeData>> {
    private LayoutInflater inflater;

    public UnderLoadingAdapterDelegate(Activity activity){
        inflater = LayoutInflater.from(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicHomeData> items, int position) {
        return items.get(position) instanceof UnderLoadingHomeData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new UnderLoadingViewHolder(inflater.inflate(R.layout.progress_bar, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicHomeData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
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
