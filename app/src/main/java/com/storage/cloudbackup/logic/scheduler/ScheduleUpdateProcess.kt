package com.storage.cloudbackup.logic.scheduler

import android.content.Context
import android.os.PowerManager
import com.storage.cloudbackup.SharedApp
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.scheduler.updater.StandardScheduledUpdater
import com.storage.cloudbackup.logic.shared.utilities.utilities.AlarmUtilities
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ScheduleUpdateProcess {
    private val refreshMilliseconds = 100L
    private val timeOutMinutes = 10

    fun launch(context: Context){
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "CloudProviderV2:WakeLockTag"
        )
        wakeLock.acquire()

        try {
            val app = SharedApp.getInstance(context)
            if (waitForCloudProvidersToOpen(app.cloudProviders)) beginUpdate(app, context)
        }finally {
            wakeLock.release()
        }
    }

    private fun waitForCloudProvidersToOpen(cloudProviders: List<CloudProvider>): Boolean {
        val begin = LocalDateTime.now()

        while(!areAllCloudProvidersOpen(cloudProviders)){
            Thread.sleep(refreshMilliseconds)

            if(ChronoUnit.MINUTES.between(begin, LocalDateTime.now()) >= timeOutMinutes) return false
        }

        return true
    }

    private fun areAllCloudProvidersOpen(cloudProviders: List<CloudProvider>): Boolean {
        cloudProviders.forEach {
            if(!it.logicComponent.isOpen) return false
        }

        return true
    }
    private fun beginUpdate(app: SharedApp, context: Context){
        val scheduledUpdater = StandardScheduledUpdater(app.updater)
        scheduledUpdater.update(app.updatePlanContainerResource.updatePlanContainer)

        if(app.settingsResource.settings.automaticUpdatingEnabled) AlarmUtilities.setAlarm(app.updatePlanContainerResource.updatePlanContainer, context)
    }
}