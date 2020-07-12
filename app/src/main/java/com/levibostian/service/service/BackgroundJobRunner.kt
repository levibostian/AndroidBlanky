package com.levibostian.service.service

import com.levibostian.service.datasource.ReposTellerRepository
import com.levibostian.service.logger.ActivityEvent
import com.levibostian.service.logger.ActivityEventParamKey
import com.levibostian.service.logger.Logger
import com.levibostian.service.pendingtasks.PendingTasks
import com.levibostian.service.util.DataNotification
import com.levibostian.service.util.NotificationAction
import javax.inject.Inject

/**
 * When some tasks need to be done in the background, this class runs those tasks for you.
 *
 * Note: All of the functions below will be run in the background so we use synchronous returns.
 */
class BackgroundJobRunner @Inject constructor(private val pendingTasks: PendingTasks, private val reposTellerRepository: ReposTellerRepository, private val logger: Logger) {

    /**
     * WorkManager runs background jobs periodically when the app is in the background. Run those jobs here.
     *
     * @return true if the jobs ran successfully.
     */
    fun runPeriodicJobs(): Boolean {
        logger.appEventOccurred(ActivityEvent.PerformBackgroundSync, mapOf(
                Pair(ActivityEventParamKey.Type, "periodic")
        ))
        pendingTasks.runAllTasks()
        syncRepos(force = false)

        return true
    }

    fun handleDataPushNotification(dataNotification: DataNotification) {
        val never = when (dataNotification.action) {
            NotificationAction.UPDATE_PROGRAM -> {
                logger.appEventOccurred(ActivityEvent.PerformBackgroundSync, mapOf(
                        Pair(ActivityEventParamKey.Type, "push notification")
                ))

                syncRepos(force = true)

                Unit // we don't want to return a value from `when`. We only need to respond to the data notification.
            }
            null -> {}
        }
    }

    /**
     * Refresh the workout program for the app.
     *
     * @return true if the jobs ran successfully.
     */
    fun syncRepos(force: Boolean): Boolean {
        reposTellerRepository.requirements = ReposTellerRepository.GetRequirements("foo")

        val result = reposTellerRepository.refresh(force).blockingGet()

        return result.successful
    }

}