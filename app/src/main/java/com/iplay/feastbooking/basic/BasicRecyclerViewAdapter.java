package com.iplay.feastbooking.basic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.LengthUnitTranser;
import com.iplay.feastbooking.component.view.viewHolder.AdvertisementViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.ClickToLoadMoreViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.HotelRecyclerItemViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.ProgressViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.RecommendHotelGridViewHolder;
import com.iplay.feastbooking.component.view.viewHolder.TitleViewHolder;
import com.iplay.feastbooking.entity.Advertisement;
import com.iplay.feastbooking.entity.RecommendGrid;
import com.iplay.feastbooking.gson.homepage.hotelList.RecommendHotelGO;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.recommendHotelUtil.RecommendHotelListUtility;
import com.iplay.feastbooking.ui.hotelDetail.NewHotelDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/6.
 */

public class BasicRecyclerViewAdapter extends RecyclerView.Adapter<BasicViewHolder> {

    public static final int TYPE_ADS = 0;

    public static final int TYPE_GRID = 1;

    public static final int TYPE_LOADING = -1;

    public static final int TYPE_ALL_LOADED = -2;

    public static final int TYPE_TITLE_RECOMMEND = 4;

    public static final int TYPE_TITLE_ALL = 5;

    public static final int TYPE_HOTEL = 100;

    public static final int TYPE_CLICK_LOAD = 101;

    public static final int TYPE_PLACE_HOLDER = 7;

    private static final int numPerPage = 10;

    public boolean isClickToLoadMoreExist = false;

    private static final int AD_INDEX = 0;

    private static final int SP_RECOMMEND_PH_INDEX = 2;

    private List<InnerData> innerDatas = new ArrayList<>();

    private Context mContext;

    private int lastIndexOfSpecialRecommendToInsert = -1;

    private boolean isLoading = false;

    private ClickToLoadMore clickToLoadMore;

    public boolean isLoading(){
        return isLoading;
    }

    private void setLoading(){
        isLoading = true;
    }

    public void setLoaded(){
        isLoading = false;
    }

    private int numOfHotels = 0;

    public BasicRecyclerViewAdapter(Context context){
        mContext = context;

        InnerData advertisementPlaceHolder = new InnerData();
        advertisementPlaceHolder.type = TYPE_PLACE_HOLDER;
        advertisementPlaceHolder.data = new Integer(250);
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

        clickToLoadMore = new ClickToLoadMore();
    }

