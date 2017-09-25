package com.iplay.feastbooking.ui.hotelRoom.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/21.
 */

public class ConfigViewHolder extends BasicViewHolder {

    public TextView[] textViews = new TextView[3];

    public LinearLayout[] layouts = new LinearLayout[3];

    public ConfigViewHolder(View itemView) {
        super(itemView);
        textViews[0] = (TextView) itemView.findViewById(R.id.text1);
        textViews[1] = (TextView) itemView.findViewById(R.id.text2);
        textViews[2] = (TextView) itemView.findViewById(R.id.text3);

        LinearLayout layout = (LinearLayout) itemView;
        layouts[0] = (LinearLayout) layout.getChildAt(0);
        layouts[1] = (LinearLayout) layout.getChildAt(1);
        layouts[2] = (LinearLayout) layout.getChildAt(2);
    }
}
