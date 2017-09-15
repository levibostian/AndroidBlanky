package com.levibostian.androidblanky.service.dao

import com.levibostian.androidblanky.service.model.RepoModel
import io.reactivex.*
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmResults
import java.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.realm.RealmChangeListener
import io.realm.RealmModel

class RepoDao(private val uiRealm: Realm): Dao {

    fun getReposUi(): Flowable<RealmResults<RepoModel>> {
        return getFlowable(uiRealm, { uiRealm ->
            uiRealm.where(RepoModel::class.java).findAllAsync()
        })
    }

}