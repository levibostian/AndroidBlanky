package com.levibostian.androidblanky.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import com.levibostian.androidblanky.activity.BaseActivity;

public abstract class BaseFragment extends Fragment {

    //@Inject RefWatcher mRefWatcher;
    protected BaseActivity mActivity;

    protected BaseActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (BaseActivity) getActivity();
        //MainApplication.component().inject(this);
        //mRefWatcher.watch(this);
    }

    public abstract String getTitle();

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(getTitle());
    }

    // Create a pair via: Pair.create(view, getString(R.string.transition_name_here);
    protected void startActivityWithTransitions(Intent intent, Pair<View, String>... transitions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), transitions);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    protected void startActivityWithTransition(Intent intent, View transitionView, int transitionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), transitionView, getString(transitionName));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

}
