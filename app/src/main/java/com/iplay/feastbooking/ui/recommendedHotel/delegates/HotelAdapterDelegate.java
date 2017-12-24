package com.iplay.feastbooking.ui.recommendedHotel.delegates;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.ui.hotelDetail.NewHotelDetailActivity;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.recommendedHotel.data.HotelHomeData;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class HotelAdapterDelegate extends AdapterDelegate<List<BasicData>> {

    private LayoutInflater inflater;

    private WeakReference<Context> contextWeakReference;

    public HotelAdapterDelegate(Activity activity){
        inflater = LayoutInflater.from(activity);
        contextWeakReference = new WeakReference<Context>(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicData> items, int position) {
        return items.get(position) instanceof HotelHomeData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new HotelViewHolder(inflater.inflate(R.layout.special_recommend_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        HotelHomeData data = (HotelHomeData) items.get(position);
        if(data.getHotel()!= null){
            RecommendHotelGO go = data.getHotel();
            HotelViewHolder hotelHolder = (HotelViewHolder) holder;
            if(go.pictureUrl == null || go.pictureUrl.equals("")){
                Glide.with(contextWeakReference.get()).load(R.drawable.placeholder).into(hotelHolder.hotel_icon_iv);
            }else {
                Glide.with(contextWeakReference.get()).load(go.pictureUrl).placeholder(R.drawable.loading_reco).into(hotelHolder.hotel_icon_iv);
            }
            hotelHolder.hotel_name_iv.setText(go.name);
            hotelHolder.price_range_iv.setText(go.getPriceRange());
            hotelHolder.table_num_iv.setText(go.getTableRange());
            hotelHolder.remark_num_iv.setText(go.getNumOfReviews());
            hotelHolder.loc_iv.setText(go.districtOfAddress);
            hotelHolder.itemView.setOnClickListener(new HotelItemOnClickListener(go, contextWeakReference.get()));
        }
    }

    private static class HotelItemOnClickListener implements View.OnClickListener{

        private RecommendHotelGO go;

        private WeakReference<Context> contextWeakReference;

        HotelItemOnClickListener(RecommendHotelGO go, Context context) {
            this.go = go;
            contextWeakReference = new WeakReference<>(context);
        }

        @Override
        public void onClick(View v) {
            if(go != null && contextWeakReference.get() != null) {
                NewHotelDetailActivity.start(contextWeakReference.get(),go);
            }
        }
    }

    private static class HotelViewHolder extends RecyclerView.ViewHolder{
        ImageView hotel_icon_iv;

        TextView hotel_name_iv;

        TextView price_range_iv;

        TextView table_num_iv;

        TextView remark_num_iv;

        TextView loc_iv;

        HotelViewHolder(View itemView) {
            super(itemView);
            hotel_icon_iv = (ImageView) itemView.findViewById(R.id.hotel_icon);
            hotel_name_iv = (TextView) itemView.findViewById(R.id.hotel_name);
            price_range_iv = (TextView) itemView.findViewById(R.id.price_range_tv);
            table_num_iv = (TextView) itemView.findViewById(R.id.table_num_tv);
            remark_num_iv = (TextView) itemView.findViewById(R.id.remark_tv);
            loc_iv = (TextView) itemView.findViewById(R.id.loc_tv);

        }
    }
}
