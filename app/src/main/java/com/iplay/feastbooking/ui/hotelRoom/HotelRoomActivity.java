package com.iplay.feastbooking.ui.hotelRoom;

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
import com.iplay.feastbooking.entity.HotelRoom;
import com.iplay.feastbooking.ui.hotelRoom.adapter.HotelRoomAdapter;


/**
 * Created by admin on 2017/9/21.
 */

public class HotelRoomActivity extends BasicActivity implements View.OnClickListener{

    private HotelRoom hotelRoom;

    private ImageView back_iv;

    public static final String KEY_ROOM = "key_room";

    private RecyclerView content;

    private HotelRoomAdapter adapter;

    private View status_bar_fix_tile;

    @Override
    public void setContentView() {
        setContentView(R.layout.hotel_room_detail_layout);
    }

    @Override
    public void findViews() {
        status_bar_fix_tile = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix_tile.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        back_iv = (ImageView) findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
        content = (RecyclerView) findViewById(R.id.room_recycler_content);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content.setLayoutManager(manager);
        adapter = new HotelRoomAdapter(this);
        adapter.setHotelRoom(hotelRoom);
        content.setAdapter(adapter);
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        hotelRoom = (HotelRoom) intent.getSerializableExtra(KEY_ROOM);
    }

    @Override
    public void showContent() {

    }

    public static void start(Context context, HotelRoom room){
        Intent intent = new Intent(context,HotelRoomActivity.class);
        intent.putExtra(KEY_ROOM,room);
        context.startActivity(intent);
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
