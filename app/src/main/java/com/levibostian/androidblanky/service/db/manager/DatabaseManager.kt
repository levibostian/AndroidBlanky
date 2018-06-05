package com.levibostian.androidblanky.service.db.manager

import android.arch.persistence.room.Room
import android.content.Context
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.manager.UserManager

class DatabaseManager(private val userManager: UserManager) {

    companion object {
        private const val DATABASE_NAME_PREFIX = "database"
    }

    fun dbInstance(context: Context): Database {
        if (!userManager.isUserLoggedIn()) throw RuntimeException("User is not logged in. Cannot get database instance.")

        return Room.databaseBuilder(context, Database::class.java, "${DATABASE_NAME_PREFIX}_${userManager.id!!}")
                //.addMigrations(Migration1()) // When you need to run a migration, un-comment.
                .build()
    }

}