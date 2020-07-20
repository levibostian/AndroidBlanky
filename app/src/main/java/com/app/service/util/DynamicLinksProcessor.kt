package com.app.service.util

import android.net.Uri

sealed class DynamicLinkAction {
    data class PasswordlessTokenExchange(val token: String) : DynamicLinkAction()
}

class DynamicLinksProcessor {
    companion object {

        fun getActionFromDynamicLink(link: Uri): DynamicLinkAction? {
            link.getQueryParameter("passwordless_token")?.let { return DynamicLinkAction.PasswordlessTokenExchange(it) }

            return null
        }
    }
}
