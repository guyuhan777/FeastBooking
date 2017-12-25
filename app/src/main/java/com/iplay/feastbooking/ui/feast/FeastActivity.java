package com.iplay.feastbooking.ui.feast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.component.view.gridview.UnScrollableGridView;
import com.iplay.feastbooking.gson.feast.FeastDetail;
import com.iplay.feastbooking.gson.hotelDetail.Feast;
import com.iplay.feastbooking.messageEvent.feast.FeastMessageEvent;
import com.iplay.feastbooking.net.NetProperties;
import com.iplay.feastbooking.net.utilImpl.feast.FeastUtility;
import com.iplay.feastbooking.ui.feast.adapter.FeastGridAdapter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

/**
 * Created by admin on 2017/9/17.
 */

public class FeastActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "FeastActivity";

    private ImageView back_iv;

    public static final String KEY_FEAST = "FEAST";

    private View status_bar_fix_tile;

    private FeastUtility utility;

    private Feast feast;

    private ScrollView feast_sv;

    private boolean isNetOn = false;

    private ImageView single_icon;

    private RollPagerView icon_roll_pagers;

    private TextView feast_name_tv;

    private TextView price_per_table_tv;

    private TextView meal_num_tv;

    private FeastDetail feastDetail;

    private UnScrollableGridView feast_list;

    @Override
    public void setContentView() {
        if(!NetProperties.isNetworkConnected(this)){
            setContentView(R.layout.feast_no_internet);
        }else {
            isNetOn = true;
            setContentView(R.layout.new_feast_detail);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findViews() {
        back_iv = (ImageView) findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
        status_bar_fix_tile = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix_tile.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        back_iv.setOnClickListener(this);
        if(isNetOn){
            feast_sv = (ScrollView) findViewById(R.id.feast_sv);
            single_icon = (ImageView) findViewById(R.id.feast_icon);
            icon_roll_pagers = (RollPagerView) findViewById(R.id.feast_icons_roll_pager);
            price_per_table_tv = (TextView) findViewById(R.id.price_per_table_tv);
            meal_num_tv = (TextView) findViewById(R.id.meal_num_tv);
            feast_name_tv = (TextView) findViewById(R.id.feast_name);
            feast_list = (UnScrollableGridView) findViewById(R.id.feast_list);
        }
    }

    public static void start(Context context, Feast feast){
        Intent intent = new Intent(context,FeastActivity.class);
        intent.putExtra(KEY_FEAST,feast);
        context.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFeastMessageEvent(FeastMessageEvent event){
        feastDetail = event.getFeastDetail();
        if(feastDetail.pictureUrls.size() == 0){
            single_icon.setVisibility(View.VISIBLE);
            icon_roll_pagers.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.ph_long).into(single_icon);
        } else if(feastDetail.pictureUrls.size() == 1){
            single_icon.setVisibility(View.VISIBLE);
            icon_roll_pagers.setVisibility(View.GONE);
            Glide.with(this).load(Uri.parse(feastDetail.pictureUrls.get(0))).placeholder(R.drawable.loading).into(single_icon);
        }else if(feastDetail.pictureUrls.size() > 1){
            icon_roll_pagers.setAdapter(new FeastIconLooperAdapter(icon_roll_pagers));
        }
        feast_name_tv.setText(feastDetail.name);
        price_per_table_tv.setText("$" + feastDetail.price);
        feast_list.setAdapter(new FeastGridAdapter(this,R.layout.feast_meal,feastDetail.courses));
        meal_num_tv.setText(feastDetail.courses.size() + "道菜");
        feast_sv.smoothScrollTo(0,20);
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        feast = (Feast) intent.getSerializableExtra(KEY_FEAST);
        utility = FeastUtility.getInstance(this);
    }

    @Override
    public void showContent() {
        utility.initFeast(feast.id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_iv:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
                break;
            default:
                break;
        }
    }

    private  class FeastIconLooperAdapter extends LoopPagerAdapter {

        public FeastIconLooperAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            String picUrl = feastDetail.pictureUrls.get(position);
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WeakReference<ImageView> imageViewWeakReference = new WeakReference<ImageView>(imageView);
            ImageView view = imageViewWeakReference.get();
            Glide.with(container.getContext()).load(Uri.parse(picUrl)).placeholder(R.drawable.loading).into(view);
            return imageView;
        }

        @Override
        public int getRealCount() {
            return feastDetail.pictureUrls.size();
        }
    }
}
