package com.iplay.feastbooking.component.view.bar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;

/**
 * Created by admin on 2017/10/13.
 */

public class RatingBar extends LinearLayout {

    private ImageView[] stars = new ImageView[5];

    private TextView rate_tv;

    public RatingBar(Context context) {
        super(context);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.rating_layout,this,true);
        stars[0] = (ImageView) findViewById(R.id.star_1);
        stars[1] = (ImageView) findViewById(R.id.star_2);
        stars[2] = (ImageView) findViewById(R.id.star_3);
        stars[3] = (ImageView) findViewById(R.id.star_4);
        stars[4] = (ImageView) findViewById(R.id.star_5);
        rate_tv = (TextView) findViewById(R.id.hotel_rate_tv);
    }

    public void setRate(double rate){
        int base = (int) rate;
        rate_tv.setText(rate+"");
        double digit = rate - base;
        for(int i=0;i<base;i++){
            Glide.with(getContext()).load(R.drawable.star_full).into(stars[i]);
        }
        if(digit>0){
            Glide.with(getContext()).load(R.drawable.star_half).into(stars[base]);
        }
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
