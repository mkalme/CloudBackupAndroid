package com.storage.cloudbackup.logic.scheduler.alarm.scheduler

import java.time.LocalDateTime

interface AlarmScheduler {
    fun scheduleAt(time: LocalDateTime)
    fun cancel()
}