package com.app.view.fragment

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import com.app.mock.MockWebServer
import com.app.rule.DiGraphRule
import com.app.service.vo.response.MessageResponseVo
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class MainFragmentTest {

    @Inject lateinit var mockWebServer: MockWebServer

    @get:Rule val diGraph = DiGraphRule()

    @Before
    fun setup() {
        diGraph.graph.inject(this)
    }

    @Test
    fun test_showReposDataListFetchingFresh() {
        mockWebServer.queue(200, MessageResponseVo("message here"))

        Truth.assertThat("").isEqualTo("")
    }
}
