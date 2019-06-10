package com.supreme.picker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.supreme.picker.entity.Date;
import com.supreme.picker.listener.OnDateClickedListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Date startDate;
    private Date endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        startDate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        endDate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        final DateRangePickerDialog dialog = new DateRangePickerDialog(this, startDate, endDate);
        dialog.setDateClickedListener(new OnDateClickedListener() {

            @Override
            public void onDateRangeSelected(Date start, Date end) {

                startDate = start;
                endDate = end;

                updateDateRangeText(dialog);

                if (end != null) {
                    dialog.findViewById(R.id.btn_search).setEnabled(true);
                } else {
                    dialog.findViewById(R.id.btn_search).setEnabled(false);
                }
            }
        });

        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        updateDateRangeText(dialog);
    }

    private void updateDateRangeText(DateRangePickerDialog dialog) {
        TextView tvStart = dialog.findViewById(R.id.tv_start_date);
        TextView tvEnd = dialog.findViewById(R.id.tv_end_date);
        String strStart, strEnd;
        strStart = (startDate.getMonth() + 1) + "月" + startDate.getDayOfMonth() + "日\n" + getWeek(startDate);
        if (endDate != null)
            strEnd = (endDate.getMonth() + 1) + "月" + endDate.getDayOfMonth() + "日\n" + getWeek(endDate);
        else
            strEnd = "开始";

        tvStart.setText(strStart);
        tvEnd.setText(strEnd);
    }

    private String getWeek(Date date) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth());
        int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }
}
