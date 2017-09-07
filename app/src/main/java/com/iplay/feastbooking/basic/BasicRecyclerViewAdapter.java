package com.iplay.feastbooking.basic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.component.view.viewHolder.AdvertisementViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.HotelRecyclerItemViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.ProgressViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.RecommendHotelGridViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.TitleViewHolder;
import com.iplay.feastbooking.entity.Hotel;

import java.util.List;

/**
 * Created by admin on 2017/9/6.
 */

public class BasicRecyclerViewAdapter extends RecyclerView.Adapter<BasicViewHolder> {

    public static final int TYPE_HEADER = 0;

    public static final int TYPE_GRID = 1;

    public static final int TYPE_FOOTER = 2;

    public static final int TYPE_NORMAL = 3;

    public static final int TYPE_TITLE_RECOMMEND = 4;

    public static final int TYPE_TITLE_ALL = 5;

    private List<Hotel> hotels;

    private View mHeaderView;

    public void setmGridView(View mGridView) {
        this.mGridView = mGridView;
        notifyItemInserted(2);
    }

    private View mGridView;

    private View mFooterView;

    public void setTitle_all(View title_all) {
        this.title_all = title_all;
        notifyItemInserted(3);
    }

    public void setTitle_recommend(View title_recommend) {
        this.title_recommend = title_recommend;
        notifyItemInserted(1);
    }

    private View title_recommend;

    private View title_all;

    private Context mContext;

    public BasicRecyclerViewAdapter(Context context){
        mContext = context;
    }

    public void setData(List<Hotel> hotels){
        this.hotels = hotels;
    }

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        notifyItemInserted(0);
    }

    public void setmFooterView(View mFooterView) {
        this.mFooterView = mFooterView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null && mFooterView == null && mGridView == null){
            return TYPE_NORMAL;
        }
        if(position == 0){
            return TYPE_HEADER;
        }
        if(position == 1){
            return TYPE_TITLE_RECOMMEND;
        }
        if(position == 2){
            return TYPE_GRID;
        }
        if(position == 3){
            return TYPE_TITLE_ALL;
        }
        if(hotels.get(position-4) == null){
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView!=null && viewType == TYPE_HEADER){
            return new AdvertisementViewHolder(mHeaderView);
        }
        if(title_recommend!=null && viewType == TYPE_TITLE_RECOMMEND){
            return new TitleViewHolder(title_recommend);
        }
        if(mGridView!=null && viewType == TYPE_GRID){
            return new RecommendHotelGridViewHolder(mGridView);
        }
        if(title_all!=null && viewType == TYPE_TITLE_ALL){
            return new TitleViewHolder(title_all);
        }
        if(mFooterView!=null && viewType == TYPE_FOOTER){
            return new ProgressViewHolder(mFooterView);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_recommend_item,parent,false);
        return new HotelRecyclerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BasicViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof HotelRecyclerItemViewHolder){
                HotelRecyclerItemViewHolder hrHolder = (HotelRecyclerItemViewHolder) holder;
                Hotel hotel = hotels.get(position-4);
                Glide.with(mContext).load(hotel.icon_id).into(hrHolder.hotel_icon_iv);
                hrHolder.hotel_name.setText(hotel.hotel_name);
            }
        }else if(getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_GRID){
            return;
        }else if(getItemViewType(position) == TYPE_TITLE_RECOMMEND){
            ((TitleViewHolder) holder).setText(mContext.getResources().getString(R.string.home_page_recommend));
            return;
        }else if(getItemViewType(position) == TYPE_TITLE_ALL){
            ((TitleViewHolder) holder).setText(mContext.getResources().getString(R.string.home_page_hotel_all));
            return;
        } else if(holder instanceof ProgressViewHolder){
            ((ProgressViewHolder) holder).pb.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return hotels == null ?4:hotels.size()+4;
    }
}
