package com.levibostian.androidblanky;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.levibostian.androidblanky.activity.MainActivity;
import com.levibostian.androidblanky.fragment.MainFragment;
import com.levibostian.androidblanky.module.ApiModule;
import com.levibostian.androidblanky.module.DaoModule;
import com.levibostian.androidblanky.module.ManagerModule;
import com.levibostian.androidblanky.module.UtilModule;
import dagger.Component;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application {

    @Singleton
    @Component(modules = {ApiModule.class, DaoModule.class, ManagerModule.class, UtilModule.class})
    public interface ApplicationComponent {
        void inject(MainActivity mainActivity);
        void inject(MainFragment mainFragment);
    }

    //private static RefWatcher mRefWatcher;
    private static ApplicationComponent sApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        final Fabric fabric = new Fabric.Builder(this)
                                      .kits(new Crashlytics.Builder().core(core).build())
                                      .debuggable(true)
                                      .build();
        Fabric.with(fabric);
        //mRefWatcher = LeakCanary.install(this);

        sApplicationComponent = DaggerMainApplication_ApplicationComponent.builder()
                                                                          .apiModule(new ApiModule(this))
                                                                          .daoModule(new DaoModule())
                                                                          .managerModule(new ManagerModule(this))
                                                                          .utilModule(new UtilModule(this))
                                                                          .build();

        configureRealm();
    }

    private void configureRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                                            .schemaVersion(0)
                                            .build();
        Realm.setDefaultConfiguration(config);
    }

    //    public static RefWatcher getRefWatcher() {
    //        return mRefWatcher;
    //    }

    public static ApplicationComponent component() {
        return sApplicationComponent;
    }
}
