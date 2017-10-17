package com.iplay.feastbooking.basic;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by admin on 2017/10/14.
 */

public class BasicArrayAdapter<T> extends ArrayAdapter<T> {

    protected Context mContext;
    protected int layoutResourceId;
    protected List<T> mGridData;

    public BasicArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mGridData = objects;
    }

    public void setGridData(List<T> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
}
