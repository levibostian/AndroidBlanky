package com.app.service

import android.os.AsyncTask
import com.app.service.db.Database
import com.app.service.service.FileStorage
import com.app.service.service.KeyValueStorage
import com.levibostian.teller.Teller
import com.levibostian.wendy.service.Wendy
import javax.inject.Inject

class DataDestroyer @Inject constructor(
    private val db: Database,
    private val fileStorage: FileStorage,
    private val keyValueStorage: KeyValueStorage
) {

    fun destroyAll(complete: (() -> Unit)?) {
        DataDestroyerDestroyAllAsyncTask(this) { error ->
            error?.let { throw it }
            complete?.invoke()
        }.execute()
    }

    // Call on background thread only!
    fun destroyAllSync() {
        destroySqlite()
        deleteAllFiles()
        destroySharedPreferences()
        Wendy.shared.clear() // clears pending tasks
        deleteCacheManager()
    }

    fun deleteAllFiles() {
        fileStorage.deleteAll()
    }

    fun destroySqlite() {
        db.clearAllTables()
    }

    fun destroySharedPreferences() {
        keyValueStorage.deleteAll()
    }

    fun deleteCacheManager() {
        Teller.shared.clear()
    }

    fun destroyWendy(complete: () -> Unit?) {
        Wendy.shared.clearAsync(complete)
    }

    private class DataDestroyerDestroyAllAsyncTask(private val destroyer: DataDestroyer, private val complete: (error: Throwable?) -> Unit?) : AsyncTask<Unit?, Unit?, Unit?>() {

        private var doInBackgroundError: Throwable? = null

        override fun doInBackground(vararg p: Unit?): Unit? {
            try {
                destroyer.destroyAllSync()
            } catch (e: Throwable) {
                doInBackgroundError = e
            }

            return null
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            complete(doInBackgroundError)
        }
    }
}
