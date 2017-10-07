package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.messageEvent.activityFinish.ActivityFinishMessageEvent;
import com.iplay.feastbooking.net.utilImpl.registerUtil.RegisterConfirmUtility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by admin on 2017/9/28.
 */

public class RegisterConfirmActivity extends BasicActivity implements View.OnClickListener,TextWatcher{

    public static final String TAG = "RegisterConfirmActivity";

    private View status_bar_fix;

    private ImageView back_iv;

    private String token;

    private String mail;

    private Button next_btn;

    private EditText username_et;

    private EditText password_et;

    private EditText confirm_password_et;

    public static final String TOKEN_KEY = "token";

    public static final String MAIL_KEY = "mail";

    private String userNamePatternStr = ".*[^a-zA-Z0-9]+.*";

    private RegisterConfirmUtility utility;

    private void enableNextButton(){
        if(isInValid()){
            return;
        }
        next_btn.setEnabled(true);
        next_btn.setBackground(getResources().getDrawable(R.drawable.button1));
    }

    private void disableNextButton(){
        next_btn.setEnabled(false);
        next_btn.setBackground(getResources().getDrawable(R.drawable.button_disable));
    }

    private boolean isInValid(){
        String username = username_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        String confirmPassword = confirm_password_et.getText().toString().trim();
        return password.equals("")||confirmPassword.equals("")||username.equals("")||username.matches(userNamePatternStr);
    }

    @Override
    public void setContentView() {
        isRegistered = true;
        setContentView(R.layout.register_confirm_layout);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityFinishMessageEvent(ActivityFinishMessageEvent event){
        if(event.isExist(TAG)){
            Log.d(TAG,"bye bye");
            finish();
        }
    }

    @Override
    public void findViews() {
        status_bar_fix = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.register_confirm_layout_root).setOnClickListener(this);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);
        username_et = (EditText) findViewById(R.id.user_name);
        username_et.addTextChangedListener(this);
        password_et = (EditText) findViewById(R.id.pasword);
        password_et.addTextChangedListener(this);
        confirm_password_et = (EditText) findViewById(R.id.password_confirm);
        password_et.addTextChangedListener(this);
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        token = intent.getStringExtra(TOKEN_KEY);
        mail = intent.getStringExtra(MAIL_KEY);
    }

    @Override
    public void showContent() {
        utility = RegisterConfirmUtility.getInstance(this);
    }

    public static void start(Context context, String token, String mail){
        Intent intent = new Intent(context,RegisterConfirmActivity.class);
        intent.putExtra(TOKEN_KEY,token);
        intent.putExtra(MAIL_KEY,mail);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_confirm_layout_root:
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
                disableNextButton();
                String password = password_et.getText().toString().trim();
                String repeatPassword = confirm_password_et.getText().toString().trim();
                String user_name = username_et.getText().toString().trim();
                if(!password.equals(repeatPassword)){
                    Toast.makeText(this,"重復密碼錯誤",Toast.LENGTH_SHORT).show();
                    enableNextButton();
                }else {

                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(isInValid()){
            disableNextButton();
        }else {
            enableNextButton();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
