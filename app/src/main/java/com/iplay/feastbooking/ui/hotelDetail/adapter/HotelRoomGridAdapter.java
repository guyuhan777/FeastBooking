package com.iplay.feastbooking.ui.hotelDetail.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicArrayAdapter;
import com.iplay.feastbooking.gson.hotelDetail.BanquetHall;
import com.iplay.feastbooking.ui.hotelRoom.HotelRoomActivity;

import java.util.List;

/**
 * Created by admin on 2017/10/14.
 */

public class HotelRoomGridAdapter extends BasicArrayAdapter<BanquetHall> {

    public HotelRoomGridAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BanquetHall> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.hotel_room_name = (TextView) convertView.findViewById(R.id.hotel_room_name);
            holder.hotel_min_cost = (TextView) convertView.findViewById(R.id.min_cost);
            holder.hotel_room_area = (TextView) convertView.findViewById(R.id.hotel_room_area);
            holder.hotel_room_icon = (ImageView) convertView.findViewById(R.id.hotel_room_icon);
            holder.table_end = (TextView) convertView.findViewById(R.id.table_end);
            holder.table_start = (TextView) convertView.findViewById(R.id.table_start);
            holder.root_view = (CardView) convertView.findViewById(R.id.hotel_room_root);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        BanquetHall banquetHall = mGridData.get(position);
        Glide.with(mContext).load(Uri.parse(banquetHall.pictureUrl)).into(holder.hotel_room_icon);
        holder.table_start.setText(banquetHall.tableRange[0] + "");
        holder.table_end.setText(banquetHall.tableRange[1] + "");
        holder.hotel_room_area.setText(banquetHall.area + "");
        holder.hotel_min_cost.setText("$" + banquetHall.minimumPrice);
        holder.hotel_room_name.setText(banquetHall.name);
        holder.root_view.setOnClickListener(new BanquetHallCheckListener(position));
        return convertView;
    }

    private class BanquetHallCheckListener implements View.OnClickListener{

        private int index;

        public BanquetHallCheckListener(int index){
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            HotelRoomActivity.start(mContext,mGridData.get(index));
        }
    }

    private class ViewHolder {
        CardView root_view;
        TextView hotel_room_name;
        TextView hotel_room_area;
        TextView hotel_min_cost;
        TextView table_start;
        TextView table_end;
        ImageView hotel_room_icon;
    }


}
