package com.levibostian.androidblanky.service

import retrofit2.http.GET
import com.levibostian.androidblanky.vo.RepoVo
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Path

interface GitHubService {

    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Observable<List<RepoVo>>

}