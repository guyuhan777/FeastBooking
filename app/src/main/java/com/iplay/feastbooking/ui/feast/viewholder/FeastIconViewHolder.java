package com.iplay.feastbooking.ui.feast.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/17.
 */

public class FeastIconViewHolder extends BasicViewHolder {

    public ImageView icon;

    public FeastIconViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.feast_icon);
    }
}
