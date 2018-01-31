package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;

/**
 * Created by koishi on 18-1-31.
 */

public class SetNewPasswordActivity extends BasicActivity implements View.OnClickListener{


    private EditText new_password_et;

    private EditText new_password_confirm_et;

    private Button next_btn;

    private static final String EMAIL_KEY = "EMAIL_KEY", TOKEN_KEY = "TOKEN_KEY";

    private String email;

    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.reset_password_layout);
    }

    public static void start(Context context, String email, String token){
        Intent intent = new Intent(context, SetNewPasswordActivity.class);
        intent.putExtra(EMAIL_KEY, email).putExtra(TOKEN_KEY, token);
        context.startActivity(intent);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        new_password_et = (EditText) findViewById(R.id.new_email_et);
        new_password_confirm_et = (EditText) findViewById(R.id.new_password_confirm_et);
    }

    @Override
    public void getData() {
        email = getIntent().getStringExtra(EMAIL_KEY);
        token = getIntent().getStringExtra(TOKEN_KEY);
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
            default:
                break;
        }
    }

}
