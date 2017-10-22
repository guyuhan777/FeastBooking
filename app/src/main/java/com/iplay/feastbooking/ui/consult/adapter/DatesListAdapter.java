package com.iplay.feastbooking.ui.consult.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.DateFormatter;
import com.iplay.feastbooking.basic.BasicArrayAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/10/22.
 */

public class DatesListAdapter extends BasicArrayAdapter<List<Date>> {

    public DatesListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<List<Date>> objects) {
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
            holder.date_tv = (TextView) convertView.findViewById(R.id.date_tv);
            holder.cancel_iv = (ImageView) convertView.findViewById(R.id.cancel_iv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        List<Date> dates = mGridData.get(position);
        holder.date_tv.setText(getFormattedDate(dates));
        holder.cancel_iv.setOnClickListener(new CancelListener(dates));
        return convertView;
    }

    private String getFormattedDate(List<Date> dates){
        String dateStr = "";
        if (dates.size() == 1){
            dateStr =  getFormattedDate(dates.get(0));
        }
        if(dates.size() >= 2){
            dateStr =  getFormattedDate(dates.get(0)) + " - " + getFormattedDate(dates.get(dates.size() - 1));
        }
        return dateStr;
    }

    private String getFormattedDate(Date date){
        String dateStr = "";
        if (date!=null){
            dateStr = DateFormatter.formatDate(date);
        }
        return dateStr;
    }

    private class CancelListener implements View.OnClickListener{

        private List<Date> dates;

        public CancelListener(List<Date> dates){
            super();
            this.dates = dates;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.cancel_iv){
                mGridData.remove(dates);
                notifyDataSetChanged();
            }
        }
    }

    private class ViewHolder{
        TextView date_tv;
        ImageView cancel_iv;
    }
}
