

package com.supreme.picker.annotation;

import android.support.annotation.IntRange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@IntRange(from = 1, to = 31)
public @interface DayOfMonth {
}
