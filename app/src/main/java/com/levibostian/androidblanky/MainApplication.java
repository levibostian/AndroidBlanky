package com.levibostian.androidblanky;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.levibostian.androidblanky.module.ApiModule;
import com.levibostian.androidblanky.module.FragmentModule;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;
import dagger.ObjectGraph;

import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application {

    private static ObjectGraph sObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        LeakCanary.install(this);
        FlowManager.init(this);

        sObjectGraph = ObjectGraph.create(getModules().toArray());
    }

    protected List<Object> getModules() {
        return Arrays.asList(new ApiModule(this),
                             new FragmentModule());
    }

    public static void inject(Object object) {
        sObjectGraph.inject(object);
    }

}
