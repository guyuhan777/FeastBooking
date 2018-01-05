package com.iplay.feastbooking.ui.search.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicArrayAdapter;
import com.iplay.feastbooking.dto.HistorySearchItemDto;
import com.iplay.feastbooking.ui.feast.adapter.FeastGridAdapter;

import java.util.List;

/**
 * Created by gu_y-pc on 2018/1/4.
 */

public class SearchHistoryAdapter extends BasicArrayAdapter<HistorySearchItemDto> implements View.OnClickListener{

    private EditText searchBox;

    public SearchHistoryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HistorySearchItemDto> objects) {
        super(context, resource, objects);
    }

    public void setSearchBox(EditText searchBox) {
        this.searchBox = searchBox;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.search_item = (TextView) convertView.findViewById(R.id.search_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        HistorySearchItemDto searchItem = mGridData.get(position);
        holder.search_item.setText(searchItem.getKeyword());
        holder.search_item.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if(v instanceof TextView){
            String keyword = ((TextView) v).getText().toString();
            if(searchBox != null) searchBox.setText(keyword);
        }
    }

    private class ViewHolder{
        TextView search_item;
    }
}
