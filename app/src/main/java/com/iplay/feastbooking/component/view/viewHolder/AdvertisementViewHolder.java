package com.iplay.feastbooking.component.view.viewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

/**
 * Created by admin on 2017/9/6.
 */

public class AdvertisementViewHolder extends BasicViewHolder {

    private RollPagerView rollPagerView;

    public AdvertisementViewHolder(View itemView) {
        super(itemView);
        rollPagerView = (RollPagerView) itemView.findViewById(R.id.ads_roll_pager);
        rollPagerView.setAdapter(new AdsLoopAdapter(rollPagerView));
    }

    static class AdsLoopAdapter extends LoopPagerAdapter {
        private int[] images = {
                R.drawable.ad,
                R.drawable.ad2,
                R.drawable.ad3
        };

        public AdsLoopAdapter(RollPagerView view){
            super(view);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(container.getContext()).load(images[position]).into(imageView);
            return imageView;
        }

        @Override
        public int getRealCount() {
            return images.length;
        }
    }
}
