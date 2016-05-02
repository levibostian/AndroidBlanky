package com.levibostian.androidblanky.dao;

import com.levibostian.androidblanky.db.models.SampleModel;
import io.realm.Realm;

import javax.inject.Inject;
import java.util.ArrayList;

public class SampleDao extends BaseDao {

    @Inject
    public SampleDao(Realm realm) {
        super(realm);
    }

    public SampleModel findFirstById(long id) {
        return mRealm.where(SampleModel.class).equalTo("id", id).findFirst();
    }

    public void saveSamples(final ArrayList<SampleModel> samples, final Listener listener) {
        realmTransactionAsync(new TransactionExecListener() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(samples);
            }
        }, listener);
    }

    public long getNewestSample() {
        Number max = mRealm.where(SampleModel.class).max("id");

        if (max == null) {
            return 0;
        } else {
            return max.longValue();
        }
    }

}
