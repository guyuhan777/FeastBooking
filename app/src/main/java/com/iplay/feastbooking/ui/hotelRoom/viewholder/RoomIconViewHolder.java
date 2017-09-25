package com.iplay.feastbooking.ui.hotelRoom.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/24.
 */

public class RoomIconViewHolder extends BasicViewHolder {

    public ImageView icon;

    public RoomIconViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.room_icon);
    }
}
