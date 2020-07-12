package com.levibostian.service.repository

import com.levibostian.service.api.GitHubApi
import com.levibostian.service.db.Database
import com.levibostian.service.model.RepoModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class ReposRepository @Inject constructor(private val api: GitHubApi,
                                          private val db: Database) {

    fun getRepos(username: String): Single<Result<List<RepoModel>>> {
        return api.getRepos(username)
    }

    fun replaceRepos(repos: List<RepoModel>, forUsername: String) {
        db.reposDao().insertRepos(repos)
    }

    fun observeReposForUsername(username: String): Observable<List<RepoModel>> {
        return db.reposDao().observeReposForUser(username).toObservable()
    }


}