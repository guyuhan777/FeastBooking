package com.iplay.feastbooking.ui.recommendedHotel.delegates;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.ui.recommendedHotel.data.RecommendHotelHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.basic.BasicHomeData;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class RecommendHotelAdapterDelegate extends AdapterDelegate<List<BasicHomeData>> {

    private LayoutInflater inflater;

    private WeakReference<Context> contextWeakReference;

    public RecommendHotelAdapterDelegate(Activity activity){
        inflater = LayoutInflater.from(activity);
        contextWeakReference = new WeakReference<Context>(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicHomeData> items, int position) {
        return items.get(position) instanceof RecommendHotelHomeData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new RecommendHotelViewHolder(inflater.inflate(R.layout.special_recommend_hotel_layout, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicHomeData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        RecommendHotelHomeData data = (RecommendHotelHomeData) items.get(position);
        RecommendHotelViewHolder recommendHotelViewHolder = (RecommendHotelViewHolder) holder;
        Glide.with(contextWeakReference.get()).load(data.getRecommendGrid().getUrl()).placeholder(R.drawable.loading_reco).into(recommendHotelViewHolder.icon);
    }

    private static class RecommendHotelViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;

         RecommendHotelViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.recommend_hotel_iv);
        }
    }
}
