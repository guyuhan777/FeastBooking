package com.iplay.feastbooking.ui.hotelRoom.viewholder;

import android.view.View;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/21.
 */

public class RoomTitleViewHolder extends BasicViewHolder {

    public TextView title;

    public RoomTitleViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.hotel_room_title);
    }
}
