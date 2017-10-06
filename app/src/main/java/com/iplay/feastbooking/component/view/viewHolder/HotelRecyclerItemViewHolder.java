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

    public TextView hotel_name_iv;

    public TextView price_range_iv;

    public TextView table_num_iv;

    public TextView remark_num_iv;

    public HotelRecyclerItemViewHolder(View itemView) {
        super(itemView);
        hotel_icon_iv = (ImageView) itemView.findViewById(R.id.hotel_icon);
        hotel_name_iv = (TextView) itemView.findViewById(R.id.hotel_name);
        price_range_iv = (TextView) itemView.findViewById(R.id.price_range_tv);
        table_num_iv = (TextView) itemView.findViewById(R.id.table_num_tv);
        remark_num_iv = (TextView) itemView.findViewById(R.id.remark_tv);
    }
}
