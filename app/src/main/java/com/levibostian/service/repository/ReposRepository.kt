package com.levibostian.service.repository

import com.levibostian.service.GitHubService
import com.levibostian.service.util.ResponseProcessor
import com.levibostian.service.db.Database
import com.levibostian.service.error.network.NotFoundResponseError
import com.levibostian.service.model.RepoModel
import com.levibostian.teller.repository.OnlineRepository
import com.levibostian.teller.type.Age
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReposRepository @Inject constructor(private val responseProcessor: ResponseProcessor,
                                          private val service: GitHubService,
                                          private val db: Database): OnlineRepository<List<RepoModel>, ReposRepository.GetRequirements, List<RepoModel>>() {

    override var maxAgeOfCache: Age = Age(1, Age.Unit.HOURS)

    override fun fetchFreshCache(requirements: GetRequirements): Single<FetchResponse<List<RepoModel>>> {
        return service.listRepos(requirements.username)
                .map { result ->
                    responseProcessor.process(result) { code, _, _ ->
                        when (code) {
                            404 -> NotFoundResponseError("The username ${requirements.username} does not exist. Try another one.")
                            else -> null
                        }
                    }
                }
    }

    public override fun saveCache(cache: List<RepoModel>, requirements: GetRequirements) {
        db.reposDao().insertRepos(cache)
    }

    override fun observeCache(requirements: GetRequirements): Observable<List<RepoModel>> {
        return db.reposDao().observeReposForUser(requirements.username).toObservable()
                .subscribeOn(Schedulers.io())
    }

    override fun isCacheEmpty(cache: List<RepoModel>, requirements: GetRequirements): Boolean = cache.isEmpty()

    class GetRequirements(val username: String): GetCacheRequirements {
        override var tag: String = "GitHub repositories for user: $username"
    }

}