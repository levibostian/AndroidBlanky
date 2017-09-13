package com.levibostian.androidblanky.service

import com.levibostian.androidblanky.service.model.RepoModel
import retrofit2.http.GET
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.Path

open interface GitHubService {

    @GET("users/{user}/repos")
    open fun getRepos(@Path("user") user: String): Single<Result<List<RepoModel>>>

}