package com.iplay.feastbooking.ui.order.delegate;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.ui.order.data.FootStateData;
import com.iplay.feastbooking.ui.order.data.basic.OrderBasicData;

import java.util.List;

/**
 * Created by Guyuhan on 2017/10/29.
 */

public class FooterStateAdapterDelegate extends AdapterDelegate<List<OrderBasicData>> {

    private LayoutInflater inflater;

    public FooterStateAdapterDelegate(Activity activity) {
        inflater = LayoutInflater.from(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<OrderBasicData> items, int position) {
        return items.get(position) instanceof FootStateData && ((FootStateData) items.get(position)).getType() == FootStateData.TYPE_ALL_LOADED;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new FooterStateViewHolder(inflater.inflate(R.layout.load_state_footer, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<OrderBasicData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        // do nothing
    }

    private static class FooterStateViewHolder extends RecyclerView.ViewHolder{

        FooterStateViewHolder(View itemView) {
            super(itemView);
        }
    }
}
