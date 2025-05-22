package com.storage.cloudbackup.gui.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.storage.cloudbackup.R

class StandardServiceNotification(val context: Context) : ServiceNotification {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "User initiated update foreground service"
    private val notificationId = 5
    private val channel = NotificationChannel(channelId, "Progress Notifications", NotificationManager.IMPORTANCE_LOW)

    init {
        notificationManager.createNotificationChannel(channel)
    }

    fun getNotificationManager(): NotificationManager{
        return notificationManager
    }

    fun getChannelId(): String {
        return channelId
    }

    override fun getNotificationId(): Int {
        return notificationId
    }

    override fun getNotification(): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.app_icon)
            .setContentTitle("Update started")
            .setColor(Color.BLACK)
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }
}