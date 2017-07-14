package com.iplay.feastbooking.ui.recommendedHotel;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;
import com.iplay.feastbooking.component.pull2Load.LoadMoreDataListener;
import com.iplay.feastbooking.component.view.viewHolder.HotelRecyclerItemViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.ProgressViewHolder;
import com.iplay.feastbooking.entity.Hotel;
import com.iplay.feastbooking.factory.HotelFactory;

import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class RecommendedHotelFragment extends Fragment implements LoadMoreDataListener{

    private static final String TAG = "recommendHotelFragment";

    private View view;

    private RecyclerView recommendedHotel_rv;

    private List<Hotel> hotels;

    private Context mContext;

    private HotelAdapter adapter;

    private int lastVisibleItemPosition;

    private boolean isLoading;

    private int visibleThreshold= 1;

    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.special_recommend_layout,container,false);
        mContext = getActivity();
        init();
        return view;
    }

    private void init(){
        dataInit();
        viewInit();
    }

    private void dataInit(){
        hotels = HotelFactory.generateHotel();
    }

    private void viewInit(){
        recommendedHotel_rv = (RecyclerView) view.findViewById(R.id.special_recommend_rv);
        adapter = new HotelAdapter();
        recommendedHotel_rv.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recommendedHotel_rv.setLayoutManager(linearLayoutManager);
        recommendedHotel_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && totalItemCount <= lastVisibleItemPosition + visibleThreshold){
                    loadMoreData();
                    isLoading = true;
                }
            }
        });
    }


    @Override
    public void loadMoreData() {
        hotels.add(null);
        adapter.notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hotels.remove(hotels.size()-1);
                adapter.notifyDataSetChanged();
                List<Hotel> moreHotels = HotelFactory.getMoreHotel();
                hotels.addAll(moreHotels);
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        },3000);
    }

    private class HotelAdapter extends RecyclerView.Adapter<BasicViewHolder>{
        @Override
        public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BasicViewHolder holder = null;
            switch (viewType){
                case TYPE_ITEM:
                    holder = new HotelRecyclerItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.special_recommend_item,parent,false));
                    break;
                case TYPE_PROG:
                    holder = new ProgressViewHolder(LayoutInflater.from(mContext).inflate(R.layout.progress_bar,parent,false));
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(BasicViewHolder holder, int position) {
            switch (getItemViewType(position)){
                case TYPE_ITEM:
                    HotelRecyclerItemViewHolder itemViewHolder= (HotelRecyclerItemViewHolder) holder;
                    Hotel hotel = hotels.get(position);
                    itemViewHolder.hotel_dist.setText(hotel.distance+"km");
                    itemViewHolder.hotel_name.setText(hotel.hotel_name);
                    itemViewHolder.hotel_loc.setText(hotel.location);
                    Glide.with(mContext).load(hotel.icon_id).into(itemViewHolder.hotel_icon_iv);
                    break;
                case TYPE_PROG:
                    ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
                    if(progressViewHolder.pb!=null){
                        progressViewHolder.pb.setIndeterminate(true);
                    }
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return hotels.get(position)!=null?TYPE_ITEM:TYPE_PROG;
        }

        @Override
        public int getItemCount() {
            return hotels==null?0:hotels.size();
        }
    }
}
