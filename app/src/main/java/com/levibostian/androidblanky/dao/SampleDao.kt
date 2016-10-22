package com.levibostian.androidblanky.dao

import com.levibostian.androidblanky.db.models.SampleModel
import io.realm.Realm

import javax.inject.Inject
import java.util.ArrayList

class SampleDao(realm: Realm) : BaseDao(realm) {

    class SampleDao @Inject constructor() {
    }

    fun findFirstById(id: Long): SampleModel? {
        return mRealm.where(SampleModel::class.java).equalTo("id", id).findFirst()
    }

    fun saveSamples(samples: ArrayList<SampleModel>, listener: Listener) {
        realmTransactionAsync(object : BaseDao.TransactionExecListener {
            override fun execute(realm: Realm) {
                realm.copyToRealmOrUpdate(samples)
            }
        }, listener)
    }

    fun getNewestSample() : Long {
        val max: Number? = mRealm.where(SampleModel::class.java).max("id")

        return if (max == null) 0 else max.toLong()
    }

}