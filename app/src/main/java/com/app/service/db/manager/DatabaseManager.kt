package com.app.service.db.manager

import android.content.Context
import androidx.room.Room
import com.app.Env
import com.app.service.db.Database

class DatabaseManager {

    companion object {
        private val DATABASE_NAME = Env.appName.replace(" ", "_")
    }

    fun dbInstance(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, DATABASE_NAME)
            // .addMigrations(Migration1()) // When you need to run a migration, un-comment.
            .build()
    }
}
