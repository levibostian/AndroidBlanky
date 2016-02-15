package com.levibostian.androidblanky.service;

import com.levibostian.androidblanky.vo.RepoVo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface GitHubApi {

    @GET("users/{user}/repos")
    Call<List<RepoVo>> listRepos(@Path("user") String user);

}
