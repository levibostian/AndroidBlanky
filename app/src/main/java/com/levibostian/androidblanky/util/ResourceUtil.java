package com.levibostian.androidblanky.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

public class ResourceUtil {

    public static ArrayList<String> getArrayOfStringRes(Context context, int stringArrayRes) {
        String[] myResArray = context.getResources().getStringArray(stringArrayRes);

        return new ArrayList<>(Arrays.asList(myResArray));
    }

}