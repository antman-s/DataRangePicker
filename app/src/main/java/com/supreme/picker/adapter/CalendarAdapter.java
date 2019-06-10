

package com.supreme.picker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.supreme.picker.MonthView;
import com.supreme.picker.entity.Date;
import com.supreme.picker.listener.OnDateClickedListener;
import com.supreme.picker.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StoneHui on 17/2/14.
 * <p>
 * Data adapter for calendar component. The data type is {@link Date}
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

    private final Context context;

    private OnDateClickedListener onDateClickedListener;

    private List<Date> dateList;

    /**
     * initialize adapter by {@link Date}
     *
     * @param context Activity context
     * @param date    first display date
     */
    public CalendarAdapter(Context context, Date date) {
        this.context = context;

        dateList = new ArrayList<>();
        dateList.add(DateUtil.lastMonth(date));
        dateList.add(date);
        dateList.add(DateUtil.nextMonth(date));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MonthView monthView = new MonthView(context);
        monthView.setOnDateClickedListener(onDateClickedListener);
        return new MyViewHolder(monthView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Date date = dateList.get(position);
        ((MonthView) holder.itemView).setMonth(date.getYear(), date.getMonth());
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public List<Date> getDateList() {
        return dateList;
    }

    /**
     * Set listener for date be clicked.
     *
     * @param onDateClickedListener the listener will be call when date be clicked.
     */
    public void setOnDateClickedListener(OnDateClickedListener onDateClickedListener) {
        this.onDateClickedListener = onDateClickedListener;
    }

    /**
     * Add last month of {@link #dateList}'s  first month.
     */
    public void addNewLastMonth() {
        dateList.add(0, DateUtil.lastMonth(dateList.get(0)));
        notifyItemInserted(0);
    }

    /**
     * Add next month of {@link #dateList}'s  last month.
     */
    public void addNewNextMonth() {
        dateList.add(DateUtil.nextMonth(dateList.get(dateList.size() - 1)));
        notifyItemInserted(dateList.size() - 1);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}