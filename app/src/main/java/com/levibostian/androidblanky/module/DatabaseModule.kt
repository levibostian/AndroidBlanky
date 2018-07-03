package com.levibostian.androidblanky.module

import android.app.Application
import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager

interface DatabaseModule {

    fun provideDatabase(application: Application): Database

}