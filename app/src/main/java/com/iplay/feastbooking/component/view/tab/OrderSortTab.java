package com.iplay.feastbooking.component.view.tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.gson.homepage.hotelList.HotelListRequireConfig;
import com.iplay.feastbooking.net.utilImpl.recommendHotelUtil.RecommendHotelListUtility;
import com.iplay.feastbooking.ui.recommendedHotel.OnSortLabelClickListener;

/**
 * Created by gu_y-pc on 2017/12/25.
 */

public class OrderSortTab extends SortTab {

    public OrderSortTab(Context context) {
        super(context);
    }

    public OrderSortTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sort_label_tv.setText(getResources().getText(R.string.filter_tab_order));
    }

    public OrderSortTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void onFilterUnSelectAction() {

    }

    @Override
    void onFilterSelectAction(){
        config.setSortType(HotelListRequireConfig.SortType.ORDER);
    }
}
