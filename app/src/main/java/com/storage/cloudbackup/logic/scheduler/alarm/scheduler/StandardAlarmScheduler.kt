package com.storage.cloudbackup.logic.scheduler.alarm.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.storage.cloudbackup.logic.scheduler.receiver.UpdateBroadcastReceiver
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date


class StandardAlarmScheduler(private val context: Context) : AlarmScheduler {
    private val requestCode = 0

    override fun scheduleAt(time: LocalDateTime) {
        Log.d("TAG1", "Scheduled at: $time")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, UpdateBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val zonedDateTime = time.atZone(ZoneId.systemDefault())
        val calendar= Calendar.getInstance()
        calendar.time = Date.from(zonedDateTime.toInstant())

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun cancel() {
        Log.d("TAG1", "Canceled")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, UpdateBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }
}