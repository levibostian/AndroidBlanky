package com.levibostian.androidblanky.util;

import android.content.Context;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

public class NumberUtil {

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getFormattedCount(int count) {
        return getFormattedCount(Long.valueOf(count));
    }

    public static String getFormattedCount(String count) {
        return getFormattedCount(Long.parseLong(count));
    }

    // Example: Input: 1200000, output: 1.2m
    public static String getFormattedCount(Long count) {
        final String unit;
        final Double dbl;
        final DecimalFormat format = new DecimalFormat("#.#");
        if (count < 1000) {
            return format.format(count);
        } else if (count < 1000000) {
            unit = "k";
            dbl = count / 1000.0;
        } else if (count < 1000000000) {
            unit = "m";
            dbl = count / 1000000.0;
        } else {
            unit = "b";
            dbl = count / 1000000000.0;
        }
        return format.format(dbl) + unit;
    }
}