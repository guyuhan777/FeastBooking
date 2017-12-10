package com.iplay.feastbooking.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.assistance.property.OrderStatus;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.component.view.gridview.UnScrollableGridView;
import com.iplay.feastbooking.entity.IdentityMatrix;
import com.iplay.feastbooking.gson.order.OrderListItem;
import com.iplay.feastbooking.gson.orderDetail.OrderDetail;
import com.iplay.feastbooking.messageEvent.orderdetail.OrderDetailMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.orderdetail.OrderDetailUtility;
import com.iplay.feastbooking.ui.contract.ContractManagementActivity;
import com.iplay.feastbooking.ui.order.adapter.OrderCandidateDateBarAdapter;
import com.iplay.feastbooking.ui.payment.PaymentManageActivity;
import com.iplay.feastbooking.ui.review.PostReviewActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Guyuhan on 2017/11/4.
 */

public class OrderDetailActivity extends BasicActivity implements View.OnClickListener{

    private static final String TAG = "OrderDetailActivity";

    private static final String contentKey = "ContentKey";

    private static final String orderIdKey = "orderIdKey";

    private OrderListItem.Content content;

    private OrderDetail detail;

    private RelativeLayout order_detail_loading_rl;

    private LinearLayout order_detail_failure_state_ll;

    private ProgressBar pb;

    private ScrollView order_detail_sv;

    private OrderDetailUtility utility;

    private int orderId;

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

    private RelativeLayout contract_detail_bar;

    private RelativeLayout change_feastDate_bar;

    private ImageView feastDate_forwards_iv;

    private RelativeLayout change_manager_bar;

    private ImageView forwards_manager_iv;

    private RelativeLayout change_recommender_bar;

    private RelativeLayout upload_payment_bar;

    private ImageView forwards_recommender_iv;

    private TextView title_tv;

    private TextView review_tv;

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
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText(content.hotel);
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
        contract_detail_bar = (RelativeLayout) findViewById(R.id.contract_detail_bar);
        contract_detail_bar.setOnClickListener(this);

        upload_payment_bar = (RelativeLayout) findViewById(R.id.payment_evidence_bar);
        upload_payment_bar.setOnClickListener(this);

        change_feastDate_bar = (RelativeLayout) findViewById(R.id.change_feastDate_bar);
        change_feastDate_bar.setOnClickListener(this);
        feastDate_forwards_iv = (ImageView) findViewById(R.id.feastDate_forwards_iv);

        change_manager_bar = (RelativeLayout) findViewById(R.id.change_manager_bar);
        change_manager_bar.setOnClickListener(this);
        forwards_manager_iv = (ImageView) findViewById(R.id.forwards_manager_iv);

        change_recommender_bar = (RelativeLayout) findViewById(R.id.change_recommender_bar);
        change_recommender_bar.setOnClickListener(this);
        forwards_recommender_iv = (ImageView) findViewById(R.id.forwards_recommender_iv);

        review_tv = (TextView) findViewById(R.id.review_tv);
        review_tv.setOnClickListener(this);

        setAccessibleByPrivilege(content.getIdentityMatrix());
    }

    private void setAccessibleByPrivilege(IdentityMatrix identityMatrix){
        if(identityMatrix != null){
            boolean isEnable = identityMatrix.isCustomer();
            int visibleState = identityMatrix.isCustomer()? View.VISIBLE : View.INVISIBLE;
            change_feastDate_bar.setEnabled(isEnable);
            feastDate_forwards_iv.setVisibility(visibleState);
            /*change_recommender_bar.setEnabled(isEnable);
            forwards_recommender_iv.setVisibility(visibleState);*/
            change_manager_bar.setEnabled(isEnable);
            forwards_manager_iv.setVisibility(visibleState);
        }
    }

    @Override
    public void getData() {
        OrderListItem.Content content = (OrderListItem.Content) getIntent().getSerializableExtra(contentKey);
        this.content = content;
        utility = OrderDetailUtility.getInstance(this);
    }

    @Override
    public void showContent() {
        reload(content.id);
    }

    private void reload(int orderId){
        if(!NetProperties.isNetworkConnected(this)){
            order_detail_sv.setVisibility(View.GONE);
            order_detail_loading_rl.setVisibility(View.GONE);
            order_detail_failure_state_ll.setVisibility(View.VISIBLE);
        }else {
            pb.setIndeterminate(true);
            utility.initOrderDetail(orderId, this);
        }
    }

    public static void reload(Context context, int orderId){
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(orderIdKey, orderId);
        context.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderDetailMessageEvent(OrderDetailMessageEvent event){
        order_detail_loading_rl.setVisibility(View.GONE);
        if (event.getType() == OrderDetailMessageEvent.TYPE_FAILURE){
            order_detail_failure_state_ll.setVisibility(View.VISIBLE);
            state_tv.setText(event.getFailureResult());
        }else if (event.getType() == OrderDetailMessageEvent.TYPE_SUCCESS){
            detail = event.getOrderDetail();
            order_detail_sv.setVisibility(View.VISIBLE);
            IdentityMatrix identityMatrix = content.getIdentityMatrix();
            if(identityMatrix != null
                    && !identityMatrix.isCustomer()
                    && !identityMatrix.isManager()){
                findViewById(R.id.forwards_contract_iv).setVisibility(View.INVISIBLE);
                contract_detail_bar.setEnabled(false);
            }
            initComponent(detail);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        orderId = intent.getIntExtra(orderIdKey, -1);
        if(orderId != -1){
            reload(orderId);
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
            order_status_tv.setText(OrderStatus.getOrderStatusCh(detail.orderStatus));
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
            case R.id.contract_detail_bar:
                OrderDetail.OrderContract contract = detail.orderContract;
                ContractManagementActivity.start(this, detail.id,  contract,
                        content.getIdentityMatrix(), detail.orderStatus);
                break;
            case R.id.review_tv:
                PostReviewActivity.start(this, orderId);
                break;
            case R.id.change_manager_bar:
                ChangeManagerActivity.start(this, detail);
                break;
            case R.id.payment_evidence_bar:
                if(detail != null){
                    if(detail.orderStatus == OrderStatus.STATUS_CONSULTING){
                        Toast.makeText(this, "預定酒席后才能顯示支付憑證", Toast.LENGTH_SHORT).show();
                    }else if(detail.manager == null || detail.feastingDate == null){
                        Toast.makeText(this, "請先填寫擺酒日期/經理人信息", Toast.LENGTH_SHORT).show();
                    }else if(detail.orderStatus.equals(OrderStatus.STATUS_CONSULTING)){
                        Toast.makeText(this, "尚處于資訊中狀態", Toast.LENGTH_SHORT).show();
                    }else {
                        OrderDetail.OrderPayment payment = detail.orderPayment;
                        PaymentManageActivity.start(this, detail.id, payment,
                                content.getIdentityMatrix(), detail.orderStatus);
                    }
                }
                break;
            case R.id.change_feastDate_bar:
                ChangeFeastDateActivity.start(this, detail);
                break;
        }
    }
}
