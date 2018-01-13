package com.iplay.feastbooking.component.view.messageIcon;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iplay.feastbooking.R;

/**
 * Created by gu_y-pc on 2018/1/13.
 */

public class MessageIcon extends RelativeLayout {

    private LinearLayout redDot;

    public MessageIcon(Context context) {
        super(context);
    }

    public MessageIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.message_icon, this, true);
        redDot = (LinearLayout) findViewById(R.id.red_dot);
    }

    public MessageIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRedDotVisible(boolean visible){
        if(visible){
            redDot.setVisibility(VISIBLE);
        }else {
            redDot.setVisibility(INVISIBLE);
        }
    }
}
