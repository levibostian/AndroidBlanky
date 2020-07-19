package com.levibostian.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.levibostian.service.model.RepoModel
import io.reactivex.Flowable

@Dao
interface ReposDao {

    @Query("SELECT * FROM repo WHERE repo_owner_name = :name")
    fun observeReposForUser(name: String): Flowable<List<RepoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repos: List<RepoModel>)
}