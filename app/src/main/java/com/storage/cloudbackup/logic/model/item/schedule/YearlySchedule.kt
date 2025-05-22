package com.storage.cloudbackup.logic.model.item.schedule

import java.time.LocalDateTime

class YearlySchedule : Schedule() {
    var month: Month = Month.January
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var monthDay: Byte = 1
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    override val type: ScheduleType
        get() = ScheduleType.Yearly

    init {
        setBeginDate(LocalDateTime.now())
    }

    override fun setBeginDate(time: LocalDateTime) {
        beginTime = LocalDateTime.of(time.year, 1, 1, 0, 0, 0)
    }

    override fun clone(): Schedule {
        val output = YearlySchedule()
        output.month = month
        output.monthDay = monthDay

        deepClone(output)
        return output
    }
}