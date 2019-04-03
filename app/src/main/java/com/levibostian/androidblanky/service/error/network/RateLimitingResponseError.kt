package com.levibostian.androidblanky.service.error.network

/**
 * When hit API's rate limit for the endpoint.
 *
 * Example response:
 ```
{
"error": {
"text": "Too many requests in this time frame.",
"nextValidRequestDate": "2019-03-18T13:26:25.249Z"
}
}
 ```
 */
class RateLimitingResponseError(message: String): Throwable(message)