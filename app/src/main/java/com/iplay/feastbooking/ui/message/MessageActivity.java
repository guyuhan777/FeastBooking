package com.iplay.feastbooking.ui.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.messageEvent.notification.MessageStatusChangedMessageEvent;
import com.iplay.feastbooking.net.utilImpl.jMessage.JMessageUtility;
import com.iplay.feastbooking.ui.message.adapter.MessageAdapter;
import com.iplay.feastbooking.ui.message.data.BasicMessage;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by gu_y-pc on 2018/1/13.
 */

public class MessageActivity extends BasicActivity implements View.OnClickListener{

    private RecyclerView message_rv;

    private TextView title_tv;

    private MessageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.message_management_layout);
    }

    public static void start(Context context){
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        List<BasicData> datas = new ArrayList<>();
        findViewById(R.id.back_iv).setOnClickListener(this);
        title_tv = (TextView) findViewById(R.id.title_tv);
        message_rv = (RecyclerView) findViewById(R.id.message_rv);
        List<BasicMessage> messages = JMessageUtility.getInstance(this).getAllMessages();
        datas.addAll(messages);
        adapter = new MessageAdapter(datas, this);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        message_rv.setLayoutManager(manager);
        message_rv.setAdapter(adapter);
        refreshMessageTitle();
    }

    private void refreshMessageTitle(){
        int unreadCount = JMessageUtility.getInstance(this).getUnreadMessageCount();
        if(unreadCount > 0){
            title_tv.setText(getText(R.string.message_manage_title) + "(" + unreadCount + ")");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageStatusChangedMessageEvent(MessageStatusChangedMessageEvent event){
        refreshMessageTitle();
    }

    public void onEventMainThread(MessageEvent event){
        Message message = event.getMessage();
        if(adapter != null && BasicMessage.isMessageValid(message)){
            adapter.addMessage(message);
            refreshMessageTitle();
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
