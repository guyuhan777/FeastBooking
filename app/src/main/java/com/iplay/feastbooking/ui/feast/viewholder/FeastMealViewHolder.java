package com.iplay.feastbooking.ui.feast.viewholder;

import android.view.View;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/17.
 */

public class FeastMealViewHolder extends BasicViewHolder {

    public TextView leftMeal;

    public TextView rightMeal;

    public FeastMealViewHolder(View itemView) {
        super(itemView);
        leftMeal = (TextView) itemView.findViewById(R.id.left_meal);
        rightMeal = (TextView) itemView.findViewById(R.id.right_meal);
    }
}
