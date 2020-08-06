package com.app.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.UnitTest
import com.app.service.datasource.ReposTellerRepository
import com.app.service.logger.Logger
import com.app.service.pendingtasks.PendingTasks
import com.app.service.type.FcmTopicKey
import com.app.service.util.DataNotification
import com.levibostian.teller.repository.OnlineRepository
import com.levibostian.teller.testing.extensions.success
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

@RunWith(AndroidJUnit4::class)
class BackgroundJobRunnerTest : UnitTest() {

    @Mock lateinit var pendingTasks: PendingTasks
    @Mock lateinit var tellerRepository: ReposTellerRepository
    @Mock lateinit var logger: Logger

    private lateinit var runner: BackgroundJobRunner

    @Before
    override fun setup() {
        super.setup()

        runner = BackgroundJobRunner(pendingTasks, tellerRepository, logger)
    }

    @Test
    fun `runPeriodicJobs - expect run all pending tasks`() {
        whenever(tellerRepository.refresh(any())).thenReturn(Single.just(OnlineRepository.RefreshResult.Testing.success()))

        runner.runPeriodicJobs()

        verify(pendingTasks).runAllTasks()
        verify(tellerRepository).refresh(force = false)
    }

    @Test
    fun `handleDataPushNotification - given data notification without action, expect nothing to run`() {
        runner.handleDataPushNotification(DataNotification("no-action-topic-here"))

        verifyZeroInteractions(pendingTasks)
        verifyZeroInteractions(tellerRepository)
    }

    @Test
    fun `handleDataPushNotification - given update program action, expect run sync programs`() {
        whenever(tellerRepository.refresh(force = true)).thenReturn(Single.just(OnlineRepository.RefreshResult.Testing.success()))

        runner.handleDataPushNotification(DataNotification(FcmTopicKey.ProductUpdated.fcmName))

        verifyZeroInteractions(pendingTasks)
        verify(tellerRepository).refresh(force = true)
    }

    @Test
    fun `syncPrograms - expect set requirements before run refresh`() {
        whenever(tellerRepository.refresh(force = true)).thenReturn(Single.just(OnlineRepository.RefreshResult.Testing.success()))

        runner.syncRepos(force = true)

        verify(tellerRepository).requirements = notNull()
    }
}
