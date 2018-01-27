package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;

/**
 * Created by gu_y-pc on 2018/1/27.
 */

public class PasswordFindBackActivity extends BasicActivity implements View.OnClickListener{

    private EditText email_et;

    private EditText verify_code_et;

    private Button valid_code_btn;

    private Button next_btn;

    @Override
    public void setContentView() {
        setContentView(R.layout.forget_password_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.mail_getback_layout_root).setOnClickListener(this);
        email_et = (EditText) findViewById(R.id.email);
        email_et.setOnClickListener(this);

        verify_code_et = (EditText) findViewById(R.id.verify_code_et);
        verify_code_et.setOnClickListener(this);

        valid_code_btn = (Button) findViewById(R.id.valid_code_btn);
        valid_code_btn.setOnClickListener(this);

        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    public static void start(Context context){
        Intent intent = new Intent(context, PasswordFindBackActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mail_getback_layout_root:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            case R.id.cancel:
                finish();
                overridePendingTransition(R.anim.hold,R.anim.top2bottom);
                break;
            default:
                break;
        }
    }
}
