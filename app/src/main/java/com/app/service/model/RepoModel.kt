package com.app.service.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repo")
data class RepoModel(
    @PrimaryKey val id: Long,
    val name: String
)
