package com.iplay.feastbooking.component.view.viewHolder;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;
import com.iplay.feastbooking.entity.Advertisement;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by admin on 2017/9/6.
 */

public class AdvertisementViewHolder extends BasicViewHolder {

    private RollPagerView rollPagerView;

    private List<Advertisement> advertisements;

    public AdvertisementViewHolder(View itemView) {
        super(itemView);
        rollPagerView = (RollPagerView) itemView.findViewById(R.id.ads_roll_pager);
    }

    public void initAdapter(List<Advertisement> advertisements){
        this.advertisements = advertisements;
        rollPagerView.setAdapter(new AdsLoopAdapter(rollPagerView));
    }

    private class AdsLoopAdapter extends LoopPagerAdapter {

        public AdsLoopAdapter(RollPagerView view){
            super(view);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Advertisement advertisement = advertisements.get(position);
            WeakReference<ImageView> imageViewWeakReference = new WeakReference<ImageView>(imageView);
            ImageView view = imageViewWeakReference.get();
            Glide.with(container.getContext()).load(Uri.parse(advertisement.getUrl())).placeholder(R.drawable.ad).into(view);
            return imageView;
        }

        @Override
        public int getRealCount() {
            return advertisements.size();
        }
    }
}
