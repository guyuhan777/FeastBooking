package com.iplay.feastbooking.component.view.bar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.iplay.feastbooking.R;

/**
 * Created by admin on 2017/10/13.
 */

public class RatingBar extends LinearLayout {

    public RatingBar(Context context) {
        super(context);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.rating_layout,this,true);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
