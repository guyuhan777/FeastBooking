package com.iplay.feastbooking.basic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.component.view.viewHolder.AdvertisementViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.RecommendHotelGridViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.TitleViewHolder;
import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.entity.RecommendGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/6.
 */

public class BasicRecyclerViewAdapter extends RecyclerView.Adapter<BasicViewHolder> {

    public static final int TYPE_ADS = 0;

    public static final int TYPE_GRID = 1;

    public static final int TYPE_FOOTER = 2;

    public static final int TYPE_NORMAL = 3;

    public static final int TYPE_TITLE_RECOMMEND = 4;

    public static final int TYPE_TITLE_ALL = 5;

    public static final int TYPE_HOTEL = 6;

    public static final int TYPE_PLACE_HOLDER = 7;

    private static final int AD_INDEX = 0;

    private static final int SP_RECOMMEND_PH_INDEX = 2;

    private List<InnerData> innerDatas = new ArrayList<>();

    private Context mContext;

    private int lastIndexOfSpecialRecommendToInsert = -1;

    public BasicRecyclerViewAdapter(Context context){
        mContext = context;

        InnerData advertisementPlaceHolder = new InnerData();
        advertisementPlaceHolder.type = TYPE_PLACE_HOLDER;
        advertisementPlaceHolder.data = new Integer(300);
        innerDatas.add(advertisementPlaceHolder);

        InnerData titleRecommend = new InnerData();
        titleRecommend.type = TYPE_TITLE_RECOMMEND;
        innerDatas.add(titleRecommend);

        InnerData recommendPlaceHolder = new InnerData();
        recommendPlaceHolder.type = TYPE_PLACE_HOLDER;
        recommendPlaceHolder.data = new Integer(600);
        innerDatas.add(recommendPlaceHolder);

        InnerData titleAll = new InnerData();
        titleAll.type = TYPE_TITLE_ALL;
        innerDatas.add(titleAll);
    }

    @Override
    public int getItemViewType(int position) {
        return innerDatas.get(position).type;
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case TYPE_ADS:
                return new AdvertisementViewHolder(LayoutInflater.from(mContext).inflate(R.layout.advertisement_item, parent, false));
            case TYPE_TITLE_RECOMMEND:
                TitleViewHolder recommendTitleViewHolder = new TitleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recomment_title_item, parent, false));
                return recommendTitleViewHolder;
            case TYPE_TITLE_ALL:
                TitleViewHolder allTitleViewHolder = new TitleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recomment_title_item, parent, false));
                allTitleViewHolder.setText(mContext.getString(R.string.home_page_hotel_all));
                return allTitleViewHolder;
            case TYPE_PLACE_HOLDER:
                return new BasicViewHolder(LayoutInflater.from(mContext).inflate(R.layout.placeholder_layout,parent,false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(BasicViewHolder holder, int position) {

        int type = getItemViewType(position);
        switch (type){
            case TYPE_ADS:
                AdvertisementViewHolder advertisementViewHolder = (AdvertisementViewHolder) holder;
                List<Advertisement> advertisements = (List<Advertisement>) innerDatas.get(position).data;
                if(advertisements == null || advertisements.size() == 0){
                    return;
                }
                advertisementViewHolder.initAdapter(advertisements);
                break;
            case TYPE_PLACE_HOLDER:
                int height = (Integer) innerDatas.get(position).data;
                holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
                break;
            case TYPE_GRID:
                RecommendHotelGridViewHolder recommendHotelGridViewHolder = (RecommendHotelGridViewHolder) holder;
                RecommendGrid recommendGrid[] = (RecommendGrid[]) innerDatas.get(position).data;
                if(recommendGrid == null || recommendGrid.length!=2){
                    return;
                }
                ImageView leftPart = recommendHotelGridViewHolder.leftView;
                ImageView rightPart = recommendHotelGridViewHolder.rightView;

                Glide.with(mContext).load(recommendGrid[0].getUrl()).placeholder(R.drawable.shenzi).into(leftPart);
                if(recommendGrid[1] == null){
                    return;
                }else {
                    Glide.with(mContext).load(recommendGrid[1].getUrl()).placeholder(R.drawable.shenzi).into(leftPart);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return innerDatas.size();
    }

    public void addData(int type, Object data){
        InnerData innerData = new InnerData();
        innerData.type = type;
        innerData.data = data;
        if(type == TYPE_ADS){
            innerDatas.remove(AD_INDEX);
            innerDatas.add(AD_INDEX,innerData);
        }else if(type == TYPE_GRID){
            if(innerDatas.get(SP_RECOMMEND_PH_INDEX).type == TYPE_PLACE_HOLDER){
                innerDatas.remove(SP_RECOMMEND_PH_INDEX);
                lastIndexOfSpecialRecommendToInsert = SP_RECOMMEND_PH_INDEX;
            }else{
                if(lastIndexOfSpecialRecommendToInsert == -1){
                    return;
                }else{
                    innerDatas.add(lastIndexOfSpecialRecommendToInsert, innerData);
                    lastIndexOfSpecialRecommendToInsert++;
                }
            }
        }
        notifyDataSetChanged();
    }

    private class InnerData{

        public int type;

        public Object data;

    }
}
