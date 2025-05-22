package com.storage.cloudbackup.logic.model.item.schedule

import java.time.LocalDateTime

class DailySchedule : Schedule() {
    override val type: ScheduleType
        get() = ScheduleType.Daily

    init {
        setBeginDate(LocalDateTime.now())
    }

    override fun setBeginDate(time: LocalDateTime) {
        beginTime = LocalDateTime.of(time.year, time.month, time.dayOfMonth, 0, 0, 0)
    }

    override fun clone(): Schedule {
        val output = DailySchedule()
        deepClone(output)
        return output
    }
}