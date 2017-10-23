package com.iplay.feastbooking.ui.recommendedHotel.delegates;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.ui.recommendedHotel.data.AdvertisementHomeData;
import com.iplay.feastbooking.ui.recommendedHotel.data.basic.BasicHomeData;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class AdvertisementAdapterDelegate extends AdapterDelegate<List<BasicHomeData>> {

    private LayoutInflater inflater;

    public AdvertisementAdapterDelegate(AppCompatActivity activity){
        inflater = LayoutInflater.from(activity);
    }

    @Override
    protected boolean isForViewType(@NonNull List<BasicHomeData> items, int position) {
        return items.get(position) instanceof AdvertisementHomeData;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new AdvertisementViewHolder(inflater.inflate(R.layout.advertisement_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<BasicHomeData> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        AdvertisementHomeData data = (AdvertisementHomeData) items.get(position);
        RollPagerView rollPagerView = ((AdvertisementViewHolder) holder).rollPagerView;
        rollPagerView.setAdapter(new AdvertisementLooperAdapter(rollPagerView, data.getAdvertisements()));
    }

    private static class AdvertisementViewHolder extends RecyclerView.ViewHolder{

        RollPagerView rollPagerView;

        AdvertisementViewHolder(View itemView) {
            super(itemView);
            rollPagerView = (RollPagerView) itemView.findViewById(R.id.ads_roll_pager);
        }
    }

    private static class AdvertisementLooperAdapter extends LoopPagerAdapter {

        List<Advertisement> advertisements;

        AdvertisementLooperAdapter(RollPagerView view, List<Advertisement> advertisements){
            super(view);
            this.advertisements = advertisements;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(imageView);
            ImageView view = imageViewWeakReference.get();
            Glide.with(container.getContext()).load(Uri.parse(advertisements.get(position).getUrl())).placeholder(R.drawable.loading).into(view);
            return imageView;
        }

        @Override
        public int getRealCount() {
            return advertisements.size();
        }
    }
}
