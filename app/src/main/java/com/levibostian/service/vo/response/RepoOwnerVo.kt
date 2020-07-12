package com.levibostian.service.vo.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepoOwnerVo(val login: String) {

    companion object

}