package com.levibostian.androidblanky.service.db.manager

import com.levibostian.androidblanky.service.db.migrations.Migration1
import com.levibostian.androidblanky.service.db.migrations.RealmSchemaMigration
import com.levibostian.androidblanky.service.manager.UserManager
import io.realm.Realm
import io.realm.RealmConfiguration

open class RealmInstanceManager(private val userManager: UserManager) {

    companion object {
        fun getInMemory(): Realm {
            val realmConfiguration = RealmConfiguration.Builder()
                    .name("in_memory.realm")
                    .inMemory()
                    .build()
            return Realm.getInstance(realmConfiguration)
        }

        val migrations: List<RealmSchemaMigration> = listOf(
                Migration1()
        )

        var schemaVersion: Long = 0L
            get() = migrations.size.toLong()
    }

    open fun getDefault(): Realm {
        val config = RealmConfiguration.Builder()
                .name("${userManager.id ?: "default"}.realm")
                .schemaVersion(schemaVersion)
                .migration { dynamicRealm, oldVersion, newVersion ->
                    val schema = dynamicRealm.schema

                    for (i in oldVersion until newVersion) {
                        migrations[i.toInt()].runMigration(schema)
                    }
                }
                .build()
        return Realm.getInstance(config)
    }

    open fun getInMemory(): Realm {
        return Companion.getInMemory()
    }

}