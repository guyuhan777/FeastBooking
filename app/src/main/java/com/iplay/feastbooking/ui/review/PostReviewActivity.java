package com.iplay.feastbooking.ui.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.messageEvent.common.CommonMessageEvent;
import com.iplay.feastbooking.net.utilImpl.consult.ConsultUtility;
import com.iplay.feastbooking.net.utilImpl.reviewUtil.ReviewUtility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Guyuhan on 2017/11/19.
 */

public class PostReviewActivity extends BasicActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener{

    private int orderId;

    private static final String ORDER_ID_KEY = "ORDER_ID_KEY";

    private TextView rating_tv;

    private RatingBar ratingBar;

    private TextView submit_tv;

    private EditText remark_ev;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.order_review_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        findViewById(R.id.root_view).setOnClickListener(this);
        rating_tv = (TextView) findViewById(R.id.rating_tv);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);
        remark_ev = (EditText) findViewById(R.id.review_et);
        submit_tv = (TextView) findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonMessageEvent(CommonMessageEvent commonMessageEvent){
        CommonMessageEvent.TYPE type = commonMessageEvent.getType();
        if(type == CommonMessageEvent.TYPE.TYPE_FAILURE){
            Toast.makeText(this, commonMessageEvent.getFailureResult(), Toast.LENGTH_SHORT).show();
        } else if (type == CommonMessageEvent.TYPE.TYPE_SUCCESS) {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            case R.id.submit_tv:
                submit_tv.setEnabled(false);
                String remark = remark_ev.getText().toString();
                double rating = ratingBar.getRating();
                ReviewUtility.getInstance(this).postComment(orderId, remark, rating, this);
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        rating_tv.setText(rating + "分");
    }
}
