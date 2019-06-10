package com.supreme.picker.annotation;

import android.support.annotation.IntRange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by StoneHui on 17/2/16.
 * <p>
 * Limited month to prevent the occurrence of larger or smaller numbers.
 */
@Retention(RetentionPolicy.SOURCE)
@IntRange(from = Month.JANUARY, to = Month.DECEMBER)
public @interface Month {

    /**
     * 一月 | January
     */
    int JANUARY = 0;

    /**
     * 二月 | February
     */
    int FEBRUARY = 1;

    /**
     * 三月 | March
     */
    int MARCH = 2;

    /**
     * 四月 | April
     */
    int APRIL = 3;

    /**
     * 五月 | May
     */
    int MAY = 4;

    /**
     * 六月 | June
     */
    int JUNE = 5;

    /**
     * 七月 | July
     */
    int JULY = 6;

    /**
     * 八月 | August
     */
    int AUGUST = 7;

    /**
     * 九月 | September
     */
    int SEPTEMBER = 8;

    /**
     * 十月 | October
     */
    int OCTOBER = 9;

    /**
     * 十一月 | November
     */
    int NOVEMBER = 10;

    /**
     * 十二月 | December
     */
    int DECEMBER = 11;
}