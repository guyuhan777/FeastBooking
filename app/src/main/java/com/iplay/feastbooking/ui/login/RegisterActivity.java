package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.iplay.feastbooking.messageEvent.register.CodeValidMessageEvent;
import com.iplay.feastbooking.messageEvent.register.RegisterMessageEvent;
import com.iplay.feastbooking.net.utilImpl.registerUtil.RegisterValidUtility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.iplay.feastbooking.R.id.next_btn;

/**
 * Created by admin on 2017/9/26.
 */

public class RegisterActivity extends BasicActivity implements View.OnClickListener{

    public static final String TAG = "RegisterActivity";

    private RegisterValidUtility utility;

    private View status_bar_fix;

    private Button valid_code_btn;

    private EditText mail_et;

    private EditText code_et;

    private RelativeLayout root_view;

    private Button nextButton;

    private int defaultSeconds = 60;

    private int remainSeconds;

    private Handler timerHandler = new TimerHandler();

    private TextWatcher nextButtonTW = new NextButtonTextWatcher();

    private TextWatcher validCodeTW = new ValidCodeTextWatcher();

    public static void start(Context context){
        context.startActivity(new Intent(context,RegisterActivity.class));
    }

    @Override
    public void setContentView() {
        isRegistered = true;
        setContentView(R.layout.register_layout);
    }

    private void initRemainSeconds(){
        remainSeconds = defaultSeconds;
    }

    @Override
    public void findViews() {
        status_bar_fix = (View) findViewById(R.id.status_bar_fix_title);
        status_bar_fix.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        valid_code_btn = (Button) findViewById(R.id.valid_code_btn);
        valid_code_btn.setOnClickListener(this);
        mail_et = (EditText) findViewById(R.id.email);
        mail_et.addTextChangedListener(nextButtonTW);
        mail_et.addTextChangedListener(validCodeTW);
        root_view = (RelativeLayout) findViewById(R.id.register_layout_root);
        root_view.setOnClickListener(this);
        nextButton = (Button) findViewById(next_btn);
        nextButton.setOnClickListener(this);
        code_et = (EditText) findViewById(R.id.verify_code_et);
        code_et.addTextChangedListener(nextButtonTW);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.login_now).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityFinishMessageEvent(ActivityFinishMessageEvent event){
        if(event.isExist(TAG)){
            Log.d(TAG,"bye bye");
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterMessageEvent(RegisterMessageEvent event){
        switch (event.getType()){
            case RegisterMessageEvent.TYPE_CONNECT_TIME_OUT:
                Toast.makeText(this,"連接超時",Toast.LENGTH_SHORT).show();
                enableNextButton();
                break;
            case RegisterMessageEvent.TYPE_NO_INTERNET:
                Toast.makeText(this,"網絡不給力",Toast.LENGTH_SHORT).show();
                enableNextButton();
                break;
            case RegisterMessageEvent.TYPE_UNKNOWN_ERROR:
                Toast.makeText(this,"未知錯誤",Toast.LENGTH_SHORT).show();
                enableNextButton();
                break;
            case RegisterMessageEvent.TYPE_FAILURE:
                Toast.makeText(this,"驗證碼錯誤",Toast.LENGTH_SHORT).show();
                enableNextButton();
                break;
            default:
                break;
        }
        mail_et.setEnabled(true);
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
    public void getData() {
        initRemainSeconds();
    }

    private void enableNextButton(){
        String mail = mail_et.getText().toString();
        String valid_code = code_et.getText().toString();
        if(mail.equals("")||valid_code.equals("")) {
            return;
        }
        nextButton.setEnabled(true);
        nextButton.setBackground(getResources().getDrawable(R.drawable.button1));

    }

    private void disableNextButton(){
        nextButton.setEnabled(false);
        nextButton.setBackground(getResources().getDrawable(R.drawable.button_disable));
    }

    private void enableValidButton(){
        valid_code_btn.setText(getResources().getString(R.string.huo_qu_yzm));
        String mail = mail_et.getText().toString().trim();
        if(mail.equals("") || !mail.contains("@")){
            return;
        }
        valid_code_btn.setEnabled(true);
        valid_code_btn.setTextColor(getResources().getColor(R.color.deep_grey));

    }

    private void disableValidButton(){
        valid_code_btn.setEnabled(false);
        valid_code_btn.setTextColor(getResources().getColor(R.color.grey));
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
            case next_btn:
                String validCode = code_et.getText().toString().trim();
                String email = mail_et.getText().toString().trim();
                utility.verify(email, validCode, this);
                break;
            case R.id.valid_code_btn:
                valid_code_btn.setText("驗證中");
                disableValidButton();
                utility.applyForRegistrationEmail(mail_et.getText().toString().trim(), this);
                break;
            case R.id.login_now:
                LoginActivity.startActivity(this);
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }


    private class NextButtonTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String mail = mail_et.getText().toString();
            String valid_code = code_et.getText().toString();
            if(!(mail.equals("")||valid_code.equals(""))){
                enableNextButton();
            }else {
                disableNextButton();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class ValidCodeTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(remainSeconds == defaultSeconds){
                String mail = mail_et.getText().toString().trim();
                if(mail.equals("") || !mail.contains("@")){
                    disableValidButton();
                }else {
                    enableValidButton();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class TimerHandler extends Handler{

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
