package com.levibostian.service.util

import com.levibostian.service.type.FcmTopicKey


enum class NotificationAction {
    UPDATE_PROGRAM;
}

data class DataNotification(val topicName: String) {

    val action: NotificationAction?
        get() {
            return when (this.topicName) {
                FcmTopicKey.ProductUpdated.fcmName -> NotificationAction.UPDATE_PROGRAM
                else -> null
            }
        }

}

object NotificationUtil {
    fun parseDataNotification(data: Map<String, String>): DataNotification? {
        val topicName = data["topicName"] ?: return null

        return DataNotification(topicName)
    }
}