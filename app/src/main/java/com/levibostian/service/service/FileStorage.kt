package com.levibostian.service.service

import android.content.Context
import android.os.FileUtils
import android.system.Os.open
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.nio.file.Files
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handy way to read and write to the file system. By default, reading and writing is for..
 * 1. Strings only.
 * 2. Internal app storage only.
 * 3. Small files only.
 *
 * The reason for these defaults is...
 * 1. No asking for runtime permissions.
 * 2. Easy to work with data type (String).
 * 3. Private data by default.
 *
 * The downfalls of this set of defaults is...
 * 1. Private data only. Only your app can read this data.
 * 2. Only data type is String. If you want to use binary or media files, you will need to create more functions or create another class to do that.
 * 3. Internal storage cannot hold a lot of data. Also, we are using in-memory reading and writing files. You may want to use streams for large data sets.
 *
 * https://developer.android.com/training/data-storage/app-specific
 */
@Singleton // we require a singleton because we have observables in memory
class FileStorage @Inject constructor(private val context: Context) {

    private var fileObservables: MutableMap<Destination, BehaviorSubject<String>> = mutableMapOf()

    /**
     * Use: `readAssetString("dir/file.json")`
     */
    @Throws(FileNotFoundException::class)
    fun readAssetString(path: String): String {
        return context.assets.open(path).bufferedReader().use { it.readText() }
    }

    /**
     * Reads file to a string.
     *
     * Returns null if file does not exist or is empty.
     */
    fun readFile(destination: Destination): String? {
        val dir = context.getDir(destination.dirName, Context.MODE_PRIVATE)
        val file = File(dir, destination.fileName)

        if (!file.exists()) return null

        val contents = file.readText()
        return if (contents.isBlank()) null else contents
    }

    fun writeToFile(contents: String, destination: Destination) {
        val dir = context.getDir(destination.dirName, Context.MODE_PRIVATE)
        val file = File(dir, destination.fileName)

        FileWriter(file).apply {
            write(contents)
            close()
        }

        fileContentsChanged(destination, contents)
    }

    fun deleteFile(destination: Destination) {
        val dir = context.getDir(destination.dirName, Context.MODE_PRIVATE)
        val file = File(dir, destination.fileName)

        if (file.exists()) file.delete()
    }

    fun deleteAll() {
        Destination.values().forEach { destination -> deleteFile(destination) }
    }

    private fun fileContentsChanged(destination: Destination, contents: String) {
        fileObservables[destination]?.onNext(contents)
    }

    /**
     * Does not emit when the contents are empty. It's recommended to check if the file exists before observing.
     */
    fun observeFile(destination: Destination): Observable<String> {
        var existingObservable = fileObservables[destination]

        if (existingObservable == null) {
            existingObservable = BehaviorSubject.create()

            readFile(destination)?.let { existingFileContents ->
                existingObservable.onNext(existingFileContents)
            }
        }

        fileObservables[destination] = existingObservable

        return existingObservable
    }

    enum class Destination {
        PROGRAM_WORKOUTS {
            override val dirName: String = "docs"
            override val fileName: String= "program_workouts.json"
        };

        abstract val dirName: String
        abstract val fileName: String
        val path: String
            get() = "$dirName/$fileName"
    }

}