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
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.entity.IdentityMatrix;
import com.iplay.feastbooking.gson.cashBack.OrderCashBackMessageEvent;
import com.iplay.feastbooking.net.utilImpl.cashBack.CashBackUtility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by gu_y-pc on 2017/12/23.
 */

public class OrderCashBackActivity extends BasicActivity implements View.OnClickListener{

    private int orderId;

    private static final String ORDER_ID_KEY = "ORDER_ID_KEY";

    private static final String IDENTITY_MATRIX_KEY = "IDENTITY_MATRIX_KEY";

    private IdentityMatrix matrix;

    private RelativeLayout
            recommender_cash_back_bar,
            manager_cash_back_bar,
            customer_cash_back_bar;

    private TextView
            expected_cash_back_tv,
            customer_cash_back_rate,
            manager_cash_back_rate,
            recommender_cash_back_rate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.order_cash_back_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        recommender_cash_back_bar = (RelativeLayout) findViewById(R.id.recommender_cash_back_bar);
        manager_cash_back_bar = (RelativeLayout) findViewById(R.id.manager_cash_back_bar);
        customer_cash_back_bar = (RelativeLayout) findViewById(R.id.customer_cash_back_bar);

        expected_cash_back_tv = (TextView) findViewById(R.id.expected_cash_back_tv);
        customer_cash_back_rate = (TextView) findViewById(R.id.customer_cash_back_rate);
        manager_cash_back_rate = (TextView) findViewById(R.id.manager_cash_back_rate);
        recommender_cash_back_rate = (TextView) findViewById(R.id.recommender_cash_back_rate);
    }

    @Override
    public void getData() {
        orderId = getIntent().getIntExtra(ORDER_ID_KEY, -1);
        matrix = (IdentityMatrix) getIntent().getSerializableExtra(IDENTITY_MATRIX_KEY);
    }

    public static void start(Context context, int orderId, IdentityMatrix matrix){
        Intent intent = new Intent(context, OrderCashBackActivity.class);
        intent.putExtra(ORDER_ID_KEY, orderId);
        intent.putExtra(IDENTITY_MATRIX_KEY, matrix);
        context.startActivity(intent);
    }

    @Override
    public void showContent() {
        if(matrix.isRecommender()){
            recommender_cash_back_bar.setVisibility(View.VISIBLE);
        }
        if(matrix.isManager()){
            manager_cash_back_bar.setVisibility(View.VISIBLE);
        }
        if(matrix.isCustomer()){
            customer_cash_back_bar.setVisibility(View.VISIBLE);
        }
        updateInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderCashBackMessageEvent(OrderCashBackMessageEvent event){
        expected_cash_back_tv.setText(event.getExpectedAmount(matrix));
        customer_cash_back_rate.setText(event.getPercentageForCustomer());
        manager_cash_back_rate.setText(event.getPercentageForManager());
        recommender_cash_back_rate.setText(event.getPercentageForRecommender());
    }

    private void updateInfo(){
        CashBackUtility.getInstance(this).getCashBackForCertainOrder(orderId, this);
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
}
