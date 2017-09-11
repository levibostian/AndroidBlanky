package com.levibostian.androidblanky.service

import io.realm.Realm
import io.realm.RealmConfiguration

open class RealmInstanceWrapper {

    fun getDefault(): Realm {
        return Realm.getDefaultInstance()
    }

    fun getInMemory(): Realm {
        val realmConfiguration = RealmConfiguration.Builder()
                .name("in_memory.realm")
                .inMemory()
                .build()
        return Realm.getInstance(realmConfiguration)
    }

}