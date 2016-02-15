package com.levibostian.androidblanky.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

public class ViewUtil {

    public static void closeKeyboard(Context context, View field) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(field.getWindowToken(), 0);
        } catch (Exception ex) {
            LogUtil.d("Hide keyboard failed.");
        }
    }

    public static void showKeyboard(Context context, View field) {
        try {
            field.requestFocus();

            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception ex) {
            LogUtil.d("Show keyboard failed.");
        }
    }

    // Create a bitmap from a view. Usually used for animations to make them go smoother by using a bitmap instead of animating the view itself.
    public static Bitmap viewToImage(Context context, WebView viewToBeConverted) {
        int extraSpace = 2000; //because getContentHeight doesn't always return the full screen height.
        int height = viewToBeConverted.getContentHeight() + extraSpace;

        Bitmap viewBitmap = Bitmap.createBitmap(viewToBeConverted.getWidth(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(viewBitmap);
        viewToBeConverted.draw(canvas);

        //If the view is scrolled, cut off the top part that is off the screen.
        try {
            int scrollY = viewToBeConverted.getScrollY();

            if (scrollY > 0) {
                viewBitmap = Bitmap.createBitmap(viewBitmap, 0, scrollY, viewToBeConverted.getWidth(), height - scrollY);
            }
        } catch (Exception ex) {
            LogUtil.d("Could not remove top part of the webview image. ex: " + ex);
        }

        return viewBitmap;
    }

}
