package com.levibostian.androidblanky.service.db.manager

import android.arch.persistence.room.Room
import android.content.Context
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.migrations.Migration1

class DatabaseManager {

    companion object {
        private const val DATABASE_NAME = "database"

        fun dbInstance(context: Context): Database {
            return Room.databaseBuilder(context, Database::class.java, DATABASE_NAME)
                    //.addMigrations(Migration1()) // When you need to run a migration, un-comment.
                    .build()
        }
    }

}