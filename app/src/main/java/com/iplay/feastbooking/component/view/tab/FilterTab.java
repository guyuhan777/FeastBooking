package com.iplay.feastbooking.component.view.tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.gson.homepage.hotelList.HotelListRequireConfig;
import com.iplay.feastbooking.ui.recommendedHotel.OnSortLabelClickListener;
import com.iplay.feastbooking.ui.recommendedHotel.RequireFilterActivity;

/**
 * Created by gu_y-pc on 2017/12/25.
 */

public class FilterTab extends LinearLayout implements View.OnClickListener{

    private boolean isActive;

    private TextView filter_tv;

    private ImageView filter_iv;

    private HotelListRequireConfig config;

    private OnSortLabelClickListener listener;

    public void setConfig(HotelListRequireConfig config) {
        this.config = config;
    }

    public void setListener(OnSortLabelClickListener listener) {
        this.listener = listener;
    }

    public FilterTab(Context context) {
        super(context);
    }

    public FilterTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.filter_tab,this,true);
        filter_tv = (TextView) findViewById(R.id.filter_label_tv);
        filter_iv = (ImageView) findViewById(R.id.filter_iv);
        setOnClickListener(this);
        isActive = false;
    }

    public FilterTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void activeFilter(){
        isActive = true;
        filter_tv.setText(getResources().getText(R.string.cancel));
        filter_tv.setTextColor(getResources().getColor(R.color.pink));
        filter_iv.setBackground(getResources().getDrawable(R.drawable.filter_active));
    }

    private void inActiveFilter(){
        isActive = false;
        filter_iv.setBackground(getResources().getDrawable(R.drawable.filter));
        filter_tv.setText(getResources().getText(R.string.filter_tab_filter));
        filter_tv.setTextColor(getResources().getColor(R.color.grey));
        if(config != null){
            config.resetFilter();
            if(listener.postClick()){
                if(listener != null){
                    listener.afterClick();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(!isActive){
            RequireFilterActivity.start(getContext());
        }else {
           inActiveFilter();
        }
    }
}
