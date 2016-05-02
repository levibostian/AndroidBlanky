package com.levibostian.androidblanky.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import com.levibostian.androidblanky.R;

import java.util.Calendar;

public class IntentUtil {

    public static void openWebpageUrlIntent(Activity context, final String url) {
        openWebpageUrlIntent(null, context, url);
    }

    public static void openWebpageUrlIntent(CustomTabsSession session, Activity context, final String url) {
        CustomTabsIntent.Builder builder;

        if (session != null) {
            builder = new CustomTabsIntent.Builder(session);
        } else {
            builder = new CustomTabsIntent.Builder();
        }

        builder.setToolbarColor(ContextCompat.getColor(context, R.color.primary));

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    public static Intent startActivityIntent(Context context, Class activityClass) {
        return new Intent(context, activityClass);
    }

    public static Intent startServiceIntent(Context context, Class serviceClass) {
        return new Intent(context, serviceClass);
    }

    public static Intent getShareIntent(String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        return sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
    }

    public static Intent getAddEventCalendarIntent(String title, String description) {
        Calendar beginTime = Calendar.getInstance();

        beginTime.set(Calendar.HOUR_OF_DAY, 19);
        beginTime.set(Calendar.MINUTE, 0);
        beginTime.roll(Calendar.DAY_OF_MONTH, 1);

        Calendar endTime = (Calendar) beginTime.clone();

        endTime.roll(Calendar.HOUR_OF_DAY, 1);

        return new Intent(Intent.ACTION_INSERT)
                       .setData(CalendarContract.Events.CONTENT_URI)
                       .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                       .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                       .putExtra(CalendarContract.Events.TITLE, title)
                       .putExtra(CalendarContract.Events.DESCRIPTION, description)
                       //.putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                       .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        //.putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
    }

}
