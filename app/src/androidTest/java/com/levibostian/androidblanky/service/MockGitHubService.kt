package com.levibostian.androidblanky.service

import com.levibostian.androidblanky.service.model.OwnerModel
import com.levibostian.androidblanky.service.model.RepoModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

class MockGitHubService : GitHubService {

    val repo1 = RepoModel("name1", "description1", OwnerModel("login1"))
    val repo2 = RepoModel("name2", "description2", OwnerModel("login2"))
    val repo3 = RepoModel("name3", "description3", OwnerModel("login3"))

    override fun getRepos(user: String): Single<Result<List<RepoModel>>> {
        return Result.response(Response.success(listOf(repo1, repo2, repo3))).toSingle()
    }

}