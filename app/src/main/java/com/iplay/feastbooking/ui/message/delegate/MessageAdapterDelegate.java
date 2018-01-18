package com.iplay.feastbooking.ui.message.delegate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.DateFormatter;
import com.iplay.feastbooking.ui.message.adapter.MessageAdapter;
import com.iplay.feastbooking.ui.message.data.BasicMessage;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by gu_y-pc on 2018/1/13.
 */

public class MessageAdapterDelegate extends AdapterDelegate<List<BasicData>> {

    private WeakReference<Context> contextWeakReference;

    private LayoutInflater inflater;

    private MessageAdapter adapter;

    public MessageAdapterDelegate(Context context, MessageAdapter adapter) {
        super();
        this.contextWeakReference = new WeakReference<>(context);
        inflater = LayoutInflater.from(context);
        this.adapter = adapter;
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicData> items, int position) {
        return items.get(position) instanceof BasicMessage;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MessageViewHolder(inflater.inflate(R.layout.message_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        final BasicMessage basicMessage = (BasicMessage) items.get(position);
        final Message message = basicMessage.getMessage();
        MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
        if(message.haveRead()){
            messageViewHolder.red_dot.setVisibility(View.INVISIBLE);
        }else {
            messageViewHolder.red_dot.setVisibility(View.VISIBLE);
        }
        MessageContent content;
        if((content = message.getContent()) != null
                && content.getContentType() == ContentType.text){
            TextContent textContent = (TextContent) content;
            messageViewHolder.content_tv.setText(textContent.getText());
            UserInfo poster = message.getFromUser();
            if(poster != null){
                messageViewHolder.title_tv.setText(poster.getNickname());
            }
            Date createdTime = new Date(message.getCreateTime());
            messageViewHolder.date_tv.setText(DateFormatter.formatDate(createdTime));
        }
        messageViewHolder.delete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null){
                    adapter.removeMessage(basicMessage);
                }
            }
        });
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout red_dot;

        private TextView title_tv;

        private TextView date_tv;

        private TextView content_tv;

        private TextView delete_tv;

        MessageViewHolder(View itemView) {
            super(itemView);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            red_dot = (LinearLayout) itemView.findViewById(R.id.red_dot);
            date_tv = (TextView) itemView.findViewById(R.id.date_tv);
            content_tv = (TextView) itemView.findViewById(R.id.content_tv);
            delete_tv = (TextView) itemView.findViewById(R.id.btnDelete);
        }
    }
}
