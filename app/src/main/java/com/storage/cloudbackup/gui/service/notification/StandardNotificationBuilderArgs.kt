package com.storage.cloudbackup.gui.service.notification

import android.app.NotificationManager
import android.content.Context

data class StandardNotificationBuilderArgs(
    val notificationManager: NotificationManager,
    val notificationId: Int,
    val channelId: String,
    val context: Context)
