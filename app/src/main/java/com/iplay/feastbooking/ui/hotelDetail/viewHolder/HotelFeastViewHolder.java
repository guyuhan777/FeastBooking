package com.iplay.feastbooking.ui.hotelDetail.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/16.
 */

public class HotelFeastViewHolder extends BasicViewHolder {

    public TextView name_tv;

    public ImageView detail;

    public HotelFeastViewHolder(View itemView) {
        super(itemView);
        name_tv = (TextView) itemView.findViewById(R.id.feast_name);
        detail = (ImageView) itemView.findViewById(R.id.forwards);
    }
}
