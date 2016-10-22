package com.levibostian.androidblanky.db.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

class SampleModel : RealmObject() {

    @PrimaryKey var id: Long = 0
    @Required var created: Date = Date()
    @Required var title: String = ""
    var done: Boolean = false
    var other_model: OtherSampleModel? = null

}