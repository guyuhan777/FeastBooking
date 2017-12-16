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
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.dto.UserDto;
import com.iplay.feastbooking.gson.selfInfo.SelfInfo;
import com.iplay.feastbooking.messageEvent.selfInfo.ChangePhoneMessageEvent;
import com.iplay.feastbooking.messageEvent.selfInfo.SelfInfoMessageEvent;
import com.iplay.feastbooking.net.utilImpl.selfDetail.ChangeSelfInfoUtility;
import com.iplay.feastbooking.ui.home.HomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by gu_y-pc on 2017/12/16.
 */

public class SelfChangePhoneActivity extends BasicActivity implements View.OnClickListener{

    private TextView submit_tv;

    private EditText phone_et;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.self_change_user_phone_layout);
    }

    @Override
    public void findViews() {
        View status_bar_fix = findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.root_view).setOnClickListener(this);
        findViewById(R.id.back_iv).setOnClickListener(this);
        submit_tv = (TextView) findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);
        phone_et = (EditText) findViewById(R.id.new_phone_et);
    }

    public static void start(Context context){
        Intent intent = new Intent(context, SelfChangePhoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelfChangePhoneMessageEvent(ChangePhoneMessageEvent event){
        if(event.getType() == ChangePhoneMessageEvent.TYPE.FAILURE){
            Toast.makeText(this, event.getFailureResult(), Toast.LENGTH_SHORT).show();
        }else if(event.getType() == ChangePhoneMessageEvent.TYPE.SUCCESS){
            String phone = event.getPhone();
            UserDto currentUser = UserDao.getInstance().getLoginUser();
            if(currentUser != null) {
                currentUser.setPhone(phone);
                currentUser.save();
            }
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            SelfInfoMessageEvent selfInfoMessageEvent = new SelfInfoMessageEvent();
            SelfInfo selfInfo = new SelfInfo();
            selfInfo.phone = phone;
            selfInfoMessageEvent.setSelfInfo(selfInfo);
            EventBus.getDefault().post(selfInfoMessageEvent);
            HomeActivity.startHomeActivity(this, HomeActivity.START_TYPE.SELF);
        }
        submit_tv.setEnabled(true);
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
                String phone = phone_et.getText().toString().trim();
                if(phone.equals("")){
                    Toast.makeText(this, "請輸入手機號", Toast.LENGTH_SHORT).show();
                    return;
                }
                submit_tv.setEnabled(false);
                ChangeSelfInfoUtility.getInstance(this).updatePhone(phone, this);
                break;
            default:
                break;
        }
    }
}
