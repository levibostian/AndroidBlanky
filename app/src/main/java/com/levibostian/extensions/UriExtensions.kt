package com.levibostian.extensions

import android.net.Uri

class UriFakes {
    val randomImage: Uri = Uri.parse("https://example.com/image/${String.random}.jpg")
    val randomWebpage: Uri = Uri.parse("https://example.com/${String.random}.html")
}

val Uri.fake: UriFakes
    get() = UriFakes()
