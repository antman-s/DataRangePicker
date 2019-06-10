package com.supreme.picker;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supreme.picker.adapter.CalendarAdapter;
import com.supreme.picker.entity.Date;
import com.supreme.picker.listener.OnDateClickedListener;
import com.supreme.picker.recyclerview.PageRecyclerView;
import com.supreme.picker.recyclerview.SpeedScrollLinearLayoutManager;
import com.supreme.picker.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class CalendarView extends LinearLayout {

    private LayoutInflater layoutInflater;

    private ImageButton iBtnLastMonth;
    private ImageButton iBtnNextMonth;
    private TextView tvTitle;
    private PageRecyclerView rcvMonth;
    private LinearSnapHelper snapHelper;

    private CalendarAdapter calendarAdapter;

    private int currentPosition = 1;

    private SimpleDateFormat dateFormat;
    private Date currentDate;

    public static Date startDate, endDate;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        layoutInflater = LayoutInflater.from(getContext());

        initTitleBar();

        initWeekBar();

        initRCVMonth();

        setTitleFormat();

        // scroll to current date
        rcvMonth.scrollToPosition(currentPosition);
        showTitle(calendarAdapter.getDateList().get(currentPosition));
    }


    private void initTitleBar() {

        View vTitleLayout = layoutInflater.inflate(R.layout.layout_calendar_title, this, true);
        tvTitle = vTitleLayout.findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.setTextColor(Color.parseColor("#337DFE"));
        }

        ChangeMonthEvent changeMonthEvent = new ChangeMonthEvent();

        iBtnLastMonth = vTitleLayout.findViewById(R.id.btn_last_month);
        if (iBtnLastMonth != null) {
            iBtnLastMonth.setOnClickListener(changeMonthEvent);
        }

        iBtnNextMonth = vTitleLayout.findViewById(R.id.btn_next_month);
        if (iBtnNextMonth != null) {
            iBtnNextMonth.setOnClickListener(changeMonthEvent);
        }
    }

    // initialize week bar
    private void initWeekBar() {
        String[] weekArr = {"日", "一", "二", "三", "四", "五", "六"};

        LinearLayout weekLayout = new LinearLayout(getContext());
        weekLayout.setPadding(0, (int) getResources().getDimension(R.dimen.space_10), 0, (int) getResources().getDimension(R.dimen.space_10));
        addView(weekLayout);

        TextView tvWeek;
        for (int i = 0; i < 7; i++) {
            tvWeek = new TextView(getContext());
            tvWeek.setGravity(Gravity.CENTER);
            tvWeek.setText(weekArr[i]);
            tvWeek.setTextSize(12);
            tvWeek.setTextColor(Color.parseColor("#303030"));
            weekLayout.addView(tvWeek, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
        }
    }

    // initialize RecyclerView for date.
    private void initRCVMonth() {

        calendarAdapter = new CalendarAdapter(getContext(), DateUtil.currentMonth());

        rcvMonth = new PageRecyclerView(getContext());
        rcvMonth.addOnScrollListener(new RCVMonthScrollListener());
        SpeedScrollLinearLayoutManager manager = new SpeedScrollLinearLayoutManager(getContext(), HORIZONTAL, false);
        manager.fastScroll();
        rcvMonth.setLayoutManager(manager);
        rcvMonth.setAdapter(calendarAdapter);

        // center item when scroll idle
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcvMonth);

        addView(rcvMonth, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void setTitleFormat() {
        dateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        if (currentDate != null) {
            showTitle(currentDate);
        }
    }

    /**
     * Show title
     *
     * @param date date for title
     */
    private void showTitle(Date date) {
        currentDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth());
        tvTitle.setText(dateFormat.format(calendar.getTime()));
    }

    public void setRangeTime(Date start, Date end) {
        startDate = start;
        endDate = end;
    }

    /**
     * Set listener for date be clicked.
     *
     * @param onDateClickedListener listener
     */
    public void setOnDateClickedListener(final OnDateClickedListener onDateClickedListener) {
        calendarAdapter.setOnDateClickedListener(new OnDateClickedListener() {
            @Override
            public void onDateRangeSelected(Date start, Date end) {
                calendarAdapter.notifyDataSetChanged();
                onDateClickedListener.onDateRangeSelected(start, end);
            }
        });
    }


    /**
     * Set the button be used to change month enable or not.
     *
     * @param enable button enable
     */
    private void setChangeMonthButtonEnable(boolean enable) {
        if (iBtnLastMonth != null) {
            iBtnLastMonth.setEnabled(enable);
        }
        if (iBtnNextMonth != null) {
            iBtnNextMonth.setEnabled(enable);
        }
    }

    // A listener, call back when month change.
    private class ChangeMonthEvent implements OnClickListener {

        @Override
        public void onClick(View view) {

            setChangeMonthButtonEnable(false);

            if (view == iBtnLastMonth) {
                rcvMonth.smoothScrollToPosition(currentPosition - 1);
            } else if (view == iBtnNextMonth) {
                rcvMonth.smoothScrollToPosition(currentPosition + 1);
            }
        }

    }


    private class RCVMonthScrollListener extends RecyclerView.OnScrollListener {

        boolean isLocating;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (newState != SCROLL_STATE_IDLE || isLocating) {
                isLocating = false;
                return;
            }

            isLocating = true;

            int position = recyclerView.getChildLayoutPosition(snapHelper.findSnapView(recyclerView.getLayoutManager()));
            if (currentPosition == position) {
                return;
            }
            currentPosition = position;
            Date date = calendarAdapter.getDateList().get(currentPosition);
            showTitle(date);

            // add new month if position exceed limit
            if (currentPosition == 0) {
                calendarAdapter.addNewLastMonth();
                currentPosition = 1;
            } else if (currentPosition >= calendarAdapter.getItemCount() - 1) {
                calendarAdapter.addNewNextMonth();
            }

            setChangeMonthButtonEnable(true);
        }
    }

}