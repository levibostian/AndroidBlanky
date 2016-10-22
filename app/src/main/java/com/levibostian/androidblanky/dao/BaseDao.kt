package com.levibostian.androidblanky.dao

import io.realm.Realm

abstract class BaseDao(protected val mRealm: Realm) {

    interface Listener {
        fun success()
        fun error(message: String)
    }

    interface TransactionExecListener {
        fun execute(realm: Realm)
    }

    protected fun realmTransactionAsync(transactionExecListener: TransactionExecListener, listener: Listener) {
        mRealm.executeTransactionAsync({
            realm -> transactionExecListener.execute(realm)
        }, {
            listener.success()
        }) {
            error -> listener.error(error.message!!)
        }
    }

}