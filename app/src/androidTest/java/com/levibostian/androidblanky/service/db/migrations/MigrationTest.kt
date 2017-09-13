package com.levibostian.androidblanky.service.db.migrations

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import org.junit.runner.RunWith
import javax.inject.Inject
import com.levibostian.androidblanky.rule.TestRealmConfigurationFactory
import com.levibostian.androidblanky.service.db.manager.RealmInstanceManager
import com.levibostian.androidblanky.service.model.OwnerModel
import com.levibostian.androidblanky.view.ui.AppApplicationComponent
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.view.ui.TestMainApplication
import io.realm.Realm
import io.realm.RealmMigration
import io.realm.exceptions.RealmMigrationNeededException
import junit.framework.Assert
import org.junit.rules.ExpectedException
import org.mockito.*
import io.realm.RealmConfiguration
import org.junit.*
import java.io.File

@RunWith(AndroidJUnit4::class)
open class MigrationTest {

    @get:Rule open val configFactory = TestRealmConfigurationFactory()
    @get:Rule open val thrown = ExpectedException.none()

    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test(expected = RealmMigrationNeededException::class)
    @Throws(Exception::class)
    fun migrate_migrationNeededIsThrown() {
        val REALM_NAME = "0.realm"
        val realmConfig = RealmConfiguration.Builder()
                .name(REALM_NAME)
                .schemaVersion(0)
                .build()
        configFactory.copyRealmFromAssets(context, REALM_NAME, realmConfig)

        // should fail because the realm files have changed *since* this 0.realm file.
        // When you want to get a realm instance, it will take what realm objects are already in memory (mapped by the "name" property of the RealmConfiguration) and it will compare it to the files in the application. If they are different, a realm migration exception will be thrown. So, you need to make sure to add Realm migrations to your code at all times.
        val realm = Realm.getInstance(realmConfig)
        realm.close()
    }

    @Test fun migrate_migrateFrom0toLatest() {
        val REALM_NAME = "0.realm"
        val realmConfig = RealmConfiguration.Builder()
                .name(REALM_NAME)
                .schemaVersion(RealmInstanceManager.schemaVersion)
                .migration { dynamicRealm, oldVersion, newVersion ->
                    val schema = dynamicRealm.schema

                    for (i in oldVersion until newVersion) {
                        RealmInstanceManager.migrations[i.toInt()].runMigration(schema)
                    }
                }
                .build()
        configFactory.copyRealmFromAssets(context, REALM_NAME, realmConfig)

        val realm = Realm.getInstance(realmConfig)
        realm.close()
    }

    // convenient method to generate 1 realm file in app directory to be able to copy to assets directory for the next migration test when schema version changes.
    @Test fun createFileForCurrentVersionToCopyToAssetsFile() {
        val REALM_NAME = "${RealmInstanceManager.schemaVersion}.realm"
        val realmConfig = RealmConfiguration.Builder()
                .name(REALM_NAME)
                .schemaVersion(RealmInstanceManager.schemaVersion)
                .build()

        Realm.deleteRealm(realmConfig)
        val realm = Realm.getInstance(realmConfig)
        realm.close()
    }

}
