package com.iplay.feastbooking.ui.order.delegate;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.entity.IdentityMatrix;
import com.iplay.feastbooking.gson.order.OrderListItem;
import com.iplay.feastbooking.ui.order.OrderDetailActivity;
import com.iplay.feastbooking.ui.order.data.OrderItemData;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.timeline.data.TimeLineNode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Guyuhan on 2017/10/29.
 */

public class OrderItemAdapterDelegate extends AdapterDelegate<List<BasicData>> {

    private WeakReference<Activity> activityWF;

    private LayoutInflater inflater;

    public OrderItemAdapterDelegate(Activity activity) {
        inflater = LayoutInflater.from(activity);
        activityWF = new WeakReference<>(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicData> items, int position) {
        return items.get(position) instanceof OrderItemData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new OrderItemViewHolder(inflater.inflate(R.layout.order_time_line_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        OrderItemData data = (OrderItemData) items.get(position);
        OrderListItem.Content content = data.getContent();
        IdentityMatrix identityMatrix;
        if (content == null || (identityMatrix=content.getIdentityMatrix()) == null){
            return;
        }

        OrderItemViewHolder orderItemVH = (OrderItemViewHolder) holder;
        if(identityMatrix.isCustomer()){
            orderItemVH.table_num_tv.setText(content.tables + "桌");
            orderItemVH.order_date_tv.setText((content.date == null || content.date.equals(""))? "暫無日期" : content.date);
        }else{
            orderItemVH.order_date_tv.setTextColor(activityWF.get().getResources().getColor(R.color.pink));
            if(identityMatrix.isManager()){
                if(!identityMatrix.isRecommender()){
                    orderItemVH.order_date_tv.setText("經理人");
                }else {
                    orderItemVH.order_date_tv.setText("經理人/推介人");
                }
            }else{
                orderItemVH.order_date_tv.setText("推介人");
                orderItemVH.forwards_iv.setVisibility(View.GONE);
            }
        }
        Log.d("data", content.toString());
        orderItemVH.customer_name_tv.setText(content.contact);
        orderItemVH.hotel_name_tv.setText(content.hotel);
        orderItemVH.banquet_halls_tv.setText(content.banquetHall);
        LinearLayoutManager manager = new LinearLayoutManager(activityWF.get(), LinearLayoutManager.HORIZONTAL, false);
        orderItemVH.time_line_rv.setLayoutManager(manager);
        orderItemVH.time_line_rv.setAdapter(new OrderTimeLineAdapter(content.orderStatus));
        orderItemVH.root_view_ll.setOnClickListener(new OrderItemOnclickListener(data, activityWF.get()));
    }

    private static class OrderItemOnclickListener implements View.OnClickListener{

        private final OrderItemData orderItemData;

        private final Context context;

        OrderItemOnclickListener(OrderItemData orderItemData, Context context){
            this.orderItemData = orderItemData;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            OrderListItem.Content content = orderItemData.getContent();
            IdentityMatrix identityMatrix;
            if (content != null && (identityMatrix = content.getIdentityMatrix()) != null
                    && (identityMatrix.isCustomer() || identityMatrix.isManager())) {
                OrderDetailActivity.start(context, orderItemData.getContent());
            }
        }
    }

    private static class OrderTimeLineAdapter extends RecyclerView.Adapter{

        private List<TimeLineNode> timeLineNodes;

        private static String status[] = new String[]{"CONSULTING", "RESERVED", "FEASTED", "CASHBACK", "TO_BE_REVIEWD"};

        private static HashMap<String,String> statusMap;

        private static final int TYPE_ACTIVE = 1;

        private static final int TYPE_INACTIVE = 2;

        static {
            statusMap = new HashMap<>();
            statusMap.put("CONSULTING","咨詢中");
            statusMap.put("RESERVED", "已預訂");
            statusMap.put("FEASTED", "已擺酒");
            statusMap.put("CASHBACK", "等待返現");
            statusMap.put("TO_BE_REVIEWD", "待評價");
        }

       OrderTimeLineAdapter(String statue) {
           timeLineNodes = new ArrayList<>();
            boolean isTailReached = false;
            for(int i=0; i<status.length; i++){
                TimeLineNode node = new TimeLineNode();
                node.setDescribe(statusMap.get(status[i]));
                if(i==0){
                    node.setHead(true);
                }
                if(!isTailReached){
                    node.setActive(true);
                }
                if(statue.equals(status[i])){
                    isTailReached = true;
                }
                timeLineNodes.add(node);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TimeLineNodeViewHolder holder = null;
            if(viewType == TYPE_ACTIVE){
                holder = new TimeLineNodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.active_time_line_item, parent, false));
            }else if(viewType == TYPE_INACTIVE){
                holder = new TimeLineNodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.time_line_item, parent, false));
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TimeLineNode node = timeLineNodes.get(position);
            TimeLineNodeViewHolder timeLineNodeVH = (TimeLineNodeViewHolder) holder;
            if(node.isHead()){
                timeLineNodeVH.node_left_line.setVisibility(View.INVISIBLE);
            }
            timeLineNodeVH.state_tv.setText(node.getDescribe());
        }

        @Override
        public int getItemViewType(int position) {
            TimeLineNode node = timeLineNodes.get(position);
            return node.isActive() ? TYPE_ACTIVE : TYPE_INACTIVE;
        }

        @Override
        public int getItemCount() {
            return timeLineNodes.size();
        }
    }

    private static class TimeLineNodeViewHolder extends RecyclerView.ViewHolder{

        private View node_left_line;

        private TextView state_tv;

        TimeLineNodeViewHolder(View itemView) {
            super(itemView);
            node_left_line = itemView.findViewById(R.id.node_left_line);
            state_tv = (TextView) itemView.findViewById(R.id.state_tv);
        }
    }

    private static class OrderItemViewHolder extends RecyclerView.ViewHolder{

        private  TextView banquet_halls_tv;

        private TextView order_date_tv;

        private TextView hotel_name_tv;

        private TextView table_num_tv;

        private ImageView forwards_iv;

        private RecyclerView time_line_rv;

        private LinearLayout root_view_ll;

        private TextView customer_name_tv;

        OrderItemViewHolder(View itemView) {
            super(itemView);
            banquet_halls_tv = (TextView) itemView.findViewById(R.id.banquet_halls_tv);
            order_date_tv = (TextView) itemView.findViewById(R.id.order_date_tv);
            hotel_name_tv = (TextView) itemView.findViewById(R.id.hotel_name_tv);
            table_num_tv = (TextView) itemView.findViewById(R.id.table_num_tv);
            forwards_iv = (ImageView) itemView.findViewById(R.id.forwards_iv);
            time_line_rv = (RecyclerView) itemView.findViewById(R.id.time_line_rv);
            customer_name_tv = (TextView) itemView.findViewById(R.id.customer_name_tv);
            root_view_ll = (LinearLayout) itemView.findViewById(R.id.order_list_item_root_view);
        }
    }
}
