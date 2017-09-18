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
import android.widget.RelativeLayout;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicRecyclerViewAdapter;
import com.iplay.feastbooking.entity.Hotel;
import com.iplay.feastbooking.factory.HotelFactory;

import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class RecommendedHotelFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = "recommendHotelFragment";

    private View statusView;

    private View view;

    private Context mContext;

    private RecyclerView mainView;

    private View adsHeader;

    private View loadStateFooter;

    private View adsGridView;

    private View title_recommend;

    private View title_all;

    private List<Hotel> hotels;

    private int visibleThreshold= 1;

    private int totalItemCount;

    private int lastVisibleItemPosition;

    private boolean isLoading;

    private Handler handler = new Handler();

    private BasicRecyclerViewAdapter adapter;

    private void setLoaded(){
        isLoading = false;
    }

    public void loadMoreData(){
        hotels.add(null);
        adapter.notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hotels.remove(hotels.size()-1);
                adapter.notifyDataSetChanged();
                List<Hotel> list = HotelFactory.getMoreHotel();
                for(int i=0;i<list.size();i++){
                    hotels.add(list.get(i));
                }
                adapter.notifyDataSetChanged();
                setLoaded();
            }
        },3000);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.special_recommend_layout,container,false);
        mContext = getActivity();

        statusView = (View) view.findViewById(R.id.status_bar_fix);
        statusView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(getActivity())));

        adsHeader = (View) LayoutInflater.from(mContext).inflate(R.layout.advertisement_item,container,false);
        loadStateFooter = (View) LayoutInflater.from(mContext).inflate(R.layout.progress_bar,container,false);
        adsGridView = (View) LayoutInflater.from(mContext).inflate(R.layout.recommend_hotel_grid_item,container,false);
        title_recommend = (View) LayoutInflater.from(mContext).inflate(R.layout.recomment_title_item,container,false);
        title_all = (View) LayoutInflater.from(mContext).inflate(R.layout.recomment_title_item,container,false);

        hotels = HotelFactory.generateHotel();

        adapter = new BasicRecyclerViewAdapter(getActivity());
        adapter.setData(hotels);
        adapter.setmHeaderView(adsHeader);
        adapter.setTitle_recommend(title_recommend);
        adapter.setmGridView(adsGridView);
        adapter.setTitle_all(title_all);
        adapter.setmFooterView(loadStateFooter);

        mainView = (RecyclerView) view.findViewById(R.id.main_recycler_view);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mainView.setLayoutManager(manager);
        mainView.setAdapter(adapter);

        mainView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = manager.getItemCount();
                lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if(!isLoading && totalItemCount <= lastVisibleItemPosition + visibleThreshold){
                    loadMoreData();
                    isLoading = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

}
