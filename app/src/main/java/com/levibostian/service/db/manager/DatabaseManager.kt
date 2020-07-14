package com.levibostian.service.db.manager

import android.content.Context
import androidx.room.Room
import com.levibostian.service.db.Database

class DatabaseManager {

    companion object {
        private const val DATABASE_NAME = "database"
    }

    fun dbInstance(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, DATABASE_NAME)
            // .addMigrations(Migration1()) // When you need to run a migration, un-comment.
            .build()
    }
}
