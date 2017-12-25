package com.iplay.feastbooking.component.view.tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.iplay.feastbooking.R;

/**
 * Created by gu_y-pc on 2017/12/25.
 */

public class FilterTab extends LinearLayout{

    public FilterTab(Context context) {
        super(context);
    }

    public FilterTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.filter_tab,this,true);
    }

    public FilterTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
