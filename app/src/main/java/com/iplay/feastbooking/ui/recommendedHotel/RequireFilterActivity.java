package com.iplay.feastbooking.ui.recommendedHotel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.messageEvent.home.FilterMessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by gu_y-pc on 2018/1/4.
 */

public class RequireFilterActivity extends BasicActivity implements View.OnClickListener{

    private EditText minPriceEt;

    private EditText maxPriceEt;

    private RatingBar ratingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.filter_form_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        findViewById(R.id.submit_tv).setOnClickListener(this);
        findViewById(R.id.root_view).setOnClickListener(this);

        minPriceEt = (EditText) findViewById(R.id.min_price_iv);
        maxPriceEt = (EditText) findViewById(R.id.max_price_iv);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    public static void start(Context context){
        Intent intent = new Intent(context, RequireFilterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    private void hideInput(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    private Double transStr2Double(String str){
        Double result;
        try {
            result = Double.parseDouble(str);
        }catch (NumberFormatException exception){
            result = null;
        }
        return result;
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
            case R.id.root_view:
                hideInput(v);
                break;
            case R.id.submit_tv:
                double minRate = ratingBar.getRating();
                FilterMessageEvent event = new FilterMessageEvent();
                event.setMaxPrice(transStr2Double(maxPriceEt.getText().toString()));
                event.setMinPrice(transStr2Double(minPriceEt.getText().toString()));
                event.setMinRate(minRate);
                EventBus.getDefault().post(event);
                this.finish();
            default:
                break;
        }
    }
}
