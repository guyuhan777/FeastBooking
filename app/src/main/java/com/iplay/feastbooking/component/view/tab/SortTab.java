package com.iplay.feastbooking.component.view.tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.gson.homepage.hotelList.HotelListRequireConfig;

/**
 * Created by gu_y-pc on 2017/12/25.
 */

public abstract class SortTab extends LinearLayout implements View.OnClickListener{

    protected TextView sort_label_tv;

    private ImageView sort_iv;

    private boolean asc;

    private HotelListRequireConfig config;

    public void setConfig(HotelListRequireConfig hotelListRequireConfig){
        config = config == null ? hotelListRequireConfig : config;
    }

    public SortTab(Context context) {
        super(context);
    }

    public SortTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.sort_tab,this,true);
        sort_label_tv = (TextView) findViewById(R.id.filter_label_tv);
        sort_iv = (ImageView) findViewById(R.id.sort_iv);
        setOnClickListener(this);
    }

    public SortTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void unSelectFilter() {
        sort_label_tv.setTextColor(getResources().getColor(R.color.grey));
        //Todo change forwards of sort arrow
        onFilterUnSelectAction();
    }

    abstract void onFilterUnSelectAction();

    abstract void onFilterSelectAction();

    private void onFilterSelect() {
        sort_label_tv.setTextColor(getResources().getColor(R.color.pink));
        //Todo change color and forwards of sort arrow
        onFilterSelectAction();
    }

    @Override
    public void onClick(View v) {
        onFilterSelect();
    }
}
