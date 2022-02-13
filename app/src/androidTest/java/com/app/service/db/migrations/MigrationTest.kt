package com.app.service.db.migrations

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.BaseInstrumentationTest
import com.app.di.DatabaseModule
import com.app.di.NetworkModule
import com.app.service.db.Database
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DatabaseModule::class, NetworkModule::class)
class MigrationTest : BaseInstrumentationTest() {

    companion object {
        private const val TEST_DB = "migration-test"
    }

    private val helper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), Database::class.java.canonicalName, FrameworkSQLiteOpenHelperFactory())

    override fun provideTestClass(): Any = this
    @get:Rule val rules = instrumentationTestRules.around(helper)

    @Test
    fun migrate1to2() {
        assertThat("delete me when want to write actual test").isEqualTo("delete me when want to write actual test")

//        var db = helper.createDatabase(TEST_DB, 1)
//
//        // db has schema version 1. insert some data using SQL queries.
//        // You cannot use DAO classes because they expect the latest schema.
//        db.execSQL("")
//
//        // Prepare for the next version.
//        db.close()
//
//        // Re-open the database with version 2 and provide
//        // MIGRATION_1_2 as the migration process.
//        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, Migration1())
//
//        // MigrationTestHelper automatically verifies the schema changes,
//        // but you need to validate that the data was migrated properly.
    }
}
