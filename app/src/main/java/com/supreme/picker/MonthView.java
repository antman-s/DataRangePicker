
package com.supreme.picker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.supreme.picker.annotation.Month;
import com.supreme.picker.entity.Date;
import com.supreme.picker.listener.OnDateClickedListener;
import com.supreme.picker.util.DateUtil;


public class MonthView extends GridLayout {

    public static final int ROW_MAX_COUNT = 6;
    public static final int COLUMN_COUNT = 7;

    private int year;
    @Month
    private int month;

    private int firstWeekOfMonth;
    private int dayCountOfMonth;

    private final int itemCount = ROW_MAX_COUNT * COLUMN_COUNT;
    private OnDateClickedListener onDateClickedListener;

    public MonthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context) {
        this(context, null);
    }

    private void init() {

        setWillNotDraw(false); // if not call this code, not call onDraw(canvas)

        setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        setRowCount(ROW_MAX_COUNT);
        setColumnCount(COLUMN_COUNT);
    }

    /**
     * Set date to display.
     *
     * @param year  year of date
     * @param month month of date，0 to 11
     */
    public void setMonth(int year, @Month int month) {
        this.year = year;
        this.month = month;

        firstWeekOfMonth = DateUtil.getFirstWeekOfMonth(year, month);
        dayCountOfMonth = DateUtil.getDayCountOfMonth(year, month);

        initAllDayItem();
    }

    /**
     * Set listener for item clicked.
     *
     * @param onDateClickedListener listener
     */
    public void setOnDateClickedListener(OnDateClickedListener onDateClickedListener) {
        this.onDateClickedListener = onDateClickedListener;
    }

    // initialize all item view
    private void initAllDayItem() {

        if (getChildCount() < itemCount) {
            removeAllViews();
            addAllDayItem();
        }

        int indexOfLastDay = firstWeekOfMonth + dayCountOfMonth - 1;
        View itemView;
        for (int index = 0; index < itemCount; index++) {
            itemView = getChildAt(index);
            itemView.setBackgroundColor(Color.TRANSPARENT);
            if (index < firstWeekOfMonth) {
                itemView.setVisibility(INVISIBLE);
            } else if (index > indexOfLastDay) {
                itemView.setVisibility(INVISIBLE);
            } else {
                handleNormalDate(itemView, index);
            }
        }

        updateDayView();
    }


    // add all item view
    private void addAllDayItem() {
        for (int i = 0; i < itemCount; i++) {
            final View dayView = createDayItemView();
            dayView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDateClickedListener != null && view.getVisibility() == VISIBLE) {
                        onClickView(view);
                    }
                }
            });
            addView(dayView);
        }
    }

    private void onClickView(View view) {
        clearAllBackground();

        view.setBackgroundResource(R.drawable.day_view_selected);
        if (CalendarView.startDate != null && CalendarView.endDate != null) {
            CalendarView.startDate = null;
            CalendarView.endDate = null;

            int dayOfMonth = indexOfChild(view) - firstWeekOfMonth + 1;
            CalendarView.startDate = new Date(year, month, dayOfMonth);

        } else if (CalendarView.startDate != null && CalendarView.endDate == null) {
            int dayOfMonth = indexOfChild(view) - firstWeekOfMonth + 1;
            Date temp = new Date(year, month, dayOfMonth);
            if (DateUtil.isAfter(temp, CalendarView.startDate)) {
                CalendarView.endDate = temp;
            } else {
                CalendarView.endDate = CalendarView.startDate;
                CalendarView.startDate = temp;
            }
        }

        onDateClickedListener.onDateRangeSelected(CalendarView.startDate, CalendarView.endDate);

        updateDayView();
    }

    private void clearAllBackground() {
        View itemView;
        for (int index = 0; index < itemCount; index++) {
            itemView = getChildAt(index);
            itemView.setBackgroundColor(Color.TRANSPARENT);
            TextView textView = itemView.findViewById(R.id.tv_day_of_month);
            textView.setTextColor(Color.parseColor("#303030"));
        }
    }

    private void updateDayView() {
        if (CalendarView.startDate != null && CalendarView.endDate == null) {
            View itemView;
            for (int index = 0; index < itemCount; index++) {
                itemView = getChildAt(index);

                int dayOfMonth = index - firstWeekOfMonth + 1;
                Date current = new Date(year, month, dayOfMonth);
                TextView textView = itemView.findViewById(R.id.tv_day_of_month);
                if (DateUtil.isEquals(current, CalendarView.startDate)) {
                    textView.setTextColor(Color.WHITE);
                    itemView.setBackground(getResources().getDrawable(R.drawable.day_view_selected));
                }
            }
        } else if (CalendarView.startDate != null && CalendarView.endDate != null) {

            View itemView;
            if (DateUtil.isEquals(CalendarView.startDate, CalendarView.endDate)) {
                for (int index = 0; index < itemCount; index++) {
                    itemView = getChildAt(index);

                    int dayOfMonth = index - firstWeekOfMonth + 1;
                    Date current = new Date(year, month, dayOfMonth);
                    TextView textView = itemView.findViewById(R.id.tv_day_of_month);
                    if (DateUtil.isEquals(current, CalendarView.startDate)) {
                        textView.setTextColor(Color.WHITE);
                        itemView.setBackground(getResources().getDrawable(R.drawable.day_view_selected));
                    }
                }
            } else {
                for (int index = 0; index < itemCount; index++) {
                    itemView = getChildAt(index);
                    TextView textView = itemView.findViewById(R.id.tv_day_of_month);

                    int dayOfMonth = index - firstWeekOfMonth + 1;
                    Date current = new Date(year, month, dayOfMonth);
                    if (DateUtil.isEquals(current, CalendarView.startDate)) {
                        textView.setTextColor(Color.WHITE);

                        Drawable bgDrawable = getResources().getDrawable(R.drawable.day_selected_left);
                        Drawable bgDrawable2 = getResources().getDrawable(R.drawable.day_view_selected);
                        LayerDrawable drawable = new LayerDrawable(new Drawable[]{bgDrawable, bgDrawable2});
                        itemView.setBackground(drawable);
                    } else if (DateUtil.isEquals(current, CalendarView.endDate)) {
                        textView.setTextColor(Color.WHITE);

                        Drawable bgDrawable = getResources().getDrawable(R.drawable.day_selected_right);
                        Drawable bgDrawable2 = getResources().getDrawable(R.drawable.day_view_selected);
                        LayerDrawable drawable = new LayerDrawable(new Drawable[]{bgDrawable, bgDrawable2});
                        itemView.setBackground(drawable);
                    } else if (DateUtil.belongCalendar(current, CalendarView.startDate, CalendarView.endDate)) {
                        textView.setTextColor(Color.WHITE);
                        itemView.setBackgroundResource(R.drawable.day_view_bg);
                    }
                }
            }
        }
    }

    // create view for item
    @NonNull
    private View createDayItemView() {
        View dayView = LayoutInflater.from(getContext()).inflate(R.layout.def_date_layout, this, false);
        LayoutParams params = new LayoutParams();
        // average distribution in horizontal direction
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        dayView.setLayoutParams(params);
        return dayView;
    }

    private void handleNormalDate(View itemView, int index) {
        itemView.setVisibility(VISIBLE);
        int dayOfMonth = index - firstWeekOfMonth + 1;
        try {
            TextView textView = itemView.findViewById(R.id.tv_day_of_month);
            textView.setText(String.valueOf(dayOfMonth));
            textView.setTextSize(14);
            textView.setTextColor(Color.parseColor("#303030"));
        } catch (NullPointerException e) {
            throw new RuntimeException("Missing ID is tv_day_of_month's TextView!");
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        setMeasuredDimension(getMeasuredWidth(), getChildAt(0).getMeasuredWidth() * ROW_MAX_COUNT);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int totalWidth = right - left - getPaddingLeft() - getPaddingRight();
        int childSize = totalWidth / COLUMN_COUNT;

        for (int i = 0, N = getChildCount(); i < N; i++) {
            View c = getChildAt(i);
            int childLeft = i % COLUMN_COUNT * childSize + getPaddingLeft();
            int childTop = i / COLUMN_COUNT * childSize + getPaddingTop();
            c.layout(childLeft, childTop, childLeft + childSize, childTop + childSize);
        }
    }

}
