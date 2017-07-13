package com.iplay.feastbooking.ui.hotelDetail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.ui.hotelDetail.viewholder.FeastViewHolder;

/**
 * Created by admin on 2017/7/13.
 */

public class FeastAdapter extends RecyclerView.Adapter<FeastViewHolder>{

    private Context mContext;

    private String feasts[];

    private LayoutInflater inflater;

    public FeastAdapter(Context context,String[] feasts){
        mContext = context;
        this.feasts = feasts;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public FeastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeastViewHolder(inflater.inflate(R.layout.hotel_feast_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(FeastViewHolder holder, int position) {
        holder.feast_name_tv.setText(feasts[position]);
    }

    @Override
    public int getItemCount() {
        return feasts.length;
    }
}
