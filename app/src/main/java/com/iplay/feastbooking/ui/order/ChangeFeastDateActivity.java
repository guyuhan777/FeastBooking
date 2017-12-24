package com.iplay.feastbooking.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.DateFormatter;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.assistance.property.OrderStatus;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.gson.orderDetail.OrderDetail;
import com.iplay.feastbooking.messageEvent.orderdetail.OrderDetailChangeMessageEvent;
import com.iplay.feastbooking.net.utilImpl.orderdetail.OrderDetailUtility;
import com.iplay.feastbooking.ui.consult.TimerPickerDialog;
import com.squareup.timessquare.CalendarPickerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

/**
 * Created by Guyuhan on 2017/11/18.
 */

public class ChangeFeastDateActivity extends BasicActivity implements View.OnClickListener{

    private OrderDetail detail;

    private static final String ORDERR_DETAIL_KEY = "order_detail_key";

    private TextView change_fesat_date_tv;

    private TextView submit_tv;

    private TextView title_tv;

    private ProgressBar loading_pb;

    private TimerPickerDialog dialog;

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
        setContentView(R.layout.change_feastdate_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        title_tv = (TextView) findViewById(R.id.title_tv);
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
        detail = (OrderDetail) getIntent().getSerializableExtra(ORDERR_DETAIL_KEY);
    }

    @Override
    public void showContent() {
        if(detail.feastingDate != null && !detail.feastingDate.equals("")){
            change_fesat_date_tv.setText(detail.feastingDate);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDateReceived(List<Date> dates){
        if(dates != null && dates.size() == 1){
            String dateStr = DateFormatter.formatDate(dates.get(0));
            change_fesat_date_tv.setText(dateStr);
            submit_tv.setEnabled(true);
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
            case R.id.change_fesat_date_tv:
                if(dialog == null){
                    dialog = new TimerPickerDialog(this, CalendarPickerView.SelectionMode.SINGLE);
                    dialog.show();
                }
                break;
            case R.id.submit_tv:
                if(change_fesat_date_tv.getText().toString().trim().equals("")){
                    Toast.makeText(this, "尚未設置日期", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    String fesatDate = change_fesat_date_tv.getText().toString().trim();
                    if(fesatDate.equals("")){
                        return;
                    }
                    loading_pb.setIndeterminate(true);
                    loading_pb.setVisibility(View.VISIBLE);
                    change_fesat_date_tv.setEnabled(false);
                    submit_tv.setEnabled(false);
                    OrderDetailUtility.getInstance(this).changeFeastDate(this, fesatDate, detail.id);
                }
                break;
        }
    }
}
