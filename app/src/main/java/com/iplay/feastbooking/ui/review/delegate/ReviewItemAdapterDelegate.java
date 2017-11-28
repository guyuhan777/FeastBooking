package com.iplay.feastbooking.ui.review.delegate;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.component.view.bar.RatingBar;
import com.iplay.feastbooking.ui.order.data.basic.BasicData;
import com.iplay.feastbooking.ui.review.data.ReviewData;

import java.lang.ref.WeakReference;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gu_y-pc on 2017/11/28.
 */

public class ReviewItemAdapterDelegate extends AdapterDelegate<List<BasicData>> {

    private WeakReference<Activity> activityWF;

    private LayoutInflater inflater;

    public ReviewItemAdapterDelegate(Activity activity) {
        inflater = LayoutInflater.from(activity);
        activityWF = new WeakReference<>(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicData> items, int position) {
        return items.get(position) instanceof ReviewData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ReviewItemViewHolder(inflater.inflate(R.layout.hotel_review_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ReviewData reviewData = (ReviewData) items.get(position);
        ReviewItemViewHolder orderItemVH = (ReviewItemViewHolder) holder;
        Activity activity = activityWF.get();
        if(activity != null){
            Glide.with(activity).load(R.drawable.shenzi).into(orderItemVH.portrait);
        }
        orderItemVH.date_tv.setText(reviewData.getReviewTime());
        orderItemVH.hotel_rate_bar.setRate(reviewData.getRating());
        orderItemVH.remark_tv.setText(reviewData.getReview());
        orderItemVH.reviewer_name_tv.setText(reviewData.getAuthor());
    }


    private static class ReviewItemViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView portrait;

        private TextView reviewer_name_tv;

        private RatingBar hotel_rate_bar;

        private TextView remark_tv;

        private TextView date_tv;

        ReviewItemViewHolder(View itemView) {
            super(itemView);
            portrait = (CircleImageView)itemView.findViewById(R.id.reviewer_portrait_civ);
            reviewer_name_tv = (TextView) itemView.findViewById(R.id.reviewer_name_tv);
            hotel_rate_bar = (RatingBar) itemView.findViewById(R.id.hotel_rate_bar);
            remark_tv = (TextView) itemView.findViewById(R.id.remark_tv);
            date_tv = (TextView) itemView.findViewById(R.id.date_tv);
        }
    }
}
