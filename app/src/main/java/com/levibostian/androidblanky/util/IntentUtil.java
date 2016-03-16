package com.levibostian.androidblanky.util;

import android.content.Intent;
import android.net.Uri;

public class IntentUtil {

    public static Intent getOpenWebpageUrlIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

}
