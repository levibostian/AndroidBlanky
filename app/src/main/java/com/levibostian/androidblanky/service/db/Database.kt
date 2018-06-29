package com.levibostian.androidblanky.service.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.model.RepoModel

@Database(entities = [RepoModel::class], version = 1, exportSchema = true)
abstract class Database: RoomDatabase() {
    abstract fun reposDao(): ReposDao
}