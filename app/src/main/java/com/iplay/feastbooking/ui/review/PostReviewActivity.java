package com.iplay.feastbooking.ui.review;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;

/**
 * Created by Guyuhan on 2017/11/19.
 */

public class PostReviewActivity extends BasicActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener{

    private int orderId;

    private static final String ORDER_ID_KEY = "ORDER_ID_KEY";

    private TextView rating_tv;

    private RatingBar ratingBar;

    @Override
    public void setContentView() {
        setContentView(R.layout.order_review_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        rating_tv = (TextView) findViewById(R.id.rating_tv);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);
    }

    public static void start(Context context, int orderId){
        Intent intent = new Intent(context, PostReviewActivity.class);
        intent.putExtra(ORDER_ID_KEY, orderId);
        context.startActivity(intent);
    }

    @Override
    public void getData() {
        orderId = getIntent().getIntExtra(ORDER_ID_KEY, -1);
    }

    @Override
    public void showContent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_iv:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        rating_tv.setText(rating + "分");
    }
}
