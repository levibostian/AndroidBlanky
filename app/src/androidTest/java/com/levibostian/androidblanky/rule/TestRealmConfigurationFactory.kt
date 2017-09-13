package com.levibostian.androidblanky.rule

/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.rules.TemporaryFolder;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.realm.CompactOnLaunchCallback;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration

import org.junit.Assert.assertTrue;

/**
 * Rule that creates the {@link RealmConfiguration } in a temporary directory and deletes the Realm created with that
 * configuration once the test finishes. Be sure to close all Realm instances before finishing the test. Otherwise
 * {@link Realm#deleteRealm(RealmConfiguration)} will throw an exception in the {@link #after()} method.
 * The temp directory will be deleted regardless if the {@link Realm#deleteRealm(RealmConfiguration)} fails or not.
 */
class TestRealmConfigurationFactory: TemporaryFolder() {

    private val map = ConcurrentHashMap<RealmConfiguration, Boolean>()
    private val configurations = Collections.newSetFromMap(map)

    private var unitTestFailed = false

    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                before()
                try {
                    base.evaluate()
                } catch (throwable: Throwable) {
                    setUnitTestFailed()
                    throw throwable
                } finally {
                    after()
                }
            }
        }
    }

    @Throws(Throwable::class)
    override fun before() {
        super.before()
        Realm.init(InstrumentationRegistry.getTargetContext())
    }

    override fun after() {
        try {
            for (configuration in configurations) {
                Realm.deleteRealm(configuration)
            }
        } catch (e: IllegalStateException) {
            // Only throws the exception caused by deleting the opened Realm if the test case itself doesn't throw.
            if (!isUnitTestFailed()) {
                throw e
            }
        } finally {
            // This will delete the temp directory.
            super.after()
        }
    }

    @Synchronized fun setUnitTestFailed() {
        this.unitTestFailed = true
    }

    @Synchronized private fun isUnitTestFailed(): Boolean {
        return this.unitTestFailed
    }

    // This builder creates a configuration that is *NOT* managed.
    // You have to delete it yourself.
    private fun createConfigurationBuilder(): RealmConfiguration.Builder {
        return RealmConfiguration.Builder().directory(root)
    }

    fun createConfiguration(name: String? = null, schemaVersion: Int = 0, migration: RealmMigration? = null): RealmConfiguration {
        val builder = createConfigurationBuilder()

        val folder = root
        assertTrue(folder.mkdirs())
        builder.directory(folder)

        builder.schemaVersion(schemaVersion.toLong())

        migration?.let { builder.migration(it) }
        name?.let { builder.name(it) }

        val configuration = builder.build()
        configurations.add(configuration)

        return configuration
    }

    // Copies a Realm file from assets to temp dir
    @Throws(IOException::class)
    fun copyRealmFromAssets(context: Context, realmPath: String, newName: String) {
        val config = RealmConfiguration.Builder()
                .directory(root)
                .name(newName)
                .build()

        copyRealmFromAssets(context, realmPath, config)
    }

    @Throws(IOException::class)
    fun copyRealmFromAssets(context: Context, realmPath: String, config: RealmConfiguration) {
        // Deletes the existing file before copy
        Realm.deleteRealm(config)

        val outFile = File(config.realmDirectory, config.realmFileName)

        var stream: InputStream? = null
        var os: FileOutputStream? = null
        try {
            stream = context.assets.open(realmPath)!!
            os = FileOutputStream(outFile)

            stream.copyTo(os, 1024)
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (ignore: IOException) {}
            }
            if (os != null) {
                try {
                    os.close()
                } catch (ignore: IOException) { }
            }
        }
    }

}