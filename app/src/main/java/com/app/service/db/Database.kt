package com.app.service.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.service.dao.ReposDao
import com.app.service.model.RepoModel

@Database(entities = [RepoModel::class], version = 1, exportSchema = true)
abstract class Database : RoomDatabase() {
    abstract fun reposDao(): ReposDao
}
