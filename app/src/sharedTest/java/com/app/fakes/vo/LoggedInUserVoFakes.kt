package com.app.fakes.vo

import com.app.extensions.random
import com.app.service.vo.response.LoggedInUserVo

object LoggedInUserVoFakes {
    val randomAccessToken: LoggedInUserVo
        get() = LoggedInUserVo(Int.random, String.random, String.random, String.random)

    val randomPasswordlessToken: LoggedInUserVo
        get() = LoggedInUserVo(Int.random, String.random, null, String.random)
}

val LoggedInUserVo.Companion.randomAccessToken: LoggedInUserVo
    get() = LoggedInUserVoFakes.randomAccessToken

val LoggedInUserVo.Companion.randomPasswordlessToken: LoggedInUserVo
    get() = LoggedInUserVoFakes.randomPasswordlessToken
