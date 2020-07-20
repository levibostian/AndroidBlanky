package com.app.service.model

import com.app.service.vo.response.RepoOwnerVo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RepoOwnerModel(val name: String = "") {

    companion object {

        fun newFrom(vo: RepoOwnerVo): RepoOwnerModel {
            return RepoOwnerModel(
                name = vo.login
            )
        }

        // called with existing instance to update from VO
        fun updateFrom(existing: RepoOwnerModel, vo: RepoOwnerVo): RepoOwnerModel {
            return RepoOwnerModel(
                name = vo.login
            )
        }
    }
}
