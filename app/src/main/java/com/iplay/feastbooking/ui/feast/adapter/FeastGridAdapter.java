package com.iplay.feastbooking.ui.feast.adapter;

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
 * Created by admin on 2017/10/14.
 */

public class FeastGridAdapter extends BasicArrayAdapter<String> {

    public FeastGridAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.feast_name = (TextView) convertView.findViewById(R.id.meal_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String meal= mGridData.get(position);
        holder.feast_name.setText(meal);
        return convertView;
    }

    private class ViewHolder {
        TextView feast_name;
    }
}
