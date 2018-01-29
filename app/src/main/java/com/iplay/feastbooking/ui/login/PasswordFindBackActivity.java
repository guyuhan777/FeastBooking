package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.net.utilImpl.loginUtil.LoginUtility;

/**
 * Created by gu_y-pc on 2018/1/27.
 */

public class PasswordFindBackActivity extends BasicActivity implements View.OnClickListener,TextWatcher {

    private EditText email_et;

    private EditText verify_code_et;

    private Button valid_code_btn;

    private Button next_btn;

    private int defaultRemainSeconds = 60;

    private int remainSeconds = defaultRemainSeconds;

    private Handler timeHandler = new TimerHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

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
        email_et.addTextChangedListener(this);

        verify_code_et = (EditText) findViewById(R.id.verify_code_et);
        verify_code_et.setOnClickListener(this);
        verify_code_et.addTextChangedListener(this);

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
            case R.id.valid_code_btn:
                String mailStr = email_et.getText().toString();
                if(!isEmailValid(mailStr)){
                    Toast.makeText(this, getText(R.string.email_format_invalid),Toast.LENGTH_SHORT).show();
                }else {
                    disableValidButton();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0; i<defaultRemainSeconds; i++){
                                timeHandler.sendEmptyMessage(TimerHandler.TYPE_TIMER);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    LoginUtility.getInstance(this).getTotp(mailStr, this);
                }
                break;
            case R.id.next_btn:

                break;
            default:
                break;
        }
    }

    private boolean isTotpValid(String totp){
        return totp.length() == 6;
    }

    private boolean isEmailValid(String email){
        return !"".equals(email) && email.indexOf('@') > 0;
    }

    private void enableNextButton(){
        next_btn.setEnabled(true);
        next_btn.setBackground(getResources().getDrawable(R.drawable.button1));
    }

    private void disableNextButton(){
        next_btn.setEnabled(false);
        next_btn.setBackground(getResources().getDrawable(R.drawable.button_disable));
    }

    private void enableCodeValidButton(){
        valid_code_btn.setText(getText(R.string.huo_qu_yzm));
        valid_code_btn.setEnabled(true);
        valid_code_btn.setTextColor(getResources().getColor(R.color.deep_grey));
    }

    private void disableValidButton(){
        valid_code_btn.setEnabled(false);
        valid_code_btn.setTextColor(getResources().getColor(R.color.grey));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String email = email_et.getText().toString();
        String totp = verify_code_et.getText().toString();
        if(isEmailValid(email) && isTotpValid(totp)){
            enableNextButton();
        }else {
            disableNextButton();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // do nothing
    }

    private class TimerHandler extends Handler {

        private static final int TYPE_TIMER = 1;

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == TYPE_TIMER){
                remainSeconds--;
                valid_code_btn.setText("重新發送(" + remainSeconds + ")");
            }
            if(remainSeconds == 0){
                enableCodeValidButton();
                remainSeconds = defaultRemainSeconds;
            }
        }
    }
}
