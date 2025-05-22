package com.storage.cloudbackup.logic.scheduler

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.storage.cloudbackup.SharedApp

class AutomaticUpdateService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val serviceNotification = SharedApp.getInstance(this).serviceNotification
        startForeground(serviceNotification.getNotificationId(), serviceNotification.getNotification())

        Thread {
            ScheduleUpdateProcess().launch(this)
            stopForeground(STOP_FOREGROUND_DETACH)
        }.start()

        return START_NOT_STICKY
    }
}