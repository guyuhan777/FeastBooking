package com.iplay.feastbooking.ui.self;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.ui.home.HomeActivity;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/10/19.
 */

public class SelfInfoActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "SelfInfoActivity";

    @Override
    public void setContentView() {
        setContentView(R.layout.self_info_detail);
    }

    @Override
    public void findViews() {
        View status_bar_fix = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        RelativeLayout log_out_btn = (RelativeLayout) findViewById(R.id.log_out_btn);
        log_out_btn.setOnClickListener(this);
    }

    public static void start(Context context){
        context.startActivity(new Intent(context,SelfInfoActivity.class));
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
            case R.id.log_out_btn:
                DataSupport.deleteAll(UserDao.class,"isLogin = ?","" + 1);
                LoginUserHolder.getInstance().removeCurrentUser();
                HomeActivity.startActivity(this);
                finish();
                break;
            default:
                break;
        }
    }
}
