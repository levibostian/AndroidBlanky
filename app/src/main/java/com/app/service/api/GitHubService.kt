package com.app.service.api

import com.app.service.model.RepoModel
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {

    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Single<Result<List<RepoModel>>>
}
