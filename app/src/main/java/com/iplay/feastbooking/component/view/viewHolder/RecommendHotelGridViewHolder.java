package com.iplay.feastbooking.component.view.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/9/7.
 */

public class RecommendHotelGridViewHolder extends BasicViewHolder {

    private GridView recommended_hotel_gv;

    private int[] hotel_icon_image_ids = new int[9];

    private void dataInit(){
        hotel_icon_image_ids[0] = R.drawable.hotel1;
        hotel_icon_image_ids[1] = R.drawable.hotel2;
        hotel_icon_image_ids[2] = R.drawable.hotel3;
        hotel_icon_image_ids[3] = R.drawable.hotel4;
        hotel_icon_image_ids[4] = R.drawable.hotel5;
        hotel_icon_image_ids[5] = R.drawable.hotel6;
        hotel_icon_image_ids[6] = R.drawable.hotel7;
        hotel_icon_image_ids[7] = R.drawable.hotel8;
        hotel_icon_image_ids[8] = R.drawable.hotel9;
    }

    public RecommendHotelGridViewHolder(View itemView) {
        super(itemView);
        recommended_hotel_gv = (GridView) itemView.findViewById(R.id.home_page_recommend_grid_view);
        dataInit();
        ImageAdapter adapter = new ImageAdapter();
        adapter.setData(hotel_icon_image_ids);
        recommended_hotel_gv.setAdapter(adapter);
    }

    static class ImageAdapter extends BaseAdapter {

        private int[] hotel_icon_image_ids;

        public void setData(int[] hotel_icon_image_ids){
            this.hotel_icon_image_ids = hotel_icon_image_ids;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_recommend_grid_item,parent,false);
                vh = new ViewHolder();
                vh.iv = (ImageView) convertView.findViewById(R.id.recommended_hotel_icon);
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder) convertView.getTag();
            }
            Glide.with(parent.getContext()).load(hotel_icon_image_ids[position]).into(vh.iv);
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return hotel_icon_image_ids == null?0:hotel_icon_image_ids.length;
        }

        class ViewHolder{
            ImageView iv;
        }
    }
}
