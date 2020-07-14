package com.levibostian.service.api

/**
 * Handy object that is used in the API class that is not dependent on the library used to perform HTTP requests. We pull out values of the library specific http result and put it into a format that is common among all libraries. If the fields below are not enough, add more. Prevent the urge to add a library specific data type to this object (one reason the body is a String type).
 */
data class ProcessedResponse(
    val url: String,
    val method: String,
    val statusCode: Int,
    val body: String?
)
