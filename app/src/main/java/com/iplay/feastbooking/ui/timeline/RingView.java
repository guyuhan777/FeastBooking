package com.iplay.feastbooking.ui.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.LengthUnitTranser;

/**
 * Created by Guyuhan on 2017/10/28.
 */

public class RingView extends View{

    private Paint p;

    private int radiusDp;

    private int borderDp;

    public RingView(Context context) {
        super(context);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(getResources().getColor(R.color.deep_grey));
        p.setStyle(Paint.Style.STROKE);
        borderDp = LengthUnitTranser.dip2px(context, 4);
        p.setStrokeWidth(borderDp);
        radiusDp = LengthUnitTranser.dip2px(context, 8);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(radiusDp + borderDp, radiusDp + borderDp, radiusDp + 2, p);
    }
}
