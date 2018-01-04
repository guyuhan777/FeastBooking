package com.iplay.feastbooking.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.component.view.gridview.UnScrollableGridView;
import com.iplay.feastbooking.dto.HistorySearchItemDto;
import com.iplay.feastbooking.ui.search.adapter.SearchHistoryAdapter;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by gu_y-pc on 2018/1/4.
 */

public class SearchActivity extends BasicActivity
        implements View.OnClickListener, View.OnTouchListener{

    private UnScrollableGridView recentSearchGv;

    @Override
    public void setContentView() {
        setContentView(R.layout.hotel_search_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.cancel_tv).setOnClickListener(this);
        findViewById(R.id.root_view).setOnClickListener(this);
        findViewById(R.id.recent_search_sv).setOnTouchListener(this);
        findViewById(R.id.recent_search_gv).setOnTouchListener(this);
        recentSearchGv = (UnScrollableGridView) findViewById(R.id.recent_search_gv);
        List<HistorySearchItemDto> historySearchItems = DataSupport.findAll(HistorySearchItemDto.class);
        recentSearchGv.setAdapter(new SearchHistoryAdapter(this, R.layout.recent_search_item, historySearchItems));
    }

    @Override
    public void getData() {

    }

    public static void start(Context context){
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void showContent() {

    }

    private void hideInput(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_tv:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
                break;
            case R.id.root_view:
                hideInput(v);
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideInput(v);
        return false;
    }
}
