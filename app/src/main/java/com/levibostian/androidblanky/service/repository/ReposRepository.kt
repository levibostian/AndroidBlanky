package com.levibostian.androidblanky.service.repository

import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.teller.repository.OnlineRepository
import com.levibostian.teller.type.AgeOfData
import io.reactivex.Observable
import io.reactivex.Single

class ReposRepository(private val responseProcessor: ResponseProcessor,
                      private val service: GitHubService,
                      private val db: Database): OnlineRepository<List<RepoModel>, ReposRepository.GetRequirements, List<RepoModel>>() {

    override var maxAgeOfData: AgeOfData = AgeOfData(1, AgeOfData.Unit.HOURS)

    override fun fetchFreshData(requirements: GetRequirements): Single<FetchResponse<List<RepoModel>>> {
        return service.listRepos(requirements.username)
                .map { result ->
                    val responseError: Throwable? = responseProcessor.process(result)

                    val fetchResponse: FetchResponse<List<RepoModel>> = if (responseError != null) {
                        FetchResponse.fail(responseError)
                    } else {
                        val response = result.response()!!
                        if (!response.isSuccessful) {
                            when (response.code()) {
                                404 -> FetchResponse.fail("The username ${requirements.username} does not exist. Try another one.")
                                else -> FetchResponse.fail(responseProcessor.unhandledHttpResult(result))
                            }
                        } else {
                            FetchResponse.success(response.body()!!)
                        }
                    }

                    fetchResponse
                }
    }

    override fun saveData(data: List<RepoModel>) {
        db.reposDao().insertRepos(data)
    }

    override fun observeCachedData(requirements: GetRequirements): Observable<List<RepoModel>> {
        return db.reposDao().observeReposForUser(requirements.username).toObservable()
    }

    override fun isDataEmpty(data: List<RepoModel>): Boolean = data.isEmpty()

    class GetRequirements(val username: String): GetDataRequirements {
        override var tag: String = "${this::class.java.simpleName}_$username"
    }

}