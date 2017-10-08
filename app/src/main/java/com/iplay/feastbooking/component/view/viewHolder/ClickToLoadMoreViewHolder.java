package com.iplay.feastbooking.component.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/10/8.
 */

public class ClickToLoadMoreViewHolder extends BasicViewHolder {

    public TextView click_to_load_more_tv;

    public ClickToLoadMoreViewHolder(View itemView) {
        super(itemView);
        click_to_load_more_tv = (TextView) itemView.findViewById(R.id.click_to_load_more_tv);
    }
}
