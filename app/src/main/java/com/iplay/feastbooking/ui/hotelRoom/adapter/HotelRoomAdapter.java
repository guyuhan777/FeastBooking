package com.iplay.feastbooking.ui.hotelRoom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;
import com.iplay.feastbooking.entity.HotelRoom;
import com.iplay.feastbooking.ui.hotelRoom.viewholder.ConfigViewHolder;
import com.iplay.feastbooking.ui.hotelRoom.viewholder.EnvironmentViewHolder;
import com.iplay.feastbooking.ui.hotelRoom.viewholder.RoomIconViewHolder;
import com.iplay.feastbooking.ui.hotelRoom.viewholder.RoomSerperateViewHolder;
import com.iplay.feastbooking.ui.hotelRoom.viewholder.RoomTitleViewHolder;

import java.util.List;

/**
 * Created by admin on 2017/9/21.
 */

public class HotelRoomAdapter extends RecyclerView.Adapter<BasicViewHolder> {

    public static final int TYPE_ICON = 1;

    public static final int TYPE_TITLE_ENV = 2;

    public static final int TYPE_ENV = 3;

    public static final int TYPE_TITLE_CONFIG = 4;

    public static final int TYPE_CONFIG = 5;

    public static final int TYPE_SERPERATE = 6;

    private HotelRoom hotelRoom;

    private Context mContext;

    public HotelRoomAdapter(Context context){
        mContext = context;
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ICON){
            return new RoomIconViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.room_icon,parent,false));
        }
        if(viewType == TYPE_TITLE_CONFIG || viewType == TYPE_TITLE_ENV){
            return new RoomTitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.room_environment_title,parent,false));
        }
        if(viewType == TYPE_ENV ){
            return new EnvironmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.enviroment_bar,parent,false));
        }
        if(viewType == TYPE_CONFIG){
            return new ConfigViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.config_bar,parent,false));
        }
        if(viewType == TYPE_SERPERATE){
            return new RoomSerperateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.room_seperate_line,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BasicViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_ICON){
            RoomIconViewHolder roomIconViewHolder = (RoomIconViewHolder) holder;
            Glide.with(mContext).load(hotelRoom.icon_id).into(roomIconViewHolder.icon);
        }
        if(getItemViewType(position) == TYPE_TITLE_CONFIG){
            RoomTitleViewHolder titleViewHolder = (RoomTitleViewHolder) holder;
            titleViewHolder.title.setText("配置");
        }
        if(getItemViewType(position) == TYPE_ENV){
            String leftPart = hotelRoom.environments.get((position-2)*2);
            String rightPart = hotelRoom.environments.get((position-2)*2 + 1);
            EnvironmentViewHolder environmentViewHolder = (EnvironmentViewHolder) holder;
            environmentViewHolder.left_env.setText(leftPart);
            environmentViewHolder.right_env.setText(rightPart);
        }
        if(getItemViewType(position) == TYPE_CONFIG){
            int start = (position - 4 - hotelRoom.environments.size()/2) * 3;
            ConfigViewHolder configViewHolder =  (ConfigViewHolder) holder;
            for(int i=start;i<start+3;i++){
                String config = hotelRoom.configs.get(i);
                if(config.equals("")){
                    configViewHolder.layouts[i-start].setVisibility(View.INVISIBLE);
                }else{
                    configViewHolder.textViews[i-start].setText(config);
                }
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_ICON;
        }
        if(position == 1){
            return TYPE_TITLE_ENV;
        }
        if(position >= 2 && position < 2 + hotelRoom.environments.size()/2){
            return TYPE_ENV;
        }
        if(position == 2 + hotelRoom.environments.size()/2){
            return TYPE_SERPERATE;
        }
        if(position == 3 + hotelRoom.environments.size()/2){
            return TYPE_TITLE_CONFIG;
        }
        return TYPE_CONFIG;
    }

    @Override
    public int getItemCount() {
        return 4 + hotelRoom.configs.size()/3  + hotelRoom.environments.size()/2 ;
    }

    public void setHotelRoom(HotelRoom hotelRoom) {
        this.hotelRoom = hotelRoom;
        List<String> configs = hotelRoom.configs;
        List<String> envs = hotelRoom.environments;
        if(configs.size()%3!=0){
            int left = 3 - configs.size()%3;
            for(int i=0;i<left;i++){
                configs.add("");
            }
        }
        if(envs.size() %2 !=0 ){
            envs.add("");
        }
    }
}
