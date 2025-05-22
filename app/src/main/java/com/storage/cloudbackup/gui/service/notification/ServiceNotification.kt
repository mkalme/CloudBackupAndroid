package com.storage.cloudbackup.gui.service.notification

import android.app.Notification

interface ServiceNotification {
    fun getNotificationId(): Int
    fun getNotification(): Notification
}