package com.iplay.feastbooking.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.component.view.gridview.UnScrollableGridView;
import com.iplay.feastbooking.gson.order.OrderListItem;
import com.iplay.feastbooking.gson.orderDetail.OrderDetail;
import com.iplay.feastbooking.messageEvent.orderdetail.OrderDetailMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.orderdetail.OrderDetailUtility;
import com.iplay.feastbooking.ui.order.adapter.OrderCandidateDateBarAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Guyuhan on 2017/11/4.
 */

public class OrderDetailActivity extends BasicActivity implements View.OnClickListener{

    private static final String TAG = "OrderDetailActivity";

    private static final String contentKey = "ContentKey";

    private OrderListItem.Content content;

    private RelativeLayout order_detail_loading_rl;

    private LinearLayout order_detail_failure_state_ll;

    private ProgressBar pb;

    private ScrollView order_detail_sv;

    private OrderDetailUtility utility;

    private TextView state_tv;

    private TextView table_num_tv;

    private TextView link_man_tv;

    private TextView tel_tv;

    private UnScrollableGridView candidate_gv;

    private TextView feasting_date_tv;

    private TextView recommender_tv;

    private TextView manager_tv;

    private TextView order_number_tv;

    private TextView order_status_tv;

    private TextView banquetHall_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    public static void start(Context context, OrderListItem.Content content){
        Intent intent = new Intent(context,OrderDetailActivity.class);
        intent.putExtra(contentKey, content);
        context.startActivity(intent);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.order_detail_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        ((TextView) findViewById(R.id.title_tv)).setText(content.hotel);
        order_detail_loading_rl = (RelativeLayout) findViewById(R.id.order_detail_loading_rl);
        order_detail_failure_state_ll = (LinearLayout) findViewById(R.id.order_detail_no_internet_ll);
        order_detail_sv = (ScrollView) findViewById(R.id.order_detail_sv);
        pb = (ProgressBar) findViewById(R.id.refresh_progress_bar);
        state_tv = (TextView) findViewById(R.id.state_tv);
        order_number_tv = (TextView) findViewById(R.id.order_number_tv);
        order_status_tv = (TextView) findViewById(R.id.order_status_tv);
        banquetHall_tv = (TextView) findViewById(R.id.banquet_halls_tv);
        table_num_tv = (TextView) findViewById(R.id.table_num_tv);
        link_man_tv = (TextView) findViewById(R.id.link_man_tv);
        tel_tv = (TextView) findViewById(R.id.tel_tv);
        feasting_date_tv = (TextView) findViewById(R.id.feasting_date_tv);
        recommender_tv = (TextView) findViewById(R.id.recommender_tv);
        manager_tv = (TextView) findViewById(R.id.manager_tv);
        candidate_gv = (UnScrollableGridView) findViewById(R.id.candidate_gv);
    }

    @Override
    public void getData() {
        OrderListItem.Content content = (OrderListItem.Content) getIntent().getSerializableExtra(contentKey);
        this.content = content;
        utility = OrderDetailUtility.getInstance(this);
    }

    @Override
    public void showContent() {
        if(!NetProperties.isNetworkConnected(this)){
            order_detail_loading_rl.setVisibility(View.GONE);
            order_detail_failure_state_ll.setVisibility(View.VISIBLE);
        }else {
            pb.setIndeterminate(true);
            utility.initOrderDetail(content.id, this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderDetailMessageEvent(OrderDetailMessageEvent event){
        order_detail_loading_rl.setVisibility(View.GONE);
        if (event.getType() == OrderDetailMessageEvent.TYPE_FAILURE){
            order_detail_failure_state_ll.setVisibility(View.VISIBLE);
            state_tv.setText(event.getFailureResult());
        }else if (event.getType() == OrderDetailMessageEvent.TYPE_SUCCESS){
            order_detail_sv.setVisibility(View.VISIBLE);
            initComponent(event.getOrderDetail());
        }
    }

    private void initComponent(OrderDetail detail){
        if(detail != null){
            table_num_tv.setText(detail.tables + "桌");
            if(detail.contact != null){
                link_man_tv.setText(detail.contact);
            }
            if(detail.manager != null){
                manager_tv.setText(detail.manager);
            }
            if(detail.recommender != null){
                recommender_tv.setText(detail.recommender);
            }
            if(detail.feastingDate != null){
                feasting_date_tv.setText(detail.feastingDate);
            }
            order_number_tv.setText(detail.orderNumber + "");
            banquetHall_tv.setText(detail.banquetHall);
            order_status_tv.setText(WindowAttr.getOrderStatusCh(detail.orderStatus));
            tel_tv.setText(detail.phone);
            OrderCandidateDateBarAdapter adapter = new OrderCandidateDateBarAdapter(this, R.layout.order_candidate_bar, detail.candidateDates);
            candidate_gv.setAdapter(adapter);
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
        }
    }
}
