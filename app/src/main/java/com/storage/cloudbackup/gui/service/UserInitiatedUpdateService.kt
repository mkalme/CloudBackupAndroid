package com.storage.cloudbackup.gui.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import com.storage.cloudbackup.SharedApp
import com.storage.cloudbackup.logic.model.item.scheme.Scheme

class UserInitiatedUpdateService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val serviceNotification = SharedApp.getInstance(this).serviceNotification
        startForeground(serviceNotification.getNotificationId(), serviceNotification.getNotification())

        Thread {
            val powerManager = this.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "CloudProviderV2:WakeLockTag"
            )
            wakeLock.acquire()

            try {
                val ids = intent.getStringExtra("ids")!!
                val sIds = ids.split("|")
                val schemes = getSchemes(sIds)
                SharedApp.getInstance(this).updater.updateSchemes(schemes, true)
            }finally {
                wakeLock.release()
            }

            stopForeground(STOP_FOREGROUND_DETACH)
        }.start()

        return START_NOT_STICKY
    }

    private fun getSchemes(ids: List<String>) : List<Scheme> {
        val map = mutableMapOf<String, Scheme>()
        val app = SharedApp.getInstance(this)

        app.updatePlanContainerResource.updatePlanContainer.updatePlans.forEach {
            it.schemes.forEach { scheme ->
                run {
                    map[scheme.id.toString()] = scheme
                }
            }
        }

        val output = mutableListOf<Scheme>()
        ids.forEach {
            val scheme = map[it]
            if(scheme != null) output.add(scheme)
        }

        return output
    }
}

