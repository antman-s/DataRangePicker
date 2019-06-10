package com.supreme.picker.annotation;

import android.support.annotation.IntRange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by StoneHui on 17/2/16.
 * <p>
 * Limited week to prevent the occurrence of larger or smaller numbers.
 */
@Retention(RetentionPolicy.SOURCE)
@IntRange(from = Week.SUNDAY, to = Week.SATURDAY)
public @interface Week {

    /**
     * 星期日 | Sunday
     */
    int SUNDAY = 0;

    /**
     * 星期一 | Monday
     */
    int MONDAY = 1;

    /**
     * 星期二 | Tuesday
     */
    int TUESDAY = 2;

    /**
     * 星期三 | Wednesday
     */
    int WEDNESDAY = 3;

    /**
     * 星期四 | Thursday
     */
    int THURSDAY = 4;

    /**
     * 星期五 | Friday
     */
    int FRIDAY = 5;

    /**
     * 星期六 | Saturday
     */
    int SATURDAY = 6;

}
