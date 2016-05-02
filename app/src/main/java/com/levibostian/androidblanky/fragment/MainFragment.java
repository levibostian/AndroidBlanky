package com.levibostian.androidblanky.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.levibostian.androidblanky.MainApplication;
import com.levibostian.androidblanky.R;
import com.levibostian.androidblanky.manager.api.BaseApiManager;
import com.levibostian.androidblanky.manager.api.ReposApiManager;
import com.levibostian.androidblanky.service.GitHubApi;
import com.levibostian.androidblanky.util.LogUtil;
import com.levibostian.androidblanky.vo.RepoVo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

public class MainFragment extends BaseFragment {

    @Inject ReposApiManager mReposApiManager;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainApplication.component().inject(this);

        mReposApiManager.getRepos("curiosityio", new BaseApiManager.ApiResponseCallback<List<RepoVo>>() {
            @Override
            public void success(List<RepoVo> repos) {
                LogUtil.d("Success. Number repos: " + repos.size());
            }

            @Override
            public void apiError(String message) {
                LogUtil.d("Fail getting repos");
            }

            @Override
            public void failure(String message) {
                LogUtil.d("Fail getting repos");
            }
        });
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

}
