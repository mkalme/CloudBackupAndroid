package com.storage.cloudbackup

import android.content.Context
import com.storage.cloudbackup.gui.notification.StandardNotification
import com.storage.cloudbackup.gui.notification.builder.StandardNotificationBuilder
import com.storage.cloudbackup.gui.service.notification.ServiceNotification
import com.storage.cloudbackup.gui.service.notification.StandardNotificationBuilderArgs
import com.storage.cloudbackup.gui.service.notification.StandardServiceNotification
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.resource.MobileDataUsageResource
import com.storage.cloudbackup.logic.model.resource.SchemeHistoryContainerResource
import com.storage.cloudbackup.logic.model.resource.SettingsResource
import com.storage.cloudbackup.logic.model.resource.UpdatePlanContainerResource
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.edit.StandardEditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.permit.language.StandardLanguagePermit
import com.storage.cloudbackup.logic.permit.update.ShallowUpdatePermit
import com.storage.cloudbackup.logic.permit.update.UpdatePermit
import com.storage.cloudbackup.logic.scheduler.alarm.scheduler.AlarmScheduler
import com.storage.cloudbackup.logic.scheduler.alarm.scheduler.StandardAlarmScheduler
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.watcher.PropertyWatcher
import com.storage.cloudbackup.logic.shared.utilities.observable.watcher.PropertyWatcherArgs
import com.storage.cloudbackup.logic.shared.utilities.utilities.AlarmUtilities
import com.storage.cloudbackup.logic.traffic.MobileDataMonitor
import com.storage.cloudbackup.logic.traffic.data.usage.DataUsageProvider
import com.storage.cloudbackup.logic.traffic.state.NetworkStateProvider
import com.storage.cloudbackup.logic.updater.Updater
import com.storage.cloudbackup.logic.updater.history.UpdateHistory
import com.storage.cloudbackup.logic.updater.standard.updater.StandardUpdater

class SharedApp(context: Context) {
    companion object {
        @Volatile
        private var instance: SharedApp? = null

        fun getInstance(context: Context): SharedApp {
            return instance ?: synchronized(this) {
                instance ?: SharedApp(context).also { instance = it }
            }
        }
    }

    val settingsResource: SettingsResource
    val updatePlanContainerResource: UpdatePlanContainerResource
    val historyResource: SchemeHistoryContainerResource
    val mobileDataUsageResource: MobileDataUsageResource
    val cloudProviders: List<CloudProvider>

    val updater: Updater
    val serviceNotification: ServiceNotification
    val notification: StandardNotification
    val alarmScheduler: AlarmScheduler

    val updatePermit: UpdatePermit
    val editPermit: EditPermit
    val languagePermit: LanguagePermit

    init {
        settingsResource = SettingsResource(context)

        mobileDataUsageResource = MobileDataUsageResource(context)
        MobileDataMonitor(mobileDataUsageResource.mobileDataUsage, DataUsageProvider(), NetworkStateProvider(context))

        updater = StandardUpdater(mobileDataUsageResource.mobileDataUsage, settingsResource.settings, context)

        updatePermit = ShallowUpdatePermit(updater)
        editPermit = StandardEditPermit(updatePermit, settingsResource.settings)
        languagePermit = StandardLanguagePermit(settingsResource.settings)

        cloudProviders = listOf(
            com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.CloudProvider(editPermit, languagePermit, context),
            com.storage.cloudbackup.logic.cloud.provider.providers.s3.CloudProvider(editPermit, languagePermit, context),
            com.storage.cloudbackup.logic.cloud.provider.providers.demo.CloudProvider(editPermit, languagePermit, context)
        )
        cloudProviders.forEach {
            it.logicComponent.load()
            it.guiComponent.load()
        }

        historyResource = SchemeHistoryContainerResource(context)
        updatePlanContainerResource = UpdatePlanContainerResource(context, cloudProviders, historyResource.schemeHistoryContainer)

        UpdateHistory(updater, settingsResource.settings.saveHistoryAttempts)

        val serviceNotification = StandardServiceNotification(context)
        this.serviceNotification = serviceNotification

        val args = StandardNotificationBuilderArgs(
            serviceNotification.getNotificationManager(),
            serviceNotification.getNotificationId(),
            serviceNotification.getChannelId(),
            context)
        val notificationBuilder = StandardNotificationBuilder(args)

        notification = StandardNotification(updater, notificationBuilder, languagePermit)

        alarmScheduler = StandardAlarmScheduler(context)

        updatePlanContainerResource.propertyChangeListener.add(object : EmptyEvent {
            override fun onInvoke() {
                if(settingsResource.settings.automaticUpdatingEnabled) AlarmUtilities.setAlarm(updatePlanContainerResource.updatePlanContainer, context)
            }
        })

        val automaticUpdatingWatcherArgs = PropertyWatcherArgs(
            settingsResource.settings.propertyChangedListener,
            {settingsResource.settings.automaticUpdatingEnabled},
            {
                if(!settingsResource.settings.automaticUpdatingEnabled) AlarmUtilities.cancelAlarm(context)
                else AlarmUtilities.setAlarm(updatePlanContainerResource.updatePlanContainer, context)
            })
        PropertyWatcher(automaticUpdatingWatcherArgs)

        if(settingsResource.settings.automaticUpdatingEnabled) AlarmUtilities.setAlarm(updatePlanContainerResource.updatePlanContainer, context)
    }
}