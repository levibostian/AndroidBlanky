package com.levibostian.androidblanky.service.vo.response

data class LoggedInUserVo(val id: Int,
                          val email: String,
                          val username: String?,
                          val access_token: String)