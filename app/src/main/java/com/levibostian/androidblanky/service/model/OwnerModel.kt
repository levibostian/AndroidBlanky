package com.levibostian.androidblanky.service.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

@JsonClass(generateAdapter = true)
open class OwnerModel(var login: String = "",
                      var avatar_url: String = "") : RealmObject()