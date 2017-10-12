package com.iplay.feastbooking.component.view.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;

/**
 * Created by admin on 2017/10/11.
 */

public class FunctionBar extends LinearLayout {

    public TextView function_name_tv;

    public ImageView forward_iv;

    private LinearLayout root_view;

    public FunctionBar(Context context) {
        super(context);
    }

    public FunctionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.function_bar,this,true);
        function_name_tv = (TextView) findViewById(R.id.function_name_tv);
        forward_iv = (ImageView) findViewById(R.id.forwards);
        root_view = (LinearLayout) findViewById(R.id.fuction_bar_root);
    }

    public void enable(){
        root_view.setEnabled(true);
        root_view.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void disable(){
        root_view.setEnabled(false);
        root_view.setBackgroundColor(getResources().getColor(R.color.button_grey));
    }

    public FunctionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