    @Override
    public int getItemViewType(int position) {
        return innerDatas.get(position).type;
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case TYPE_ADS:
                AdvertisementViewHolder advertisementViewHolder = new  AdvertisementViewHolder(LayoutInflater.from(mContext).inflate(R.layout.advertisement_item, parent, false));
                return advertisementViewHolder;
            case TYPE_TITLE_RECOMMEND:
                TitleViewHolder recommendTitleViewHolder = new TitleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recomment_title_item, parent, false));
                return recommendTitleViewHolder;
            case TYPE_TITLE_ALL:
                TitleViewHolder allTitleViewHolder = new TitleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recomment_title_item, parent, false));
                allTitleViewHolder.setText(mContext.getString(R.string.home_page_hotel_all));
                return allTitleViewHolder;
            case TYPE_PLACE_HOLDER:
                return new BasicViewHolder(LayoutInflater.from(mContext).inflate(R.layout.placeholder_layout,parent,false));
            case TYPE_HOTEL:
                return new HotelRecyclerItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.special_recommend_item,parent,false));
            case TYPE_GRID:
                return new RecommendHotelGridViewHolder(LayoutInflater.from(mContext).inflate(R.layout.special_recommend_bar,parent,false));
            case TYPE_LOADING:
                ProgressViewHolder loadProgress = new ProgressViewHolder(LayoutInflater.from(mContext).inflate(R.layout.progress_bar,parent,false));
                loadProgress.pb.setIndeterminate(true);
                return loadProgress;
            case TYPE_ALL_LOADED:
                return new BasicViewHolder(LayoutInflater.from(mContext).inflate(R.layout.load_state_footer,parent,false));
            case TYPE_CLICK_LOAD:
                return new ClickToLoadMoreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.click_to_load_more,parent,false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(BasicViewHolder holder, final int position) {
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
                int heightDp = (Integer) innerDatas.get(position).data;
                int heightPx = LengthUnitTranser.dip2px(mContext,heightDp);
                holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,heightPx));
                break;
            case TYPE_HOTEL:
                if(innerDatas.get(position).data != null){
                    RecommendHotelGO recommendHotelGO = (RecommendHotelGO) innerDatas.get(position).data;
                    HotelRecyclerItemViewHolder hotelRecyclerItemVH = (HotelRecyclerItemViewHolder) holder;
                    if(recommendHotelGO.pictureUrl == null || recommendHotelGO.pictureUrl.equals("")){
                        Glide.with(mContext).load(R.drawable.placeholder).into(hotelRecyclerItemVH.hotel_icon_iv);
                    }else {
                        Glide.with(mContext).load(recommendHotelGO.pictureUrl).placeholder(R.drawable.loading_reco).into(hotelRecyclerItemVH.hotel_icon_iv);
                    }
                    hotelRecyclerItemVH.hotel_name_iv.setText(recommendHotelGO.name);
                    hotelRecyclerItemVH.price_range_iv.setText(recommendHotelGO.getPriceRange());
                    hotelRecyclerItemVH.table_num_iv.setText(recommendHotelGO.getTableRange());
                    hotelRecyclerItemVH.remark_num_iv.setText(recommendHotelGO.getNumOfComment());
                    hotelRecyclerItemVH.loc_iv.setText(recommendHotelGO.districtOfAddress);
                    hotelRecyclerItemVH.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(innerDatas.get(position).data == null){
                                return;
                            }
                            NewHotelDetailActivity.start(mContext,(RecommendHotelGO) innerDatas.get(position).data);
                        }
                    });
                }
                break;
            case TYPE_CLICK_LOAD:
                ClickToLoadMoreViewHolder clickToLoadMoreVH = (ClickToLoadMoreViewHolder) holder;
                clickToLoadMoreVH.click_to_load_more_tv.setOnClickListener(clickToLoadMore);
                break;
            case TYPE_GRID:
                RecommendHotelGridViewHolder recommendHotelGridViewHolder = (RecommendHotelGridViewHolder) holder;
                RecommendGrid recommendGrid[] = (RecommendGrid[]) innerDatas.get(position).data;

                if(recommendGrid == null || recommendGrid.length!=2){
                    return;
                }

                int[] hotelIds = new int[2];
                hotelIds[0] = recommendGrid[0].getHotelId();
                hotelIds[1] = recommendGrid[1].getHotelId();

                recommendHotelGridViewHolder.setHotelIds(hotelIds);

                ImageView leftPart = recommendHotelGridViewHolder.leftView;
                ImageView rightPart = recommendHotelGridViewHolder.rightView;

                Glide.with(mContext).load(recommendGrid[0].getUrl()).placeholder(R.drawable.loading_reco).into(leftPart);
                if(recommendGrid[1] == null){
                    return;
                }else {
                    Glide.with(mContext).load(recommendGrid[1].getUrl()).placeholder(R.drawable.loading_reco).into(rightPart);
                }
                break;
            default:
                break;
        }

    }

    public boolean isAllLoaded(){
        return numOfHotels!=0 && numOfHotels%numPerPage!=0;
    };

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
            notifyItemChanged(AD_INDEX);
            return;
        }else if(type == TYPE_GRID){
            if(innerDatas.get(SP_RECOMMEND_PH_INDEX).type == TYPE_PLACE_HOLDER){
                innerDatas.remove(SP_RECOMMEND_PH_INDEX);
                lastIndexOfSpecialRecommendToInsert = SP_RECOMMEND_PH_INDEX;
            }
            if(lastIndexOfSpecialRecommendToInsert == -1){
                return;
            }else {
                innerDatas.add(lastIndexOfSpecialRecommendToInsert, innerData);
                if(lastIndexOfSpecialRecommendToInsert == SP_RECOMMEND_PH_INDEX){
                    notifyItemChanged(SP_RECOMMEND_PH_INDEX);
                }else{
                    notifyItemInserted(lastIndexOfSpecialRecommendToInsert);
                }
                lastIndexOfSpecialRecommendToInsert++;
                return;
            }
        }else if(type == TYPE_HOTEL){
            if(data == null){
                return;
            }else {
                numOfHotels++;
                int addIndex = innerDatas.size();
                innerDatas.add(innerData);
                notifyItemChanged(addIndex);
                return;
            }
        }else {
            int addIndex = innerDatas.size();
            innerDatas.add(innerData);
            notifyItemChanged(addIndex);
            return;
        }
    }

    public void loadMoreData(RecommendHotelListUtility utility){
        if(utility == null){
            return;
        }
        setLoading();
        addData(BasicRecyclerViewAdapter.TYPE_LOADING,null);
        int page = numOfHotels/numPerPage;
        utility.loadMore(page);
    }

    public void cancelLoading(){
        InnerData innerData = innerDatas.get(innerDatas.size()-1);
        if(innerData.type == TYPE_LOADING){
            int lastIndex = innerDatas.size() - 1;
            innerDatas.remove(lastIndex);
        }
    }

    private class InnerData{

        public int type;

        public Object data;

    }

    private class ClickToLoadMore implements View.OnClickListener{

        private RecommendHotelListUtility utility = RecommendHotelListUtility.getInstance(mContext);

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.click_to_load_more_tv){
                if(!NetProperties.isNetworkConnected(mContext)){
                    Toast.makeText(mContext,"網絡不給力",Toast.LENGTH_SHORT).show();
                    return;
                }
                int lastIndex = innerDatas.size() - 1;
                innerDatas.remove(lastIndex);
                isClickToLoadMoreExist = false;
                loadMoreData(utility);
            }
        }
    }
}
