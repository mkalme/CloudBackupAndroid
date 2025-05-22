package com.storage.cloudbackup.gui.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

class UpdateEvent(private val context: Context) : ArgsEvent<List<Scheme>> {
    override fun onInvoke(args: List<Scheme>) {
        val builder = StringBuilder()
        for(i in args.indices){
            builder.append(args[i].id)
            if(i < args.size - 1) builder.append("|")
        }

        val ids = builder.toString()

        val serviceIntent = Intent(context, UserInitiatedUpdateService::class.java)
        serviceIntent.putExtra("ids", ids)
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}