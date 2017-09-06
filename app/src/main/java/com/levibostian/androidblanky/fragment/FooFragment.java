package com.levibostian.androidblanky.fragment;


import com.levibostian.androidblanky.vo.RepoVo;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;

public class FooFragment {

    public void fol() {
        DisposableSingleObserver<List<RepoVo>> observer = new DisposableSingleObserver<List<RepoVo>>() {
            @Override public void onSuccess(@NonNull List<RepoVo> repoVos) {

            }
            @Override public void onError(@NonNull Throwable e) {

            }
        };
    }

}
