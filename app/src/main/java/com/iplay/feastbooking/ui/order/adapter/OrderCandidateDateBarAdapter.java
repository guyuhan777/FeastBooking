package com.iplay.feastbooking.ui.order.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicArrayAdapter;

import java.util.List;

/**
 * Created by Guyuhan on 2017/11/5.
 */

public class OrderCandidateDateBarAdapter extends BasicArrayAdapter<String> {
    public OrderCandidateDateBarAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
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
            holder.candidate_date_tv = (TextView) convertView.findViewById(R.id.candidate_date_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String date = mGridData.get(position);
        holder.candidate_date_tv.setText(date);
        return convertView;
    }

    private class ViewHolder {
        TextView candidate_date_tv;
    }
}
