package com.levibostian.androidblanky.service.db.migrations

import com.levibostian.androidblanky.service.model.OwnerModel
import io.realm.RealmSchema

class Migration2 : RealmSchemaMigration {

    override fun runMigration(schema: RealmSchema) {
        schema.get(OwnerModel::class.java.simpleName)!!
                .removePrimaryKey()
    }

}