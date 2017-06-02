package com.levibostian.androidblanky

object AppConstants {

    var API_ENDPOINT = "https://api.github.com"

    var baseWebsiteHost = "https://curiosityio.com"

    val privacyPolicyUrl: String
        get() = baseWebsiteHost + "/privacy_policy.html"

    val termsAndConditionsUrl: String
        get() = baseWebsiteHost + "/terms_and_conditions.html"

    val aboutUrl: String
        get() = baseWebsiteHost + "/about_us.html"

}
