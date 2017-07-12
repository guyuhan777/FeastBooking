package com.iplay.feastbooking.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicActivity;

/**
 * Created by admin on 2017/7/12.
 */

public class HotelDetailActivity extends BasicActivity implements View.OnClickListener {

    private TextView back_tv;

    @Override
    public void setContentView() {
        setContentView(R.layout.hotel_detail);
    }

    @Override
    public void findViews() {
        back_tv = (TextView) findViewById(R.id.back_tv);
        back_tv.setOnClickListener(this);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    public static void start(Context context){
        Intent intent = new Intent(context,HotelDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_tv:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
                break;
        }
    }
}
