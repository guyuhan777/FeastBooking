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
import com.iplay.feastbooking.gson.homepage.hotelList.HotelListFilterRequireConfig;
import com.iplay.feastbooking.gson.homepage.hotelList.HotelListRequireConfig;
import com.iplay.feastbooking.ui.recommendedHotel.OnSortLabelClickListener;

/**
 * Created by gu_y-pc on 2017/12/25.
 */

public abstract class SortTab extends LinearLayout implements View.OnClickListener{

    protected TextView sort_label_tv;

    private ImageView sort_iv;

    public boolean isNextSortAsc() {
        return isNextSortAsc;
    }

    private boolean isNextSortAsc;

    protected HotelListRequireConfig config;

    private OnSortLabelClickListener listener;

    public void setListener(OnSortLabelClickListener listener) {
        this.listener = listener;
    }

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
        isNextSortAsc = true;
    }

    public SortTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void unSelectFilter() {
        sort_label_tv.setTextColor(getResources().getColor(R.color.grey));
        isNextSortAsc = true;
        sort_iv.setBackground(getContext().getResources().getDrawable(R.drawable.sort_not_active));
        onFilterUnSelectAction();
    }

    abstract void onFilterUnSelectAction();

    abstract void onFilterSelectAction();

    private void onFilterSelect() {
        if(listener == null || !listener.postClick()){
            return;
        }
        sort_label_tv.setTextColor(getResources().getColor(R.color.pink));
        config.setAsc(isNextSortAsc);
        if(isNextSortAsc){
            sort_iv.setBackground(getContext().getResources().getDrawable(R.drawable.sort_active_up));
        }else {
            sort_iv.setBackground(getContext().getResources().getDrawable(R.drawable.sort_active_down));
        }
        onFilterSelectAction();
        listener.afterClick();
        isNextSortAsc = !isNextSortAsc;
    }

    @Override
    public void onClick(View v) {
        onFilterSelect();
    }
}
