package com.app.service.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.service.vo.response.RepoVo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "repo")
data class RepoModel(
    @PrimaryKey val id: Long,
    val name: String,
    @Embedded(prefix = "repo_owner_") val owner: RepoOwnerModel
) {

    companion object {

        fun newFrom(vo: RepoVo): RepoModel {
            return RepoModel(
                id = vo.id,
                name = vo.name,
                owner = RepoOwnerModel.newFrom(vo.owner)
            )
        }

        // called with existing instance to update from VO
        fun updateFrom(existing: RepoModel, vo: RepoVo): RepoModel {
            /**
             * Properties that *can be updated locally*, make sure to put those into the new instance and copy use the new value.
             *
             * Here is an example:
             *
             return Workout(
             id = vo.id,
             name = vo.name

             // Properties that can be updated locally are below
             favorite = existing.favorite)
             */

            return RepoModel(
                id = vo.id,
                name = vo.name,
                owner = RepoOwnerModel.updateFrom(existing.owner, vo.owner)
            )
        }
    }
}
