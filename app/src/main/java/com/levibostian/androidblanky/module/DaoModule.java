package com.levibostian.androidblanky.module;

import com.levibostian.androidblanky.dao.SampleDao;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class DaoModule {

    @Provides
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    public SampleDao provideSampleDao(Realm realm) {
        return new SampleDao(realm);
    }

}
