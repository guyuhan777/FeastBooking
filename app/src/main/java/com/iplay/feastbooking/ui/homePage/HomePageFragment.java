package com.iplay.feastbooking.ui.homePage;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.ui.home.HomeActivity;
import com.iplay.feastbooking.ui.hotelDetail.HotelDetailActivity;
import com.iplay.feastbooking.ui.recommendedHotel.RecommendedHotelFragment;

/**
 * Created by admin on 2017/7/14.
 */

public class HomePageFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomePageFragment";

    private View view;

    private GridView recommended_hotel_gv;

    private TextView recommend_more_hotel_tv;

    private int[] hotel_icon_image_ids;

    private int size = 9;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_view,container,false);
        init();
        return view;
    }

    private void init(){
        intData();
        initView();
    }

    private void initView(){
        recommended_hotel_gv = (GridView) view.findViewById(R.id.home_page_recommend_grid_view);

        recommended_hotel_gv.setAdapter(new ImageAdapter(getActivity()));
        recommended_hotel_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotelDetailActivity.start(getActivity(),hotel_icon_image_ids[position]);
            }
        });
        recommend_more_hotel_tv = (TextView) view.findViewById(R.id.home_page_recommend_more);
        recommend_more_hotel_tv.setOnClickListener(this);
    }

    private void intData(){
        hotel_icon_image_ids = new int[size];
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_page_recommend_more:
                HomeActivity activity = (HomeActivity) getActivity();
                if(activity.recommendedHotelFragment==null){
                    activity.recommendedHotelFragment = new RecommendedHotelFragment();
                }
                activity.replaceFragment(activity.recommendedHotelFragment);
                break;
        }
    }

    private class ImageAdapter extends BaseAdapter {

        private Context context;

        ImageAdapter(Context context){
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
            Glide.with(context).load(hotel_icon_image_ids[position]).into(vh.iv);
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
