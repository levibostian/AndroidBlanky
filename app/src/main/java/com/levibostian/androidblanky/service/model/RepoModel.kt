package com.levibostian.androidblanky.service.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

@JsonClass(generateAdapter = true)
open class RepoModel(@PrimaryKey var full_name: String? = null,
                     var description: String? = null,
                     var owner: OwnerModel? = null) : RealmObject()