package com.levibostian.androidblanky.util;

import android.telephony.PhoneNumberUtils;
import android.util.Patterns;

public class ValidCheckUtil {

    public final static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public final static boolean isValidPhoneNumber(String number) {
        if (number == null) {
            return false;
        } else {
            return PhoneNumberUtils.isGlobalPhoneNumber(number);
        }
    }

    public final static boolean isValidURL(String url) {
        if (url == null) {
            return false;
        } else {
            return Patterns.WEB_URL.matcher(url).matches();
        }
    }
}
