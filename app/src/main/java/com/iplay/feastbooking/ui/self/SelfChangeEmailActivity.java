package com.iplay.feastbooking.ui.self;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.dto.UserDto;
import com.iplay.feastbooking.gson.selfInfo.SelfInfo;
import com.iplay.feastbooking.messageEvent.register.CodeValidMessageEvent;
import com.iplay.feastbooking.messageEvent.register.RegisterMessageEvent;
import com.iplay.feastbooking.messageEvent.selfInfo.ChangeEmailConfirmMessageEvent;
import com.iplay.feastbooking.messageEvent.selfInfo.SelfInfoMessageEvent;
import com.iplay.feastbooking.net.utilImpl.registerUtil.RegisterValidUtility;
import com.iplay.feastbooking.net.utilImpl.selfDetail.ChangeSelfInfoUtility;
import com.iplay.feastbooking.ui.home.HomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by gu_y-pc on 2017/12/10.
 */

public class SelfChangeEmailActivity extends BasicActivity implements View.OnClickListener{

    private EditText new_email_et;

    private Button valid_code_btn;

    private EditText valid_code_et;

    private int defaultSeconds = 60;

    private int remainSeconds = defaultSeconds;

    private Handler timerHandler = new TimerHandler();

    private TextView submit_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.self_change_user_email_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.root_view).setOnClickListener(this);
        findViewById(R.id.back_iv).setOnClickListener(this);
        submit_tv = (TextView) findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);

        new_email_et = (EditText) findViewById(R.id.new_email_et);
        valid_code_et = (EditText) findViewById(R.id.valid_code_et);
        valid_code_btn = (Button) findViewById(R.id.valid_code_btn);
        valid_code_btn.setOnClickListener(this);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    public static void start(Context context){
        Intent intent = new Intent(context, SelfChangeEmailActivity.class);
        context.startActivity(intent);
    }

    private void enableValidButton(){
        valid_code_btn.setText(getResources().getString(R.string.huo_qu_yzm));
        valid_code_btn.setEnabled(true);
        valid_code_btn.setTextColor(getResources().getColor(R.color.deep_grey));
    }

    private void disableValidButton(){
        valid_code_btn.setEnabled(false);
        valid_code_btn.setTextColor(getResources().getColor(R.color.grey));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterMessageEvent(RegisterMessageEvent event){
        switch (event.getType()){
            case RegisterMessageEvent.TYPE_CONNECT_TIME_OUT:
                Toast.makeText(this,"連接超時",Toast.LENGTH_SHORT).show();
                submit_tv.setEnabled(true);
                break;
            case RegisterMessageEvent.TYPE_NO_INTERNET:
                Toast.makeText(this,"網絡不給力",Toast.LENGTH_SHORT).show();
                submit_tv.setEnabled(true);
                break;
            case RegisterMessageEvent.TYPE_UNKNOWN_ERROR:
                Toast.makeText(this,"未知錯誤",Toast.LENGTH_SHORT).show();
                submit_tv.setEnabled(true);
                break;
            case RegisterMessageEvent.TYPE_FAILURE:
                Toast.makeText(this,"驗證碼錯誤",Toast.LENGTH_SHORT).show();
                submit_tv.setEnabled(true);
                break;
            case RegisterMessageEvent.TYPE_SUCCESS:
                String token = event.getToken();
                String email = event.getEmail();
                ChangeSelfInfoUtility.getInstance(this).confirmToken(token, email, this);
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeEmailConfirmMessageEvent(ChangeEmailConfirmMessageEvent event){
        if(event.getType() == ChangeEmailConfirmMessageEvent.TYPE.FAILURE){
            Toast.makeText(this, event.getFailureResult(), Toast.LENGTH_SHORT).show();
            submit_tv.setEnabled(true);
        }else if(event.getType() == ChangeEmailConfirmMessageEvent.TYPE.SUCCESS){
            String email = event.getEmail();
            UserDto currentUser = UserDao.getInstance().getLoginUser();
            if(currentUser != null) {
                currentUser.setEmail(email);
                currentUser.save();
            }
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            SelfInfoMessageEvent selfInfoMessageEvent = new SelfInfoMessageEvent();
            SelfInfo selfInfo = new SelfInfo();
            selfInfo.email = email;
            selfInfoMessageEvent.setSelfInfo(selfInfo);
            EventBus.getDefault().post(selfInfoMessageEvent);
            HomeActivity.startHomeActivity(this, HomeActivity.START_TYPE.SELF);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeValidMessageEvent(CodeValidMessageEvent event){
        switch (event.getType()){
            case CodeValidMessageEvent.TYPE_CONNECT_TIME_OVER:
                Toast.makeText(this,"連接超時",Toast.LENGTH_SHORT).show();
                enableValidButton();
                break;
            case CodeValidMessageEvent.TYPE_NO_INTERNET:
                Toast.makeText(this,"網絡不給力",Toast.LENGTH_SHORT).show();
                enableValidButton();
                break;
            case CodeValidMessageEvent.TYPE_EMAIL_ALREADY_EXIST:
                Toast.makeText(this,"郵箱已存在",Toast.LENGTH_SHORT).show();
                enableValidButton();
                break;
            case CodeValidMessageEvent.TYPE_UNKNOWN_ERROR:
                Toast.makeText(this,"未知錯誤",Toast.LENGTH_SHORT).show();
                enableValidButton();
                break;
            case CodeValidMessageEvent.TYPE_SUCCESS:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<defaultSeconds; i++){
                            timerHandler.sendEmptyMessage(TimerHandler.TYPE_TIMER);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.root_view:
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
            case R.id.submit_tv:
                String email = new_email_et.getText().toString().trim();
                String totp = valid_code_et.getText().toString().trim();
                if(email.equals("") || totp.equals("")){
                    Toast.makeText(this, "請輸入郵箱/驗證碼", Toast.LENGTH_SHORT).show();
                    return;
                }
                submit_tv.setEnabled(false);
                ChangeSelfInfoUtility.getInstance(this).verify(email, totp, this);
                break;
            case R.id.valid_code_btn:
                String validEmail = new_email_et.getText().toString().trim();
                if(validEmail.equals("")){
                    Toast.makeText(this, "郵箱不能爲空", Toast.LENGTH_SHORT).show();
                }else {
                    valid_code_btn.setEnabled(false);
                    valid_code_btn.setText("驗證中");
                    RegisterValidUtility.getInstance(this).applyForRegistrationEmail(validEmail, this);
                    disableValidButton();
                }
                break;
        }
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
                enableValidButton();
                remainSeconds = defaultSeconds;
            }
        }
    }

}
