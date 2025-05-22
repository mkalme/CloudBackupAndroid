package com.storage.cloudbackup.logic.model.item.schedule

import java.time.LocalDateTime

class MonthlySchedule : Schedule() {
    var monthDay: Byte = 1
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    override val type: ScheduleType
        get() = ScheduleType.Monthly

    init {
        setBeginDate(LocalDateTime.now())
    }

    override fun setBeginDate(time: LocalDateTime) {
        beginTime = LocalDateTime.of(time.year, time.month, 1, 0, 0, 0)
    }

    override fun clone(): Schedule {
        val output = MonthlySchedule()
        output.monthDay = monthDay

        deepClone(output)
        return output
    }
}