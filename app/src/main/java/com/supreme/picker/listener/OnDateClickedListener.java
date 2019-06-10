
package com.supreme.picker.listener;

import com.supreme.picker.entity.Date;



/**
 * Created by StoneHui on 17/2/16.
 * <p>
 * Listener for day of month be clicked.
 */
public interface OnDateClickedListener {

    void onDateRangeSelected(Date start, Date end);

}
