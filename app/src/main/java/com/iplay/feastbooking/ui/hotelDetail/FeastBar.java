package com.iplay.feastbooking.ui.hotelDetail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;

/**
 * Created by admin on 2017/7/13.
 */

public class FeastBar extends RelativeLayout {

    public TextView feastName;

    public FeastBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.hotel_feast_item_layout,this,true);
        feastName = (TextView) findViewById(R.id.feast_name);
    }

    public FeastBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.hotel_feast_item_layout,this,true);
        feastName = (TextView) findViewById(R.id.feast_name);
    }
}
