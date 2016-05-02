package com.levibostian.androidblanky.dao;

import io.realm.Realm;

public abstract class BaseDao {

    protected Realm mRealm;

    protected BaseDao(Realm realm) {
        mRealm = realm;
    }

    public interface Listener {
        void success();
        void error(String message);
    }

    public interface TransactionExecListener {
        void execute(Realm realm);
    }

    protected void realmTransactionAsync(final TransactionExecListener transactionExecListener, final Listener listener) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                transactionExecListener.execute(realm);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                listener.success();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                listener.error(error.getMessage());
            }
        });
    }

}
