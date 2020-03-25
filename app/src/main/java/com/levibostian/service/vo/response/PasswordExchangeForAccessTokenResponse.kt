package com.levibostian.service.vo.response

import com.levibostian.service.vo.MessageResponse

class PasswordExchangeForAccessTokenResponse(override val message: String,
                                             val user: LoggedInUserVo): MessageResponse