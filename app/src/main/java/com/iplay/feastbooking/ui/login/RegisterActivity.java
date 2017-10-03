package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.ProperTies;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.gson.register.TotpConfirm;
import com.iplay.feastbooking.net.utilImpl.registerUtil.RegisterValidUtility;

import static com.iplay.feastbooking.assistance.ProperTies.TYPE_BTN_WAITING;

/**
 * Created by admin on 2017/9/26.
 */

public class RegisterActivity extends BasicActivity implements View.OnClickListener{

    private RegisterValidUtility utility;

    private View status_bar_fix;

    private Button valid_code_btn;

    private EditText mail_et;

    private EditText code_et;

    private int valid_btn_state = ProperTies.TYPE_BTN_ACTIVE;

    private int next_btn_state = ProperTies.TYPE_BTN_ACTIVE;

    public ValidCodeHandler validHandler = new ValidCodeHandler();

    public NextConfirmHandler confirmHandler = new NextConfirmHandler();

    private RelativeLayout root_view;

    private Button nextButton;

    public void setValid_btn_state(int state){
        this.valid_btn_state = state;
    }

    public void setNext_btn_state(int state){
        this.next_btn_state = state;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.register_layout);
    }

    @Override
    public void findViews() {
        status_bar_fix = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        valid_code_btn = (Button) findViewById(R.id.valid_code_btn);
        valid_code_btn.setOnClickListener(this);
        mail_et = (EditText) findViewById(R.id.email);
        root_view = (RelativeLayout) findViewById(R.id.register_layout_root);
        root_view.setOnClickListener(this);
        nextButton = (Button) findViewById(R.id.next_btn);
        nextButton.setOnClickListener(this);
        code_et = (EditText) findViewById(R.id.verify_code_et);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {
        utility = RegisterValidUtility.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_layout_root:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            case R.id.next_btn:
                final String validCode = code_et.getText().toString().trim();
                final String mailSend = mail_et.getText().toString().trim();
                if(validCode.equals("")){
                    Toast.makeText(this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(mailSend.equals("")){
                    Toast.makeText(this,"邮箱不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    TotpConfirm confirm = new TotpConfirm();
                    confirm.email = mailSend;
                    confirm.totp = validCode;
                    if(next_btn_state == ProperTies.TYPE_BTN_DISABLE || next_btn_state == TYPE_BTN_WAITING){
                        return;
                    }
                    if(next_btn_state == ProperTies.TYPE_BTN_ACTIVE){
                        next_btn_state = TYPE_BTN_WAITING;
                        utility.verify(confirm);
                    }
                }

                break;
            case R.id.valid_code_btn:
                if(valid_btn_state == TYPE_BTN_WAITING || valid_btn_state == ProperTies.TYPE_BTN_DISABLE){
                    return;
                }
                if(valid_btn_state == ProperTies.TYPE_BTN_ACTIVE) {
                    final String mail = mail_et.getText().toString();
                    if (mail.equals("")) {
                        Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (mail.indexOf("@") <= 0) {
                        Toast.makeText(RegisterActivity.this, "邮箱格式不符", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        valid_btn_state = ProperTies.TYPE_BTN_WAITING;
                        utility.applyForRegistrationEmail(mail);
                    }
                }
                break;
        }
    }

    class NextConfirmHandler extends Handler{

        public static final int TYPE_LOSE_CONNECT = 0;

        public static final int TYPE_INVALID_CODE = 1;

        @Override
        public void handleMessage(Message msg) {
            int type = msg.what;
            if (type == TYPE_LOSE_CONNECT){
                Toast.makeText(RegisterActivity.this, "与服务器失去连接", Toast.LENGTH_SHORT).show();
            }else if(type == TYPE_INVALID_CODE){
                Toast.makeText(RegisterActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ValidCodeHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            int seconds = msg.what;
            if(seconds == 61){
                valid_code_btn.setText(getResources().getText(R.string.huo_qu_yzm));
            }else if(seconds >=0 && seconds <= 60){
                valid_code_btn.setText("重新发送(" + seconds + ")");
            }else if(seconds == 99){
                Toast.makeText(RegisterActivity.this,"邮箱已存在",Toast.LENGTH_SHORT).show();
            }else if(seconds == 100){
                valid_code_btn.setText("验证中");
            }
        }
    }
}
