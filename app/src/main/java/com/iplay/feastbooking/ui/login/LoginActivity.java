package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.iplay.feastbooking.messageEvent.activityFinish.ActivityFinishMessageEvent;
import com.iplay.feastbooking.messageEvent.home.LoginMessageEvent;
import com.iplay.feastbooking.messageEvent.home.NoInternetMessageEvent;
import com.iplay.feastbooking.net.utilImpl.loginUtil.LoginUtility;
import com.iplay.feastbooking.ui.home.HomeActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by admin on 2017/10/6.
 */

public class LoginActivity extends BasicActivity implements View.OnClickListener,TextWatcher{

    public static final String TAG = "LoginActivity";

    private EditText userName_et;

    private EditText password_et;

    private Button next_btn;

    private Button cancel_btn;

    private View status_bar_fix;

    private LoginUtility utility;

    private boolean isOnBack = false;

    private static final String ONBACK_KEY = "ONBACK";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.login_layout);
    }

    @Override
    public void findViews() {
        status_bar_fix = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        userName_et = (EditText) findViewById(R.id.username);
        userName_et.addTextChangedListener(this);
        password_et = (EditText) findViewById(R.id.password);
        password_et.addTextChangedListener(this);
        cancel_btn = (Button) findViewById(R.id.cancel);
        cancel_btn.setOnClickListener(this);
        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);
        findViewById(R.id.register_now).setOnClickListener(this);
        findViewById(R.id.login_layout_root).setOnClickListener(this);
    }


    public static void startOnBackActivity(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra(ONBACK_KEY,true);
        context.startActivity(intent);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void getData() {
        utility = LoginUtility.getInstance(this);
        isOnBack = getIntent().getBooleanExtra(ONBACK_KEY, false);
    }

    @Override
    public void showContent() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoInternetMessageEvent(NoInternetMessageEvent event){
        if(event.getType() == NoInternetMessageEvent.TYPE_LOGIN){
            Toast.makeText(this,"網絡不給力",Toast.LENGTH_SHORT).show();
            enableButton();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginMessageEvent(LoginMessageEvent event){
        if(event.getType() == LoginMessageEvent.TYPE_CONNECT_TIME_OUT){
            Toast.makeText(this,"連接超時",Toast.LENGTH_SHORT).show();
            enableButton();
        }else if(event.getType() == LoginMessageEvent.TYPE_FAILURE){
            Toast.makeText(this,"用戶名或密碼錯誤",Toast.LENGTH_SHORT).show();
            enableButton();
        }else if(event.getType() == LoginMessageEvent.TYPE_SUCCESS){
            if(!isOnBack){
                HomeActivity.startHomeActivity(this);
                overridePendingTransition(R.anim.hold,R.anim.top2bottom);
            }else {
                finish();
                overridePendingTransition(R.anim.hold,R.anim.top2bottom);
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityFinishMessageEvent(ActivityFinishMessageEvent event){
        if(event.isExist(TAG)){
            Log.d(TAG,"bye bye");
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_layout_root:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            case R.id.next_btn:
                String username = userName_et.getText().toString();
                String password = password_et.getText().toString();
                disableButton();
                utility.login(username, password, this);
                break;
            case R.id.register_now:
                RegisterActivity.start(this);
                break;
            case R.id.cancel:
                finish();
                overridePendingTransition(R.anim.hold,R.anim.top2bottom);
                break;
            default:
                break;
        }
    }

    private void enableButton(){
        next_btn.setEnabled(true);
        next_btn.setBackground(getResources().getDrawable(R.drawable.button1));
    }

    private void disableButton(){
        next_btn.setEnabled(false);
        next_btn.setBackground(getResources().getDrawable(R.drawable.button_disable));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String user_name_str = userName_et.getText().toString().trim();
        String password_str = password_et.getText().toString().trim();
        if(!(user_name_str.equals("")||password_str.equals(""))){
            enableButton();
        }else{
            disableButton();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
