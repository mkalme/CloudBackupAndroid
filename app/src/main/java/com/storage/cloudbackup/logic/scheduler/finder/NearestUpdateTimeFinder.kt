package com.storage.cloudbackup.logic.scheduler.finder

import java.time.LocalDateTime

interface NearestUpdateTimeFinder<T> {
    fun getNearestTime(args: T, currentTime: LocalDateTime, findType: FindType): LocalDateTime
}