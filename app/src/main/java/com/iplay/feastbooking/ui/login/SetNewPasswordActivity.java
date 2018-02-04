package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.messageEvent.selfInfo.ChangePasswordMessageEvent;
import com.iplay.feastbooking.net.utilImpl.loginUtil.LoginUtility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by koishi on 18-1-31.
 */

public class SetNewPasswordActivity extends BasicActivity implements View.OnClickListener,TextWatcher{


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
        findViewById(R.id.root_layout).setOnClickListener(this);
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);

        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);

        new_password_et = (EditText) findViewById(R.id.new_pasword_et);
        new_password_et.addTextChangedListener(this);

        new_password_confirm_et = (EditText) findViewById(R.id.new_password_confirm_et);
        new_password_confirm_et.addTextChangedListener(this);
    }

    @Override
    public void getData() {
        email = getIntent().getStringExtra(EMAIL_KEY);
        token = getIntent().getStringExtra(TOKEN_KEY);
    }

    @Override
    public void showContent() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangePasswordMessageEvent(ChangePasswordMessageEvent event){
        enableNextButton();
        if(event.getType() == ChangePasswordMessageEvent.TYPE.SUCCESS){
            Toast.makeText(this, getText(R.string.change_password_success), Toast.LENGTH_SHORT).show();
            LoginActivity.startActivity(this);
        }else {
            Toast.makeText(this, event.getFailureResult(), Toast.LENGTH_SHORT).show();
        }
    }

    private void enableNextButton(){
        next_btn.setEnabled(true);
        next_btn.setBackground(getResources().getDrawable(R.drawable.button1));
    }

    private void disableNextButton(){
        next_btn.setEnabled(false);
        next_btn.setBackground(getResources().getDrawable(R.drawable.button_disable));
    }

    private void onNextBtnClick(){
        disableNextButton();
        String newPasswordStr = new_password_et.getText().toString();
        String newPasswordConfirmStr = new_password_confirm_et.getText().toString();
        if(!newPasswordStr.equals(newPasswordConfirmStr)){
            Toast.makeText(this, getText(R.string.password_not_consistent), Toast.LENGTH_SHORT).show();
        }else {
            LoginUtility.getInstance(this).changePassword(this, newPasswordStr, token);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.root_layout:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            case R.id.back_iv:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
                break;
            case R.id.next_btn:
                onNextBtnClick();
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String password = new_password_et.getText().toString();
        String confirmPassword = new_password_confirm_et.getText().toString();
        if(!password.trim().equals("") && !confirmPassword.trim().equals("")){
            enableNextButton();
        }else {
            disableNextButton();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
