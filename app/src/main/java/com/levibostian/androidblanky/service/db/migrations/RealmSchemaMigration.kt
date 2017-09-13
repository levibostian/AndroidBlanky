package com.levibostian.androidblanky.service.db.migrations

import io.realm.RealmSchema

interface RealmSchemaMigration {

    fun runMigration(schema: RealmSchema)

}