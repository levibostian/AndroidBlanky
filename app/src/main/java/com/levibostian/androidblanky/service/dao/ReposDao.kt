package com.levibostian.androidblanky.service.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.levibostian.androidblanky.service.model.RepoModel
import io.reactivex.Flowable

@Dao
interface ReposDao {

    @Query("SELECT * FROM repo WHERE repo_owner_name = :name")
    fun observeReposForUser(name: String): Flowable<List<RepoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repos: List<RepoModel>)

}