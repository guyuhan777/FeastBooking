package com.iplay.feastbooking.ui.feast.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;
import com.iplay.feastbooking.entity.HotelFeast;
import com.iplay.feastbooking.ui.feast.viewholder.FeastIconViewHolder;
import com.iplay.feastbooking.ui.feast.viewholder.FeastMealViewHolder;

/**
 * Created by admin on 2017/9/17.
 */

public class FeastAdapter extends RecyclerView.Adapter<BasicViewHolder> {

    public static final int TYPE_FEAST_ICON = 0;

    public static final int TYPE_MEAL = 1;

    private Context mContext;

    public void setFeast(HotelFeast feast) {
        this.feast = feast;
        if(feast.meals.size() % 2 != 0){
            feast.meals.add("");
        }
    }

    private HotelFeast feast;

    public FeastAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_FEAST_ICON){
            FeastIconViewHolder iconViewHolder = new FeastIconViewHolder(LayoutInflater.from(mContext).inflate(R.layout.feast_icon,parent,false));
            return iconViewHolder;
        }
        if(viewType == TYPE_MEAL){
            FeastMealViewHolder feastMealViewHolder = new FeastMealViewHolder(LayoutInflater.from(mContext).inflate(R.layout.feast_meal,parent,false));
            return feastMealViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BasicViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_FEAST_ICON){
            FeastIconViewHolder fh = (FeastIconViewHolder) holder;
            Glide.with(mContext).load(R.drawable.meal2).into(fh.icon);
        }
        if(getItemViewType(position) == TYPE_MEAL){
            FeastMealViewHolder fmvh = (FeastMealViewHolder) holder;
            fmvh.leftMeal.setText(feast.meals.get((position - 1) * 2));
            fmvh.rightMeal.setText(feast.meals.get((position - 1)*2+1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_FEAST_ICON;
        }
        return TYPE_MEAL;
    }

    @Override
    public int getItemCount() {
        return 1 + feast.meals.size()/2;
    }
}
