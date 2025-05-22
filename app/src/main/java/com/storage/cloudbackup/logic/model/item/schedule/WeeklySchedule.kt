package com.storage.cloudbackup.logic.model.item.schedule

import java.time.LocalDateTime

class WeeklySchedule : Schedule() {
    var weekday: WeekDay = WeekDay.Monday
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    override val type: ScheduleType
        get() = ScheduleType.Weekly

    init {
        setBeginDate(LocalDateTime.now())
    }

    override fun setBeginDate(time: LocalDateTime) {
        val dayOfWeek = time.dayOfWeek
        beginTime = LocalDateTime.of(time.year, time.month, time.dayOfMonth, 0, 0, 0).minusDays((dayOfWeek.value - 1).toLong())
    }

    override fun clone(): Schedule {
        val output = WeeklySchedule()
        output.weekday = weekday

        deepClone(output)
        return output
    }
}