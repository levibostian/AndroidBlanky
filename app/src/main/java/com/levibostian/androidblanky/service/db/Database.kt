package com.levibostian.androidblanky.service.db

import android.app.Application
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.model.RepoModel

@Database(entities = [RepoModel::class], version = 1, exportSchema = true)
abstract class Database: RoomDatabase() {
    abstract fun reposDao(): ReposDao
}