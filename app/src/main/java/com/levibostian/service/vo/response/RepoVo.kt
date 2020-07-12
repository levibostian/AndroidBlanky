package com.levibostian.service.vo.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepoVo(
        val id: Long,
        val name: String,
        val owner: RepoOwnerVo) {

    companion object

}