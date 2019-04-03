package com.levibostian.androidblanky.service.vo.response

import com.levibostian.androidblanky.service.vo.MessageResponse

class PasswordExchangeForAccessTokenResponse(override val message: String,
                                             val user: LoggedInUserVo): MessageResponse