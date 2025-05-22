package com.storage.cloudbackup.logic.scheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.storage.cloudbackup.logic.scheduler.AutomaticUpdateService

class UpdateBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(Validator.validate(context)){
            val serviceIntent = Intent(context, AutomaticUpdateService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}
