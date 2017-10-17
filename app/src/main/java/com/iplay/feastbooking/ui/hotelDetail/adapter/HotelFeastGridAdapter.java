package com.iplay.feastbooking.ui.hotelDetail.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicArrayAdapter;
import com.iplay.feastbooking.gson.hotelDetail.Feast;
import com.iplay.feastbooking.ui.feast.FeastActivity;

import java.util.List;

/**
 * Created by admin on 2017/10/14.
 */

public class HotelFeastGridAdapter extends BasicArrayAdapter<Feast>{

    public HotelFeastGridAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Feast> objects) {
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
            holder.hotel_feast_name = (TextView) convertView.findViewById(R.id.feast_name);
            holder.root_view = (LinearLayout) convertView.findViewById(R.id.feast_check_root);
            holder.hotel_feast_price = (TextView) convertView.findViewById(R.id.price_per_table_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Feast feast= mGridData.get(position);
        holder.hotel_feast_name.setText(feast.name);
        holder.root_view.setOnClickListener(new FeastCheckListener(position));
        holder.hotel_feast_price.setText("$" + feast.price + "/桌");
        return convertView;
    }

    private class FeastCheckListener implements View.OnClickListener{

        private int pos;

        public FeastCheckListener(int pos){
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            FeastActivity.start(mContext,mGridData.get(pos));
        }
    }

    private class ViewHolder {
        LinearLayout root_view;
        TextView hotel_feast_name;
        TextView hotel_feast_price;
        ImageView forwards_iv;
    }
}
