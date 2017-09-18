package com.iplay.feastbooking.ui.hotelDetail.viewHolder;

import android.view.View;
import android.widget.ImageView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/12.
 */

public class HotelFixedDescribeViewHolder extends BasicViewHolder {

    public ImageView icon;

    public HotelFixedDescribeViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.hotel_main_icon);
    }
}
