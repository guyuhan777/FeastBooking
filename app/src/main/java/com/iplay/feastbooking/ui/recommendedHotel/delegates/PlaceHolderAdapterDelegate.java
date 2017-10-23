package com.iplay.feastbooking.ui.recommendedHotel.delegates;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.LengthUnitTranser;
import com.iplay.feastbooking.ui.recommendedHotel.data.PlaceHolderHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.basic.BasicHomeData;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class PlaceHolderAdapterDelegate extends AdapterDelegate<List<BasicHomeData>> {
    private LayoutInflater inflater;

    private WeakReference<Context> contextWeakReference;

    public PlaceHolderAdapterDelegate(Activity activity){
        inflater = LayoutInflater.from(activity);
        contextWeakReference = new WeakReference<Context>(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicHomeData> items, int position) {
        return items.get(position) instanceof PlaceHolderHomeData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PlaceHolderViewHolder(inflater.inflate(R.layout.placeholder_layout, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicHomeData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        PlaceHolderHomeData data = (PlaceHolderHomeData) items.get(position);
        int height = data.getHeight();
        Context context = contextWeakReference.get();
        if(context == null){
            return;
        }
        int heightPx = LengthUnitTranser.dip2px(context,height);
        ((PlaceHolderViewHolder)holder).root_view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
    }

    private static class PlaceHolderViewHolder extends RecyclerView.ViewHolder{

        LinearLayout root_view;

        PlaceHolderViewHolder(View itemView) {
            super(itemView);
            root_view = (LinearLayout) itemView.findViewById(R.id.place_holder);
        }
    }
}
