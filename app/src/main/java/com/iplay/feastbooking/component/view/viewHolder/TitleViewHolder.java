package com.iplay.feastbooking.component.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/7.
 */

public class TitleViewHolder extends BasicViewHolder {

    private TextView title;

    public TitleViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.recommend_title);
    }

    public void setText(String titleStr){
        title.setText(titleStr);
    }
}
