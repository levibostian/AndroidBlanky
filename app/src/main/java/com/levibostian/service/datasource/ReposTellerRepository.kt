package com.levibostian.service.datasource

import com.levibostian.service.model.RepoModel
import com.levibostian.service.repository.ReposRepository
import com.levibostian.teller.repository.OnlineRepository
import com.levibostian.teller.type.Age
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReposTellerRepository @Inject constructor(private val reposRepository: ReposRepository) : OnlineRepository<List<RepoModel>, ReposTellerRepository.GetRequirements, List<RepoModel>>() {

    override var maxAgeOfCache: Age = Age(1, Age.Unit.HOURS)

    override fun fetchFreshCache(requirements: GetRequirements): Single<FetchResponse<List<RepoModel>>> {
        return reposRepository.getRepos(requirements.username)
            .map { result ->
                if (result.isFailure) FetchResponse.fail(result.exceptionOrNull()!!)
                else FetchResponse.success(result.getOrNull()!!)
            }
    }

    public override fun saveCache(cache: List<RepoModel>, requirements: GetRequirements) {
        reposRepository.replaceRepos(cache, requirements.username)
    }

    override fun observeCache(requirements: GetRequirements): Observable<List<RepoModel>> {
        return reposRepository.observeReposForUsername(requirements.username)
            .subscribeOn(Schedulers.io())
    }

    override fun isCacheEmpty(cache: List<RepoModel>, requirements: GetRequirements): Boolean = cache.isEmpty()

    class GetRequirements(val username: String) : GetCacheRequirements {
        override var tag: String = "GitHub repositories for user: $username"
    }
}
