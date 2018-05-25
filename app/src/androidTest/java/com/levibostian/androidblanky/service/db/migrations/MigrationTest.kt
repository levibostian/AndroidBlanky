package com.levibostian.androidblanky.service.db.migrations

import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.support.test.InstrumentationRegistry
import android.arch.persistence.room.testing.MigrationTestHelper
import com.levibostian.androidblanky.service.db.Database
import org.junit.Test
import java.io.IOException
import android.arch.persistence.db.SupportSQLiteDatabase

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    companion object {
        private const val TEST_DB = "migration-test"
    }

    @Rule var helper: MigrationTestHelper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), Database::class.java.canonicalName, FrameworkSQLiteOpenHelperFactory())

//    @Test fun migrate1to2() {
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
//    }

}
