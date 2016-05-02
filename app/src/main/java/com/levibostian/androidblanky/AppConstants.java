package com.levibostian.androidblanky;

public class AppConstants {

    public static String baseWebsiteHost = "http://curiosityio.com";

    public static String getPrivacyPolicyUrl() {
        return baseWebsiteHost.concat("/privacy_policy.html");
    }

    public static String getTermsAndConditionsUrl() {
        return baseWebsiteHost.concat("/terms_and_conditions.html");
    }

    public static String getAboutUrl() {
        return baseWebsiteHost.concat("/about_us.html");
    }

}
