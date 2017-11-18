package com.iplay.feastbooking.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.gson.orderDetail.OrderDetail;
import com.iplay.feastbooking.messageEvent.orderdetail.OrderDetailChangeMessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Guyuhan on 2017/11/18.
 */

public class ChangeFeastDateActivity extends BasicActivity implements View.OnClickListener{

    private OrderDetail detail;

    private static final String ORDERR_DETAIL_KEY = "order_detail_key";

    private TextView change_fesat_date_tv;

    private TextView submit_tv;

    private ProgressBar loading_pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    public static void start(Context context, OrderDetail detail){
        Intent intent = new Intent(context, ChangeFeastDateActivity.class);
        intent.putExtra(ORDERR_DETAIL_KEY, detail);
        context.startActivity(intent);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.change_manager_layout);
    }

    @Override
    public void findViews() {
        findViewById(R.id.back_iv).setOnClickListener(this);
        submit_tv = (TextView) findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);
        change_fesat_date_tv = (TextView) findViewById(R.id.change_fesat_date_tv);
        change_fesat_date_tv.setOnClickListener(this);
        loading_pb = (ProgressBar) findViewById(R.id.loading_pb);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDetailChangeMessageEvent(OrderDetailChangeMessageEvent event){
        if(event.getType() == OrderDetailChangeMessageEvent.TYPE.TYPE_FAILURE){
            submit_tv.setEnabled(true);
            loading_pb.setIndeterminate(false);
            loading_pb.setVisibility(View.INVISIBLE);
            change_fesat_date_tv.setEnabled(true);
            Toast.makeText(this, event.getFailureResult(), Toast.LENGTH_SHORT).show();
        }else if(event.getType() == OrderDetailChangeMessageEvent.TYPE.TYPE_SUCCESS){
            OrderDetailActivity.reload(this, detail.id);
        }
    }

    @Override
    public void getData() {
        detail = getIntent().getParcelableExtra(ORDERR_DETAIL_KEY);
    }

    @Override
    public void showContent() {
        if(detail.feastingDate != null || !detail.feastingDate.equals("")){
            change_fesat_date_tv.setText(detail.manager);
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
            case R.id.change_feastDate_bar:
                break;
            case R.id.submit_tv:

                break;
        }
    }
}
