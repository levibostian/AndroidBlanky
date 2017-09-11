package com.levibostian.androidblanky.service.dao

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposables
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

// This is temporary. I received this code snippet from https://stackoverflow.com/a/44341905/1486374 as well as https://github.com/realm/realm-java/issues/3497#issuecomment-275383550.
//
// Realm v4 will have rxjava2 support which allows you to get a Flowable from a realm query.
fun <T: RealmModel> Dao.getFlowable(realm: Realm, getResults: (Realm) -> RealmResults<T>): Flowable<RealmResults<T>> {
    return io.reactivex.Flowable.create<RealmResults<T>>({ emitter ->
        val results: RealmResults<T> = getResults(realm)

        val listener: RealmChangeListener<RealmResults<T>> = RealmChangeListener { _ ->
            if (!emitter.isCancelled) {
                emitter.onNext(results)
            }
        }
        emitter.setDisposable(Disposables.fromRunnable {
            results.removeChangeListener(listener)
            realm.close()
        })
        results.addChangeListener(listener)
        emitter.onNext(results)
    }, BackpressureStrategy.LATEST)
}