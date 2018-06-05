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
                    val fetchResponse: FetchResponse<List<RepoModel>>
                    val responseError: Throwable? = responseProcessor.process(result)

                    if (responseError != null) {
                        fetchResponse = FetchResponse.fail(responseError)
                    } else {
                        val response = result.response()!!
                        fetchResponse = if (!response.isSuccessful) {
                            when (response.code()) {
                                404 -> {
                                    FetchResponse.fail("The username ${requirements.username} does not exist. Try another one.")
                                }
                                else -> {
                                    // I do not like when apps say, "Unknown error. Please try again". It's terrible to do. But if it ever happens, that means you need to handle more HTTP status codes. Above are the only ones that I know GitHub will return. They don't document the rest of them, I don't think?
                                    FetchResponse.fail("Unknown error. Please, try again.")
                                }
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