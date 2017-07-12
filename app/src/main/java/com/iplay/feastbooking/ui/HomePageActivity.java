package com.iplay.feastbooking.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicActivity;

public class HomePageActivity extends BasicActivity {

    private GridView recommended_hotel_gv;

    private int[] hotel_icon_image_ids;

    private int size = 8;

    @Override
    public void setContentView() {
        setContentView(R.layout.main_view);
    }

    @Override
    public void findViews() {
        recommended_hotel_gv = (GridView) findViewById(R.id.home_page_recommend_grid_view);
        hotel_icon_image_ids = new int[size];
        for ( int i = 0 ; i < size ; i++){
            hotel_icon_image_ids[i] = R.drawable.shenzi;
        }
        recommended_hotel_gv.setAdapter(new ImageAdapter(this));
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    private class ImageAdapter extends BaseAdapter {

        private Context context;

        public ImageAdapter(Context context){
            this.context = context;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.home_page_recommend_grid_item,parent,false);
                vh = new ViewHolder();
                vh.iv = (ImageView) convertView.findViewById(R.id.recommended_hotel_icon);
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.iv.setImageDrawable(getDrawable(hotel_icon_image_ids[position]));
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return size;
        }

        class ViewHolder{
            ImageView iv;
        }
    }
}
