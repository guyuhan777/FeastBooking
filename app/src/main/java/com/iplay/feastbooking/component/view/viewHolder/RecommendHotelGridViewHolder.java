package com.iplay.feastbooking.component.view.viewHolder;

import android.view.View;
import android.widget.ImageView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/7.
 */

public class RecommendHotelGridViewHolder extends BasicViewHolder {

    public ImageView leftView;

    public ImageView rightView;

    public RecommendHotelGridViewHolder(View itemView) {
        super(itemView);
        leftView = (ImageView) itemView.findViewById(R.id.recommend_image_left_part);
        rightView = (ImageView) itemView.findViewById(R.id.recommend_image_right_part);
    }

}
