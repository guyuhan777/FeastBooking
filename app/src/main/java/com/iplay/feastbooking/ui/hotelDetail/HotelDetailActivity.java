package com.iplay.feastbooking.ui.hotelDetail;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicActivity;

/**
 * Created by admin on 2017/7/12.
 */

public class HotelDetailActivity extends BasicActivity implements View.OnClickListener {

    private static final String TAG = "HotelDetailActivity";

    private TextView back_tv;

    private ImageView hotel_icon_iv;

    private TextView flexible_content_tv;

    private TextView fixed_content_tv;

    private static final String DRAWABLE_KEY = "drawable_key";

    private int drawableId;

    private String[] feasts;

    private FeastBar feastBars[];

    private LinearLayout feasts_ll;

    @Override
    public void setContentView() {
        setContentView(R.layout.hotel_detail);
    }

    @Override
    public void findViews() {
        back_tv = (TextView) findViewById(R.id.back_tv);
        back_tv.setOnClickListener(this);
        hotel_icon_iv = (ImageView) findViewById(R.id.hotel_icon);
        Glide.with(this).load(drawableId).into(hotel_icon_iv);
        flexible_content_tv = (TextView) findViewById(R.id.hotel_flexible_area);
        fixed_content_tv = (TextView) findViewById(R.id.hotel_fixed_area);
        feasts_ll = (LinearLayout) findViewById(R.id.feast_list);
        for(int i = 0;i < feastBars.length ; i++){
            feastBars[i] = new FeastBar(this);

            feastBars[i].feastName.setText(feasts[i]);
            feasts_ll.addView(feastBars[i]);
        }
    }

    @Override
    public void getData() {
        drawableId = getIntent().getIntExtra(DRAWABLE_KEY,0);
        feasts = new String[3];
        for (int i = 0;i<feasts.length;i++){
            feasts[i] = "永结同心宴";
        }
        feastBars = new FeastBar[feasts.length];
    }

    @Override
    public void showContent() {

    }

    public static void start(Context context,int drawableId){
        Intent intent = new Intent(context,HotelDetailActivity.class);
        intent.putExtra(DRAWABLE_KEY,drawableId);
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
