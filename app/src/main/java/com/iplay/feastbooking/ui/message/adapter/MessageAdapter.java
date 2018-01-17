package com.iplay.feastbooking.ui.message.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;
import com.iplay.feastbooking.ui.message.data.BasicMessage;
import com.iplay.feastbooking.ui.message.delegate.MessageAdapterDelegate;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by gu_y-pc on 2018/1/13.
 */

public class MessageAdapter extends RecyclerView.Adapter {

    private List<BasicData> messageBasics;

    private AdapterDelegatesManager<List<BasicData>> delegatesManager;

    private WeakReference<Context> contextWeakReference;

    public MessageAdapter(List<BasicData> messageBasics, Context context) {
        super();
        this.messageBasics = messageBasics;
        this.contextWeakReference = new WeakReference<>(context);
        delegatesManager = new AdapterDelegatesManager<>();
        delegatesManager.addDelegate(new MessageAdapterDelegate(context));
    }

    public void addMessage(Message message){
        BasicMessage basicMessage = new BasicMessage();
        basicMessage.setMessage(message);
        messageBasics.add(0, basicMessage);
        notifyItemChanged(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(messageBasics, position, holder);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(messageBasics, position);
    }

    @Override
    public int getItemCount() {
        return messageBasics == null ? 0 : messageBasics.size();
    }
}
