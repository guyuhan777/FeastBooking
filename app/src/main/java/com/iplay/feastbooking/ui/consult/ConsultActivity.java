package com.iplay.feastbooking.ui.consult;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.DateFormatter;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.component.view.gridview.UnScrollableGridView;
import com.iplay.feastbooking.gson.consult.ConsultVO;
import com.iplay.feastbooking.gson.hotelDetail.BanquetHall;
import com.iplay.feastbooking.gson.hotelDetail.HotelDetail;
import com.iplay.feastbooking.messageEvent.consult.ConsultOrderMessageEvent;
import com.iplay.feastbooking.messageEvent.register.CodeValidMessageEvent;
import com.iplay.feastbooking.net.utilImpl.consult.ConsultUtility;
import com.iplay.feastbooking.ui.consult.adapter.DatesListAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.iplay.feastbooking.R.id.valid_code_btn;

/**
 * Created by admin on 2017/10/21.
 */

public class ConsultActivity extends BasicActivity implements View.OnClickListener, TextWatcher{

    public static final String TAG = "ConsultActivity";

    private static final String HOTEL_KEY = "HOTEL_KEY";

    private Button code_valid_button;

    private HotelDetail hotelDetail;

    private Spinner banquetHallSpinner;

    private TimerPickerDialog dialog;

    private List<List<Date>> flexDates = new ArrayList<>();

    private int defaultSeconds = 60;

    private int remainSeconds;

    private static final int flexDatesCapacity = 5;

    private UnScrollableGridView dates_list;

    private DatesListAdapter datesAdapter;

    private TextView hotel_name;

    private Button next_btn;

    private EditText table_num_et, recommender_et, linker_et, linker_way_et, code_valid_et;

    private ConsultUtility utility;

    private TimerHandler timerHandler = new TimerHandler();

    @Override
    public void setContentView() {
        setContentView(R.layout.consult_layout);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRegistered = true;
        super.onCreate(savedInstanceState);
        utility = ConsultUtility.getInstance(this);
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
    public void findViews() {
        findViewById(R.id.status_bar_fix).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(this)));
        findViewById(R.id.back_iv).setOnClickListener(this);
        findViewById(R.id.root_view).setOnClickListener(this);
        findViewById(R.id.add_date_iv).setOnClickListener(this);

