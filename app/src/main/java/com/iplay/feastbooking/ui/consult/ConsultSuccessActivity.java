package com.iplay.feastbooking.ui.consult;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.ui.home.HomeActivity;

/**
 * Created by gu_y-pc on 2017/10/23.
 */

public class ConsultSuccessActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "ConsultSuccessActivity";

    public static void start(Context context){
        Intent intent = new Intent(context,ConsultSuccessActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.consult_success_layout);
    }

    @Override
    public void findViews() {
        findViewById(R.id.status_bar_fix).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.finish_tv).setOnClickListener(this);
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
            case R.id.finish_tv:
                HomeActivity.startHomeActivity(this);
                overridePendingTransition(R.anim.hold,R.anim.top2bottom);
                break;
            default:
                break;
        }
    }
}
