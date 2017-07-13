package com.iplay.feastbooking.ui.hotelDetail.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.iplay.feastbooking.R;

/**
 * Created by admin on 2017/7/13.
 */

public class FeastViewHolder extends RecyclerView.ViewHolder {

    public TextView feast_name_tv;

    public FeastViewHolder(View itemView) {
        super(itemView);
        feast_name_tv = (TextView) itemView.findViewById(R.id.feast_name);
    }
}
