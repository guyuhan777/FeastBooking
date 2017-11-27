package com.iplay.feastbooking.ui.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;

/**
 * Created by gu_y-pc on 2017/11/27.
 */

public class HotelReviewActivity extends BasicActivity implements View.OnClickListener{

    private static final String TAG = "HotelReviewActivity";

    private int hotelId;

    private static final String HOTEL_KEY = "HOTEL_KEY";

    private TextView load_state;

    private ProgressBar progressBar;

    private RecyclerView comment_list_rv;

    private RelativeLayout load_state_rl;

    @Override
    public void setContentView() {
        setContentView(R.layout.hotel_review_layout);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        load_state = (TextView) findViewById(R.id.load_tv);
        progressBar = (ProgressBar) findViewById(R.id.refresh_progress_bar) ;
        comment_list_rv = (RecyclerView) findViewById(R.id.comment_list_rv);
        load_state_rl = (RelativeLayout) findViewById(R.id.load_state_rl);
    }

    public static void startActivity(Context context, int orderId){
        Intent intent = new Intent(context, HotelReviewActivity.class);
        intent.putExtra(HOTEL_KEY, orderId);
        context.startActivity(intent);
    }

    @Override
    public void getData() {
        hotelId = getIntent().getIntExtra(HOTEL_KEY, -1);
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
        }
    }
}
