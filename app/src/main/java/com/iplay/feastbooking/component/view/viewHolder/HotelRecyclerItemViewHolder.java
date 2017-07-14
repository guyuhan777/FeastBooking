package com.iplay.feastbooking.component.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/7/14.
 */

public class HotelRecyclerItemViewHolder extends BasicViewHolder {

    public ImageView hotel_icon_iv;

    public TextView hotel_name;

    public TextView hotel_loc;

    public TextView hotel_dist;

    public HotelRecyclerItemViewHolder(View itemView) {
        super(itemView);
        hotel_icon_iv = (ImageView) itemView.findViewById(R.id.hotel_icon);
        hotel_name = (TextView) itemView.findViewById(R.id.hotel_name);
        hotel_loc = (TextView) itemView.findViewById(R.id.hotel_loc);
        hotel_dist = (TextView) itemView.findViewById(R.id.hotel_dist);
    }
}
