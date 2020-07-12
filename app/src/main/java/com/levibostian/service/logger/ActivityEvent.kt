package com.levibostian.service.logger

import java.util.*

enum class ActivityEvent {
    SearchRepositories,
    Login,
    Logout,

    // Developer related events that are important to make sure features are working.
    RemoteConfigFetchSuccess,
    RemoteConfigFetchFail,
    PushNotificationReceived, // data or UI based. provide param
    PushNotificationTopicSubscribed,
    PerformBackgroundSync,
    OpenedDynamicLink;
}

enum class ActivityEventParamKey {
    Method,
    Id,
    Name,
    PaidUser, // (Bool) If user is a paying customer
    Type
}