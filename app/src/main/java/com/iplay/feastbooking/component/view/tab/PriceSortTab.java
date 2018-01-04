package com.iplay.feastbooking.component.view.tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.gson.homepage.hotelList.HotelListRequireConfig;

/**
 * Created by gu_y-pc on 2017/12/25.
 */

public class PriceSortTab extends SortTab {
    public PriceSortTab(Context context) {
        super(context);
    }

    public PriceSortTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sort_label_tv.setText(getResources().getText(R.string.filter_tab_price));
    }

    public PriceSortTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void onSortUnSelectAction() {

    }

    @Override
    void onSortSelectAction() {
        config.setSortType(HotelListRequireConfig.SortType.PRICE);
    }
}
