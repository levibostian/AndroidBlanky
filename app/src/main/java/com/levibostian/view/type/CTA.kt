package com.levibostian.view.type

import android.net.Uri
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CTA(val title: String,
               val links: List<CTALink>?,
               val notice: String?) {
    companion object
}

@JsonClass(generateAdapter = true)
data class CTALink(val title: String,
                   val url: Uri?,
                   val action: String?) {

    companion object {}

    constructor(title: String, url: Uri): this(title, url, null)
    constructor(title: String, action: String): this(title, null, action)

    val type: LinkType
        get() {
            if (url != null) {
                return LinkType.Url(url)
            }
            if (action != null) {
                return LinkType.Action(action)
            }

            throw IllegalArgumentException("Not valid option")
        }

    sealed class LinkType {
        class Url(val url: Uri): LinkType()
        class Action(val action: String): LinkType()
    }

}