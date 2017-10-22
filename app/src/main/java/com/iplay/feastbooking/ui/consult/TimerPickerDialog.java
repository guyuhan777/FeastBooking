package com.iplay.feastbooking.ui.consult;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;

import com.iplay.feastbooking.R;
import com.squareup.timessquare.CalendarPickerView;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/10/22.
 */

public class TimerPickerDialog extends Dialog implements View.OnClickListener{

    private CalendarPickerView calendar;

    public TimerPickerDialog(@NonNull Context context) {
        super(context);
    }

    public TimerPickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected TimerPickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(View.inflate(getContext(),R.layout.time_picker_layout,null));
        findViewById(R.id.confirm_btn).setOnClickListener(this);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime()).withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.RANGE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_btn:
                List<Date> dates = calendar.getSelectedDates();
                if (dates.size() == 0){
                    return;
                }else {
                    EventBus.getDefault().post(dates);
                    dismiss();
                }
                break;
            default:
                break;
        }
    }
}
