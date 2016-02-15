package com.levibostian.androidblanky.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.levibostian.androidblanky.MainApplication;
import com.levibostian.androidblanky.R;
import com.levibostian.androidblanky.service.GitHubApi;
import com.levibostian.androidblanky.util.LogUtil;
import com.levibostian.androidblanky.vo.RepoVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

public class MainFragment extends Fragment {

    @Inject GitHubApi mGitHubApi;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainApplication.inject(this);

        Call<List<RepoVo>> call = mGitHubApi.listRepos("levibostian");
        call.enqueue(new Callback<List<RepoVo>>() {
            @Override
            public void onResponse(Call<List<RepoVo>> call, Response<List<RepoVo>> response) {
                LogUtil.d("Success. Number repos: " + response.body().size());
            }

            @Override
            public void onFailure(Call<List<RepoVo>> call, Throwable t) {
                LogUtil.d("Fail getting repos");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

}
