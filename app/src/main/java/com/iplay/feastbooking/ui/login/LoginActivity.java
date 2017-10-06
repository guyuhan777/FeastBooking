package com.iplay.feastbooking.ui.login;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Created by admin on 2017/10/6.
 */

public class LoginActivity extends BasicActivity implements View.OnClickListener,TextWatcher{

    private EditText userName_et;

    private EditText password_et;

    private Button next_btn;

    private Button cancel_btn;

    private View status_bar_fix;

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
        next_btn = (Button) findViewById(R.id.next_btn);
        findViewById(R.id.login_layout_root).setOnClickListener(this);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_layout_root:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            case R.id.next_btn:

                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        String user_name_str = userName_et.getText().toString().trim();
        String password_str = password_et.getText().toString().trim();
        if(!(user_name_str.equals("")||password_str.equals(""))){
            next_btn.setEnabled(true);
            next_btn.setBackground(getResources().getDrawable(R.drawable.button1));
        }else{
            next_btn.setEnabled(false);
            next_btn.setBackground(getResources().getDrawable(R.drawable.button_disable));
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
