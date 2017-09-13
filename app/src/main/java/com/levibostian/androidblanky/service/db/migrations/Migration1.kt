package com.levibostian.androidblanky.service.db.migrations

import com.levibostian.androidblanky.service.model.OwnerModel
import io.realm.RealmSchema

class Migration1: RealmSchemaMigration {

    override fun runMigration(schema: RealmSchema) {
        schema.get(OwnerModel::class.java.simpleName)!!
                .addField("avatar_url", String::class.java)
                .setRequired("avatar_url", true)
                .transform { it.set("avatar_url", "") }
    }

}