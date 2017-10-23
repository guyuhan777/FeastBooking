package com.iplay.feastbooking.ui.recommendedHotel.delegates;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.ui.recommendedHotel.data.TitleHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.basic.BasicHomeData;

import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class TitleAdapterDelegate extends AdapterDelegate<List<BasicHomeData>> {
    private LayoutInflater inflater;

    public TitleAdapterDelegate(AppCompatActivity activity){
        inflater = LayoutInflater.from(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicHomeData> items, int position) {
        return items.get(position) instanceof TitleHomeData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new TitleViewHolder(inflater.inflate(R.layout.recomment_title_item,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicHomeData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        TitleHomeData titleHomeData = (TitleHomeData) items.get(position);
        ((TitleViewHolder) holder).title_tv.setText(titleHomeData.getTitle().toString());
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder{
        TextView title_tv;

        TitleViewHolder(View itemView) {
            super(itemView);
            title_tv = (TextView) itemView.findViewById(R.id.recommend_title);
        }
    }
}
