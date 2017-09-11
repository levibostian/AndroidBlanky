package com.levibostian.androidblanky.service.dao

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

@JvmName("RealmUtils")

fun Realm.repoDao(): RepoDao = RepoDao(this)

