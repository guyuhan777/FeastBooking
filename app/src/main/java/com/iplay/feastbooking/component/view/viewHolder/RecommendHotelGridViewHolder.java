package com.iplay.feastbooking.component.view.viewHolder;

import android.view.View;
import android.widget.ImageView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/7.
 */

public class RecommendHotelGridViewHolder extends BasicViewHolder implements View.OnClickListener {

    public ImageView leftView;

    public ImageView rightView;

    public void setHotelIds(int[] hotelIds) {
        this.hotelIds = hotelIds;
    }

    private int[] hotelIds;

    public RecommendHotelGridViewHolder(View itemView) {
        super(itemView);
        leftView = (ImageView) itemView.findViewById(R.id.recommend_image_left_part);
        leftView.setOnClickListener(this);
        rightView = (ImageView) itemView.findViewById(R.id.recommend_image_right_part);
        rightView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
