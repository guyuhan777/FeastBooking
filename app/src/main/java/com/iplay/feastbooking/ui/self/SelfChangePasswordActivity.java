package com.iplay.feastbooking.ui.self;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;

/**
 * Created by gu_y-pc on 2017/12/16.
 */

public class SelfChangePasswordActivity extends BasicActivity implements View.OnClickListener{

    private TextView submit_tv;

    private EditText old_pw_et;

    private EditText new_pw_et;

    private EditText confirm_pw_et;

    public static void start(Context context){
        Intent intent = new Intent(context, SelfChangePasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.self_change_user_password_layut);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.root_view).setOnClickListener(this);
        findViewById(R.id.back_iv).setOnClickListener(this);
        submit_tv = (TextView) findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);
        old_pw_et = (EditText) findViewById(R.id.old_pw_et);
        new_pw_et = (EditText) findViewById(R.id.new_pw_et);
        confirm_pw_et = (EditText) findViewById(R.id.confirm_pw_et);
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
            case R.id.root_view:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            case R.id.submit_tv:
                submit_tv.setEnabled(false);
                break;
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
}
