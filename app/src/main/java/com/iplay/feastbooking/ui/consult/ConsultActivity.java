package com.iplay.feastbooking.ui.consult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicActivity;
import com.iplay.feastbooking.component.view.gridview.UnScrollableGridView;
import com.iplay.feastbooking.gson.consult.ConsultVO;
import com.iplay.feastbooking.gson.hotelDetail.BanquetHall;
import com.iplay.feastbooking.gson.hotelDetail.HotelDetail;
import com.iplay.feastbooking.messageEvent.consult.ConsultOrderMessageEvent;
import com.iplay.feastbooking.net.utilImpl.consult.ConsultUtility;
import com.iplay.feastbooking.ui.consult.adapter.DatesListAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/10/21.
 */

public class ConsultActivity extends BasicActivity implements View.OnClickListener, TextWatcher{

    public static final String TAG = "ConsultActivity";

    private static final String HOTEL_KEY = "HOTEL_KEY";

    private HotelDetail hotelDetail;

    private Spinner banquetHallSpinner;

    private TimerPickerDialog dialog;

    private List<List<Date>> flexDates = new ArrayList<>();

    private static final int flexDatesCapacity = 5;

    private UnScrollableGridView dates_list;

    private DatesListAdapter datesAdapter;

    private TextView hotel_name;

    private Button next_btn;

    private EditText table_num_et, recommender_et, linker_et, linker_way_et;

    private ConsultUtility utility;

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

        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);

        table_num_et = (EditText) findViewById(R.id.table_num_et);
        linker_et = (EditText) findViewById(R.id.linker_et);
        linker_way_et = (EditText) findViewById(R.id.linker_way_et);
        recommender_et = (EditText) findViewById(R.id.recommender_et);

        table_num_et.addTextChangedListener(this);
        linker_way_et.addTextChangedListener(this);
        linker_et.addTextChangedListener(this);
        recommender_et.addTextChangedListener(this);

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

        return consultVO;
    }

    public static void start(Context context, HotelDetail hotelDetail){
        Intent intent = new Intent(context,ConsultActivity.class);
        intent.putExtra(HOTEL_KEY, hotelDetail);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        return isETempty(recommender_et) || isETempty(linker_et) || isETempty(linker_way_et) || isETempty(table_num_et);
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
}
