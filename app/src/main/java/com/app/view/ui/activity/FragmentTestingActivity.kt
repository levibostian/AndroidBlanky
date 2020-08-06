package com.app.view.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import com.app.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_fragment_testing.*

/**
 * Activity meant to house a Fragment that is under a UI test.
 *
 * This exists mostly for performing screenshots. Normally, [launchFragmentInContainer] will do great for your UI tests.
 *
 * Inspiration: https://github.com/android/architecture-samples/blob/dev-hilt/app/src/androidTest/java/com/example/android/architecture/blueprints/todoapp/HiltExt.kt#L38 which is linked from: https://developer.android.com/training/dependency-injection/hilt-testing#launchfragment
 */
@AndroidEntryPoint
class FragmentTestingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment_testing)

        setSupportActionBar(toolbar as Toolbar)
        toolbar.visibility = View.GONE // hide unless the test wants to show it.
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.commitNow {
            replace(R.id.fragment_container, fragment)
        }
    }

    /**
     * Call after the Activity has been created already.
     */
    fun showToolbar(title: String?) {
        toolbar.visibility = View.VISIBLE
        supportActionBar!!.title = title
    }
}
