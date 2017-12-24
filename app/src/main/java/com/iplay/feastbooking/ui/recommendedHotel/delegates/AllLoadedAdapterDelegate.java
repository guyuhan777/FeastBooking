package com.iplay.feastbooking.ui.recommendedHotel.delegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.recommendedHotel.data.AllLoadedHomeData;

import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class AllLoadedAdapterDelegate extends AdapterDelegate<List<BasicData>> {

    private LayoutInflater inflater;

    public AllLoadedAdapterDelegate(Activity activity){
        inflater = LayoutInflater.from(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicData> items, int position) {
        return items.get(position) instanceof AllLoadedHomeData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new AllLoadedViewHolder(inflater.inflate(R.layout.load_state_footer, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        // do nothing
    }

    private static class AllLoadedViewHolder extends RecyclerView.ViewHolder{
        AllLoadedViewHolder(View itemView) {
            super(itemView);
        }
    }
}
