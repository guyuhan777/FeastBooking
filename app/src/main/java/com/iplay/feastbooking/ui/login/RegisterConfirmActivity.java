package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.gson.register.RegisterConfirmRequest;
import com.iplay.feastbooking.net.utilImpl.registerUtil.RegisterConfirmUtility;

import java.util.regex.Pattern;

/**
 * Created by admin on 2017/9/28.
 */

public class RegisterConfirmActivity extends BasicActivity implements View.OnClickListener{

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

    public void setNext_btn_state(int next_btn_state) {
        this.next_btn_state = next_btn_state;
    }

    private int next_btn_state = ProperTies.TYPE_BTN_ACTIVE;

    @Override
    public void setContentView() {
        setContentView(R.layout.register_confirm_layout);
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
        password_et = (EditText) findViewById(R.id.pasword);
        confirm_password_et = (EditText) findViewById(R.id.password_confirm);
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
                String user_name = username_et.getText().toString().trim();
                if(user_name.equals("")){
                    Toast.makeText(RegisterConfirmActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(Pattern.matches(userNamePatternStr, user_name)){
                    Toast.makeText(RegisterConfirmActivity.this,"用户名格式错误",Toast.LENGTH_SHORT).show();
                    return;
                }
                String passwd = password_et.getText().toString();
                if(passwd.equals("")){
                    Toast.makeText(RegisterConfirmActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    String confirmPasswd = confirm_password_et.getText().toString();
                    if(!confirmPasswd.equals(passwd)){
                        Toast.makeText(RegisterConfirmActivity.this,"重复密码错误",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        if(next_btn_state == ProperTies.TYPE_BTN_WAITING || next_btn_state == ProperTies.TYPE_BTN_DISABLE){
                            return;
                        }else {
                            next_btn_state = ProperTies.TYPE_BTN_WAITING;
                            final RegisterConfirmRequest request = new RegisterConfirmRequest();
                            request.password = passwd;
                            request.username = user_name;
                            utility.register(request, token,mail);
                        }

                    }
                }
                break;
        }
    }

}