        code_valid_button = (Button) findViewById(R.id.valid_code_btn);
        code_valid_button.setOnClickListener(this);

        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);

        table_num_et = (EditText) findViewById(R.id.table_num_et);
        linker_et = (EditText) findViewById(R.id.linker_et);
        linker_way_et = (EditText) findViewById(R.id.linker_way_et);
        recommender_et = (EditText) findViewById(R.id.recommender_et);
        code_valid_et = (EditText) findViewById(R.id.valid_code_et);

        table_num_et.addTextChangedListener(this);
        linker_way_et.addTextChangedListener(this);
        linker_et.addTextChangedListener(this);
        recommender_et.addTextChangedListener(this);
        code_valid_et.addTextChangedListener(this);

        if(hotelDetail.name != null){
            hotel_name = (TextView) findViewById(R.id.hotel_name);
            hotel_name.setText(hotelDetail.name);
        }
        banquetHallSpinner = (Spinner) findViewById(R.id.banquet_halls_spinner);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, generateBanquetsList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        banquetHallSpinner.setAdapter(adapter);
        dates_list = (UnScrollableGridView) findViewById(R.id.dates_list_sv);
        datesAdapter = new DatesListAdapter(this,R.layout.date_bar,flexDates);
        dates_list.setAdapter(datesAdapter);
    }

    private List<String> generateBanquetsList(){
        List<String> banquetsList = new ArrayList<>();
        List<BanquetHall> banquetHalls = hotelDetail.banquetHalls;
        for (int i=0; i<banquetHalls.size(); i++){
            banquetsList.add(banquetHalls.get(i).name);
        }
        return banquetsList;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDateListMessageEvent(List<Date> dates){
        if(dates != null){
            datesAdapter.add(dates);
            datesAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConsultOrderMessageEvent(ConsultOrderMessageEvent event){
        if(event.getType() == ConsultOrderMessageEvent.TYPE_FAILURE){
            Toast.makeText(this,event.getFailureReason(),Toast.LENGTH_SHORT).show();
        }else if(event.getType() == ConsultOrderMessageEvent.TYPE_SUCCESS){
            if(!event.getResult().success){
                Toast.makeText(this,"推介人不存在",Toast.LENGTH_SHORT).show();
            }else {
                ConsultSuccessActivity.start(this);
                overridePendingTransition(R.anim.bottom2top, R.anim.hold);
            }
        }
    }

    @Override
    public void getData() {
        hotelDetail = (HotelDetail) getIntent().getSerializableExtra(HOTEL_KEY);
    }

    @Override
    public void showContent() {

    }

    private ConsultVO generateConsultVO(){
        ConsultVO consultVO = new ConsultVO();
        consultVO.banquetHallId = hotelDetail.banquetHalls.get(banquetHallSpinner.getSelectedItemPosition()).id;
        consultVO.contact = linker_et.getText().toString().trim();
        consultVO.recommender = recommender_et.getText().toString().trim();
        consultVO.phone = linker_way_et.getText().toString().trim();
        consultVO.tables = Integer.parseInt(table_num_et.getText().toString().trim());
        consultVO.totp = Integer.parseInt(code_valid_et.getText().toString().trim());
        String dates = "";
        for (int i=0;i<flexDates.size();i++){
            List<Date> flexDate = flexDates.get(i);
            if(flexDate.size() == 1){
                dates += DateFormatter.formatDate(flexDate.get(0)) +";";
            }else if(flexDate.size() >= 2){
                dates += DateFormatter.formatDate(flexDate.get(0)) + "-" + DateFormatter.formatDate(flexDate.get(1)) + ";";
            }
        }
        consultVO.candidateDates = dates;
        return consultVO;
    }

    public static void start(Context context, HotelDetail hotelDetail){
        Intent intent = new Intent(context,ConsultActivity.class);
        intent.putExtra(HOTEL_KEY, hotelDetail);
        context.startActivity(intent);
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
            case CodeValidMessageEvent.TYPE_UNKNOWN_ERROR:
                Toast.makeText(this,"未知錯誤",Toast.LENGTH_SHORT).show();
                enableValidButton();
                break;
            case CodeValidMessageEvent.TYPE_SUCCESS:
                remainSeconds = defaultSeconds;
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

    private void enableValidButton(){
        code_valid_button.setText(getResources().getString(R.string.huo_qu_yzm));
        code_valid_button.setEnabled(true);
        code_valid_button.setTextColor(getResources().getColor(R.color.deep_grey));

    }

    private void disableValidButton(){
        code_valid_button.setEnabled(false);
        code_valid_button.setTextColor(getResources().getColor(R.color.grey));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case valid_code_btn:
                disableValidButton();
                code_valid_button.setText("請稍候");
                utility.requireForConsultTotp(this);
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
                if(flexDates.size() == 0){
                    Toast.makeText(this,"請選擇候選日期",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    utility.createConsultOrder(generateConsultVO());
                }
                break;
            case R.id.root_view:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            case R.id.add_date_iv:
                if(flexDates.size() >= flexDatesCapacity ){
                    Toast.makeText(this,"只能添加5個候補日期",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(dialog == null) {
                    dialog = new TimerPickerDialog(this);
                }
                dialog.show();
                break;
            default:
                break;
        }
    }

    private boolean isEditTextInValid(){
        return isETempty(recommender_et) || isETempty(linker_et) || isETempty(linker_way_et) || isETempty(table_num_et) || isETempty(code_valid_et);
    }

    private boolean isETempty(EditText editText){
        return editText.getText() == null || editText.getText().toString().trim().equals("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isEditTextInValid()) {
            disableButton();
        } else {
            enableButton();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class TimerHandler extends Handler {

        private static final int TYPE_TIMER = 1;

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == TYPE_TIMER){
                remainSeconds--;
                code_valid_button.setText("重新發送(" + remainSeconds + ")");
            }
            if(remainSeconds == 0){
                enableValidButton();
                remainSeconds = defaultSeconds;
            }
        }
    }
}
