package com.iplay.feastbooking.ui.recommendedHotel;

import com.iplay.feastbooking.component.view.tab.SortTab;

/**
 * Created by gu_y-pc on 2017/12/29.
 */

public interface OnSortLabelClickListener {
    boolean postClick();

    boolean afterClick();

    void unSelectOther(SortTab sortTab);
}
