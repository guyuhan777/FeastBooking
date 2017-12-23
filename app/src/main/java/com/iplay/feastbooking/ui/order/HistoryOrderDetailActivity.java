package com.iplay.feastbooking.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.assistance.property.OrderStatus;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.entity.IdentityMatrix;
import com.iplay.feastbooking.gson.order.OrderListItem;
import com.iplay.feastbooking.gson.orderDetail.OrderDetail;
import com.iplay.feastbooking.messageEvent.orderdetail.OrderDetailMessageEvent;
import com.iplay.feastbooking.net.utilImpl.orderdetail.OrderDetailUtility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by gu_y-pc on 2017/12/23.
 */

public class HistoryOrderDetailActivity extends BasicActivity implements View.OnClickListener{

    private TextView hotel_title_tv;

    private TextView order_number_tv;

    private TextView order_status_tv;

    private int orderId;

    private IdentityMatrix matrix;

    private static  final String ORDER_ID_KEY = "ORDER_ID_KEY";

    private static final String IDENTITY_MATRIX_KEY = "IDENTITY_MATRIX_KEY";

    private RelativeLayout cash_back_check_detail_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.order_history_detail_layout);
    }

    public static void start(int orderId, IdentityMatrix matrix, Context context){
        Intent intent = new Intent(context, HistoryOrderDetailActivity.class);
        intent.putExtra(ORDER_ID_KEY, orderId);
        intent.putExtra(IDENTITY_MATRIX_KEY, matrix);
        context.startActivity(intent);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        order_status_tv = (TextView) findViewById(R.id.order_status_tv);
        hotel_title_tv = (TextView) findViewById(R.id.hotel_title_tv);
        order_number_tv = (TextView) findViewById(R.id.order_number_tv);
        cash_back_check_detail_bar = (RelativeLayout) findViewById(R.id.cash_back_check_detail_bar);
        cash_back_check_detail_bar.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderDetailMessageEvent(OrderDetailMessageEvent event){
        if (event.getType() == OrderDetailMessageEvent.TYPE_SUCCESS){
            OrderDetail detail = event.getOrderDetail();
            order_number_tv.setText(detail.orderNumber + "");
            hotel_title_tv.setText(detail.hotel);
            order_status_tv.setText(OrderStatus.getOrderStatusCh(detail.orderStatus));
        }
    }

    @Override
    public void getData() {
        orderId = getIntent().getIntExtra(ORDER_ID_KEY, -1);
        matrix = (IdentityMatrix) getIntent().getSerializableExtra(IDENTITY_MATRIX_KEY);
    }

    @Override
    public void showContent() {
        initInfo();
    }

    private void initInfo(){
        OrderDetailUtility.getInstance(this).initOrderDetail(orderId, this);
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
            case R.id.cash_back_check_detail_bar:
                break;
        }
    }
}
