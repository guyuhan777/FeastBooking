package com.iplay.feastbooking.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.gson.orderDetail.OrderDetail;
import com.iplay.feastbooking.messageEvent.orderdetail.OrderDetailChangeMessageEvent;
import com.iplay.feastbooking.net.utilImpl.orderdetail.OrderDetailUtility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Guyuhan on 2017/11/18.
 */

public class ChangeManagerActivity extends BasicActivity implements View.OnClickListener{

    private OrderDetail detail;

    private static final String ORDERR_DETAIL_KEY = "order_detail_key";

    private EditText manager_et;

    private TextView submit_tv;

    private ProgressBar loading_pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    public static void start(Context context, OrderDetail detail){
        Intent intent = new Intent(context, ChangeManagerActivity.class);
        intent.putExtra(ORDERR_DETAIL_KEY, detail);
        context.startActivity(intent);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.change_manager_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        submit_tv = (TextView) findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);
        manager_et = (EditText) findViewById(R.id.manager_et);
        loading_pb = (ProgressBar) findViewById(R.id.loading_pb);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDetailChangeMessageEvent(OrderDetailChangeMessageEvent event){
        if(event.getType() == OrderDetailChangeMessageEvent.TYPE.TYPE_FAILURE){
            submit_tv.setEnabled(true);
            loading_pb.setIndeterminate(false);
            loading_pb.setVisibility(View.INVISIBLE);
            manager_et.setEnabled(true);
            Toast.makeText(this, event.getFailureResult(), Toast.LENGTH_SHORT).show();
        }else if(event.getType() == OrderDetailChangeMessageEvent.TYPE.TYPE_SUCCESS){
            OrderDetailActivity.reload(this, detail.id);
        }
    }

    @Override
    public void getData() {
        detail = (OrderDetail) getIntent().getSerializableExtra(ORDERR_DETAIL_KEY);
    }

    @Override
    public void showContent() {
        if(detail.manager != null && !detail.manager.equals("")){
            manager_et.setText(detail.manager);
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
            case R.id.submit_tv:
                String username = manager_et.getText().toString().trim();
                if(username.equals("")){
                    Toast.makeText(this, "尚未設置經理人", Toast.LENGTH_SHORT).show();
                    return;
                }
                loading_pb.setIndeterminate(true);
                loading_pb.setVisibility(View.VISIBLE);
                manager_et.setEnabled(false);
                submit_tv.setEnabled(false);
                OrderDetailUtility.getInstance(this).changeManager(this, username, detail.id);
                break;
        }
    }
}