package com.levibostian.androidblanky.service.model

object AppConstants {

    var API_ENDPOINT = "https://api.github.com"

    var baseWebsiteHost = "https://levibostian.com"
    val privacyPolicyUrl = "$baseWebsiteHost/privacy_policy.html"
    val termsAndConditionsUrl = "$baseWebsiteHost/terms_and_conditions.html"
    val aboutUrl = "$baseWebsiteHost/about_us.html"

    val http500StatusErrorMessage = "Sorry, there seems to be an error with the system. Try again later."
    val httpUnsuccessfulStatusCodeExceptionMessage = "Sorry, there seems to be an issue. We have been notified. Try again later."
    val httpNoInternetConnectionExceptionMessage = "No Internet connection. Connect, then try again."
    val httpBadNetworkConnectionExceptionMessage = "You have a bad Internet connection. Connect, then try again."

}
