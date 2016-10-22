package com.levibostian.androidblanky.activity

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.customtabs.CustomTabsCallback
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsServiceConnection
import android.support.customtabs.CustomTabsSession
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.levibostian.androidblanky.util.CustomTabsHelper
import com.levibostian.androidblanky.util.IntentUtil
import android.support.v4.util.Pair

abstract class BaseActivity : AppCompatActivity() {

    lateinit private var mCustomTabsServiceConnection: CustomTabsServiceConnection
    private var mChromeCustomTabsSession: CustomTabsSession? = null
    private var mPotentialUrlToLaunchChromeCustomTab: String? = null

    // must call before onStart().
    // In fragments, call `getBaseActivity().setPotentialUrlToLaunchChromeCustomTab(url);` to warm up chrome custom tabs even faster
    fun setPotentialUrlToLaunchChromeCustomTab(url: String) {
        mPotentialUrlToLaunchChromeCustomTab = url
    }

    override fun onStart() {
        super.onStart()

        warmupChromeCustomTab()
    }

    fun openWebpage(url: String) {
        IntentUtil.openWebpageUrlIntent(mChromeCustomTabsSession, this, url)
    }

    private fun warmupChromeCustomTab() {
        val chromePackageName: String? = CustomTabsHelper.getPackageNameToUse(this)

        if (chromePackageName != null) {
            mCustomTabsServiceConnection = object: CustomTabsServiceConnection() {
                override fun onCustomTabsServiceConnected(name: ComponentName?, client: CustomTabsClient?) {
                    client?.warmup(0)

                    mChromeCustomTabsSession = client?.newSession(CustomTabsCallback())
                    if (mPotentialUrlToLaunchChromeCustomTab != null) {
                        mChromeCustomTabsSession?.mayLaunchUrl(Uri.parse(mPotentialUrlToLaunchChromeCustomTab), null, null)
                    }
                }

                override fun onServiceDisconnected(p0: ComponentName?) {
                }
            }

            CustomTabsClient.bindCustomTabsService(this, chromePackageName, mCustomTabsServiceConnection)
        }
    }

    override fun onDestroy() {
        unbindService(mCustomTabsServiceConnection)

        super.onDestroy()
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    // view transition animations with lollipop and above.
    // `startActivityWithTransition(DestActivity.getIntent(), srcViewToTransition, R.string.view_transition_name)`
    protected fun startActivityWithTransition(intent: Intent, transitionView: View, transitionName: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, transitionView, getString(transitionName))
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    protected fun startActivityWithTransition(intent: Intent, vararg transitions: Pair<View, String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *transitions)
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

}