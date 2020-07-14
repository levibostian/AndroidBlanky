package com.levibostian.service.vo.response.error

/**
 * When hit API's rate limit for the endpoint.
 *
 * Example response:
```
{
"error": {
"text": "Too many requests in this time frame.",
"next_valid_request_date": "2019-03-18T13:26:25.249Z"
}
}
```
 */
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class RateLimitingResponseError(val error: Error) {

    @JsonClass(generateAdapter = true)
    data class Error(val text: String, val next_valid_request_date: Date)
}
