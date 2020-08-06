package com.app.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.app.matcher.OrientationChangeAction
import com.levibostian.recyclerviewmatcher.RecyclerViewMatcher
import com.levibostian.swapper.SwapperView

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Found: https://github.com/googlesamples/android-architecture-components
object EspressoTestUtil {

    fun disableAnimations(activity: FragmentActivity) {
        disableProgressBarAnimations(activity)

        SwapperView.config.animationDuration = 0
    }

    /**
     * Disables progress bar animations for the views of the given activity rule
     *
     * @param activityTestRule The activity rule whose views will be checked
     */
    fun disableProgressBarAnimations(activity: FragmentActivity) {
        activity.supportFragmentManager
            .registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewCreated(
                        fm: FragmentManager,
                        f: Fragment,
                        v: View,
                        savedInstanceState: Bundle?
                    ) {
                        // traverse all views, if any is a progress bar, replace its animation
                        traverseViews(v)
                    }
                },
                true
            )
    }

    private fun traverseViews(view: View?) {
        if (view is ViewGroup) {
            traverseViewGroup(view)
        } else if (view is ProgressBar) {
            disableProgressBarAnimation(view)
        }
    }

    private fun traverseViewGroup(view: ViewGroup) {
        val count = view.childCount
        (0 until count).forEach {
            traverseViews(view.getChildAt(it))
        }
    }

    /**
     * necessary to run tests on older API levels where progress bar uses handler loop to animate.
     *
     * @param progressBar The progress bar whose animation will be swapped with a drawable
     */
    private fun disableProgressBarAnimation(progressBar: ProgressBar) {
        progressBar.indeterminateDrawable = ColorDrawable(Color.BLUE)
    }

    fun scrollToTopOfList(recyclerViewId: Int) {
        onView(withId(recyclerViewId))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
    }

    /**
     * Way to run view assertions on RecylerViews.
     *
     * Example:
     assertListItems(programsRecyclerViewId, ProgramListItem.realistics) { index, item ->
     matches(hasDescendant(withText((item as ProgramListItem.Prog).program.name)))
     }
     */
    fun <T> assertListItems(recyclerViewId: Int, list: List<T>, onViewAssertion: (Int, T) -> ViewAssertion) {
        list.forEachIndexed { index, item ->
            onView(withId(recyclerViewId))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))

            onView(RecyclerViewMatcher.recyclerViewWithId(recyclerViewId).itemViewAtIndex(index))
                .check(onViewAssertion(index, item))
        }
    }

    /**
     * Handy if you want to only check if a RecyclerView item is visible without scrolling. Use if you have a complex list (such as a Hash data structure) and it's difficult to use [assertListItems].
     */
    fun assertVisibleListItem(recyclerViewId: Int, onViewAssertions: List<ViewAssertion>, startingIndex: Int = 0) {
        onViewAssertions.forEachIndexed { index, viewAssertion ->
            onView(RecyclerViewMatcher.recyclerViewWithId(recyclerViewId).itemViewAtIndex(index + startingIndex))
                .check(viewAssertion)
        }
    }

    fun orientationLandscape() {
        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape())
    }

    fun orientationPortrait() {
        onView(isRoot()).perform(OrientationChangeAction.orientationPortrait())
    }
}
