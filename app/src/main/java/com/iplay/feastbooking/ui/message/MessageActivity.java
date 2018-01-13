package com.iplay.feastbooking.ui.message;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ScrollingView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.net.utilImpl.jMessage.JMessageUtility;
import com.iplay.feastbooking.ui.message.adapter.MessageAdapter;
import com.iplay.feastbooking.ui.message.data.BasicMessage;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.recommendedHotel.data.AllLoadedHomeData;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by gu_y-pc on 2018/1/13.
 */

public class MessageActivity extends BasicActivity implements View.OnClickListener{

    private RecyclerView message_sv;

    private TextView title_tv;

    @Override
    public void setContentView() {
        setContentView(R.layout.message_management_layout);
    }

    public static void start(Context context){
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        List<BasicData> datas = new ArrayList<>();
        findViewById(R.id.back_iv).setOnClickListener(this);
        title_tv = (TextView) findViewById(R.id.title_tv);
        message_sv = (RecyclerView) findViewById(R.id.message_rv);
        List<Message> messages = JMessageUtility.getInstance(this).getAllMessages();
        for(Message message : messages){
            if(message != null){
                BasicMessage basicMessage = new BasicMessage();
                basicMessage.setMessage(message);
                datas.add(basicMessage);
            }
        }
        MessageAdapter adapter = new MessageAdapter(datas, this);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        message_sv.setLayoutManager(manager);
        message_sv.setAdapter(adapter);
        int unreadCount = JMessageUtility.getInstance(this).getUnreadMessageCount();
        if(unreadCount >= 0){
            title_tv.setText(getText(R.string.message_manage_title) + "(" + unreadCount + ")");
        }
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back_iv){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            });
        }
    }
}
