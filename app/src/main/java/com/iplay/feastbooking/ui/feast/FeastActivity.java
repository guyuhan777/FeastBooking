package com.iplay.feastbooking.ui.feast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.entity.HotelFeast;
import com.iplay.feastbooking.ui.feast.adapter.FeastAdapter;

/**
 * Created by admin on 2017/9/17.
 */

public class FeastActivity extends BasicActivity implements View.OnClickListener{

    private ImageView back_iv;

    public static final String KEY_FEAST = "FEAST";

    private View status_bar_fix_tile;

    private FeastAdapter adapter;

    private RecyclerView content;

    private HotelFeast hotelFeast;

    @Override
    public void setContentView() {
        setContentView(R.layout.feast_detail);
    }

    @Override
    public void findViews() {
        back_iv = (ImageView) findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
        status_bar_fix_tile = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix_tile.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        content = (RecyclerView) findViewById(R.id.feast_recycler_content);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content.setLayoutManager(manager);
        adapter = new FeastAdapter(this);
        adapter.setFeast(hotelFeast);
        content.setAdapter(adapter);
    }

    public static void start(Context context, HotelFeast feast){
        Intent intent = new Intent(context,FeastActivity.class);
        intent.putExtra(KEY_FEAST,feast);
        context.startActivity(intent);
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        hotelFeast = (HotelFeast) intent.getSerializableExtra(KEY_FEAST);
    }

    @Override
    public void showContent() {

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
        }
    }
}
