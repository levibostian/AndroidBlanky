package com.levibostian.view.fragment

import org.junit.Rule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import javax.inject.Inject
import androidx.test.filters.SdkSuppress
import com.google.common.truth.Truth
import com.levibostian.mock.MockWebServer
import com.levibostian.rule.DiGraphRule
import com.levibostian.service.vo.response.MessageResponseVo

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