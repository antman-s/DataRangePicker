package com.supreme.picker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.supreme.picker.entity.Date;
import com.supreme.picker.listener.OnDateClickedListener;


public class DateRangePickerDialog extends Dialog {

    private Date startDate;
    private Date endDate;

    private CalendarView calendarView;
    private OnDateClickedListener listener;
    private View.OnClickListener clickListener;

    DateRangePickerDialog(@NonNull Context context, Date start, Date end) {
        super(context, R.style.BottomDialog);
        this.startDate = start;
        this.endDate = end;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_date_range_picker);
        calendarView = findViewById(R.id.calendar_view);
        initView();

        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        if (window == null)
            return;

        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    void setOnClickListener(View.OnClickListener clickListener){
        this.clickListener = clickListener;
    }

    void setDateClickedListener(OnDateClickedListener listener) {
        this.listener = listener;
    }

    private void initView() {
        calendarView.setOnDateClickedListener(listener);
        calendarView.setRangeTime(startDate, endDate);

        findViewById(R.id.btn_search).setOnClickListener(clickListener);
    }
}
