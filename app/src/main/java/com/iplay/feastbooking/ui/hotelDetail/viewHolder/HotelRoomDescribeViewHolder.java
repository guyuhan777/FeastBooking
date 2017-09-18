package com.iplay.feastbooking.ui.hotelDetail.viewHolder;

import android.view.View;
import android.widget.ImageView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/16.
 */

public class HotelRoomDescribeViewHolder extends BasicViewHolder {

    public ImageView hotel_room_icon;

    public HotelRoomDescribeViewHolder(View itemView) {
        super(itemView);
        hotel_room_icon = (ImageView) itemView.findViewById(R.id.hotel_room_icon);
    }
}
