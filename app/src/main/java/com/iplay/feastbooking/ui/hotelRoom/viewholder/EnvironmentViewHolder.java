package com.iplay.feastbooking.ui.hotelRoom.viewholder;

import android.view.View;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/21.
 */

public class EnvironmentViewHolder extends BasicViewHolder {

    public TextView left_env;

    public TextView right_env;

    public EnvironmentViewHolder(View itemView) {
        super(itemView);
        left_env = (TextView) itemView.findViewById(R.id.left_env);
        right_env = (TextView) itemView.findViewById(R.id.right_env);
    }
}
