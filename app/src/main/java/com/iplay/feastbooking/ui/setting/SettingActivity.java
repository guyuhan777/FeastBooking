package com.iplay.feastbooking.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.DataCleanManager;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;

/**
 * Created by gu_y-pc on 2018/1/20.
 */

public class SettingActivity extends BasicActivity implements View.OnClickListener{

    private RelativeLayout cacheClearFunctionBar;

    private TextView cache_size_tv;

    private ProgressBar clean_pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.setting);
    }

    private void calculateCacheSize(){
        new CacheSizeCalculateTask().execute();
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);

        cache_size_tv = (TextView) findViewById(R.id.cache_tv);
        clean_pb = (ProgressBar) findViewById(R.id.clear_cache_pb);

        cacheClearFunctionBar = (RelativeLayout) findViewById(R.id.clear_cache_bar);
        cacheClearFunctionBar.setOnClickListener(this);
        cacheClearFunctionBar.setEnabled(false);

        calculateCacheSize();
    }

    private void displayCleaningHint(boolean isStart, boolean isSuccess){
        if(isStart){
            clean_pb.setVisibility(View.VISIBLE);
        }else{
            clean_pb.setVisibility(View.INVISIBLE);
            if(isSuccess) {
                Toast.makeText(this, getText(R.string.clean_cache_success), Toast.LENGTH_SHORT).show();
                cache_size_tv.setText("0.0B");
            }else {
                Toast.makeText(this, getText(R.string.clean_cache_failure), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void start(Context context){
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void getData() {

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
                break;
            case R.id.clear_cache_bar:
                displayCleaningHint(true, false);
                cacheClearFunctionBar.setEnabled(false);
                new CacheClearTask().execute();
                break;
            default:
                break;
        }
    }

    private class CacheClearTask extends AsyncTask<String, Integer, Boolean>{

        @Override
        protected void onPostExecute(Boolean b) {
            if(b){
                displayCleaningHint(false, true);
            }else{
                displayCleaningHint(false, false);
            }
            cacheClearFunctionBar.setEnabled(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return DataCleanManager.clearApplicationCache(SettingActivity.this);
        }
    }

    private class CacheSizeCalculateTask extends AsyncTask<String, Integer, String>{
        @Override
        protected void onPostExecute(String s) {
            cache_size_tv.setText(s);
            cacheClearFunctionBar.setEnabled(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return DataCleanManager.getFormattedCacheSize(SettingActivity.this);
        }
    }
}
