package com.storage.cloudbackup.logic.scheduler.receiver

import android.content.Context
import com.storage.cloudbackup.SharedApp
import java.time.LocalDateTime

object Validator {
    fun validate(context: Context): Boolean {
        val app = SharedApp.getInstance(context)

        if(!app.updatePermit.canUpdate){
            app.alarmScheduler.scheduleAt(LocalDateTime.now().plusHours(1))
            return false
        }

        return true
    }
}