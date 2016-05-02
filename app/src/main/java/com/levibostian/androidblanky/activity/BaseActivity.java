package com.levibostian.androidblanky.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.levibostian.androidblanky.util.CustomTabsHelper;
import com.levibostian.androidblanky.util.IntentUtil;

public abstract class BaseActivity extends AppCompatActivity {

    private CustomTabsServiceConnection mCustomTabsServiceConnection;
    private CustomTabsSession mChromeCustomTabsSession;
    private String mPotentialUrlToLaunchChromeCustomTab;

    // must call before onStart().
    // In fragments, call `getBaseActivity().setPotentialUrlToLaunchChromeCustomTab(url);` to warm up chrome custom tabs even faster
    public void setPotentialUrlToLaunchChromeCustomTab(String url) {
        mPotentialUrlToLaunchChromeCustomTab = url;
    }

    @Override
    protected void onStart() {
        super.onStart();

        warmupChromeCustomTab();
    }

    // In fragments, call `getBaseActivity().openWebpage(url);` to open chrome custom tab.
    public void openWebpage(String url) {
        IntentUtil.openWebpageUrlIntent(mChromeCustomTabsSession, this, url);
    }

    private void warmupChromeCustomTab() {
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                client.warmup(0);

                mChromeCustomTabsSession = client.newSession(new CustomTabsCallback());
                if (mPotentialUrlToLaunchChromeCustomTab != null) {
                    mChromeCustomTabsSession.mayLaunchUrl(Uri.parse(mPotentialUrlToLaunchChromeCustomTab), null, null);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        CustomTabsClient.bindCustomTabsService(this, CustomTabsHelper.getPackageNameToUse(this), mCustomTabsServiceConnection);
    }

    @Override
    protected void onDestroy() {
        unbindService(mCustomTabsServiceConnection);

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    // view transition animations with lollipop and above.
    // `startActivityWithTransition(DestActivity.getIntent(), srcViewToTransition, R.string.view_transition_name)`
    protected void startActivityWithTransition(Intent intent, View transitionView, int transitionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, transitionView, getString(transitionName));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    protected void startActivityWithTransitions(Intent intent, Pair<View, String>... transitions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, transitions);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

}
