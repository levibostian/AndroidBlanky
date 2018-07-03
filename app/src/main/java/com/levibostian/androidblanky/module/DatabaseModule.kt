package com.levibostian.androidblanky.module

import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager

interface DatabaseModule {

    fun provideDatabase(): Database

}