package com.iplay.feastbooking.ui.hotelDetail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;
import com.iplay.feastbooking.entity.Hotel;
import com.iplay.feastbooking.entity.HotelFeast;
import com.iplay.feastbooking.entity.HotelRoom;
import com.iplay.feastbooking.ui.feast.FeastActivity;
import com.iplay.feastbooking.ui.hotelDetail.viewHolder.HotelFeastTitleViewHolder;
import com.iplay.feastbooking.ui.hotelDetail.viewHolder.HotelFeastViewHolder;
import com.iplay.feastbooking.ui.hotelDetail.viewHolder.HotelFixedDescribeViewHolder;
import com.iplay.feastbooking.ui.hotelDetail.viewHolder.HotelRoomDescribeViewHolder;
import com.iplay.feastbooking.ui.hotelDetail.viewHolder.HotelRoomTitleViewHolder;

/**
 * Created by admin on 2017/9/12.
 */

public class HotelDetailAdapter extends RecyclerView.Adapter<BasicViewHolder> {

    public static final int HOTEL_FIXED_DESRIBE = 0;

    public static final int HOTEL_ROOM_TITLE = 1;

    public static final int HOTEL_ROOM_DESCRIBE = 2;

    public static final int HOTEL_FEAST_TITLE = 3;

    public static final int HOTEL_FEAST = 4;

    private Context mContext;

    public HotelDetailAdapter(Context context){
        mContext = context;
    }

    public void setHotel(Hotel hotel){
        this.hotel = hotel;
    }

    public Hotel hotel;

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HOTEL_FIXED_DESRIBE){
            return new HotelFixedDescribeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.hotel_fixed_describe,parent,false));
        }
        if(viewType == HOTEL_ROOM_TITLE){
            return new HotelRoomTitleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.room_list_title,parent,false));
        }
        if(viewType == HOTEL_ROOM_DESCRIBE){
            return new HotelRoomDescribeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.room_desribe,parent,false));
        }
        if(viewType == HOTEL_FEAST_TITLE){
            return new HotelFeastTitleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.feast_list_title,parent,false));
        }
        if(viewType == HOTEL_FEAST){
            return new HotelFeastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.feast_detail_check,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BasicViewHolder holder, int position) {
        if(getItemViewType(position) == HOTEL_FIXED_DESRIBE ){
            HotelFixedDescribeViewHolder hfdvh = (HotelFixedDescribeViewHolder) holder;
            Glide.with(mContext).load(R.drawable.bh1 ).into(hfdvh.icon);
        }
        if(getItemViewType(position) == HOTEL_ROOM_DESCRIBE){
            HotelRoomDescribeViewHolder hrdvh = (HotelRoomDescribeViewHolder) holder;
            HotelRoom hotelRoom = hotel.rooms.get(position-2);
            Glide.with(mContext).load(hotelRoom.icon_id).into(hrdvh.hotel_room_icon);
        }
        if(getItemViewType(position) == HOTEL_FEAST){
            int index = position - 3 - hotel.rooms.size();
            final HotelFeast feast = hotel.feasts.get(index);
            HotelFeastViewHolder hfvh = (HotelFeastViewHolder) holder;
            hfvh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeastActivity.start(mContext,feast);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HOTEL_FIXED_DESRIBE;
        }
        if(position == 1){
            return HOTEL_ROOM_TITLE;
        }
        if(position >= 2 && position < 2 + hotel.rooms.size()){
            return HOTEL_ROOM_DESCRIBE;
        }
        if(position == 2 + hotel.rooms.size()){
            return HOTEL_FEAST_TITLE;
        }
        if(position > 2 + hotel.rooms.size() && position < hotel.rooms.size() + 3 + hotel.feasts.size()){
            return HOTEL_FEAST;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return 3 + hotel.rooms.size() + hotel.feasts.size();
    }
}
