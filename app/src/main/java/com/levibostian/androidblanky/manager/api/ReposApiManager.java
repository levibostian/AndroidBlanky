package com.levibostian.androidblanky.manager.api;

import com.levibostian.androidblanky.service.GitHubApi;
import com.levibostian.androidblanky.vo.RepoVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.inject.Inject;
import java.util.List;

public class ReposApiManager extends BaseApiManager {

    @Inject
    public ReposApiManager(GitHubApi api, Retrofit retrofit) {
        super(api, retrofit);
    }

    public void getRepos(final String username, final ApiResponseCallback callback) {
        Call<List<RepoVo>> call = mGitHubApi.listRepos(username);
        call.enqueue(new Callback<List<RepoVo>>() {
            @Override
            public void onResponse(Call<List<RepoVo>> call, Response<List<RepoVo>> response) {
                processApiResponse(response, callback);
            }

            @Override
            public void onFailure(Call<List<RepoVo>> call, Throwable t) {
                callback.failure("Error occurred while getting repos. Try again.");
            }
        });
    }

}
