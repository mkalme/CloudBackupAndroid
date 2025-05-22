package com.storage.cloudbackup.logic.model.item.schedule

import com.storage.cloudbackup.logic.shared.utilities.observable.resource.ObservableObject
import java.time.LocalDateTime

abstract class Schedule : ObservableObject() {
    var onceEvery: Int = 1
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var timeOfDay: TimeOfDay = TimeOfDay(0, 0)
        set(value){
            val equals = field == value
            field = value
            if(!equals) invokeListener()
        }

    var beginTime: LocalDateTime = LocalDateTime.MIN
        protected set(value){
            val equals = field.isEqual(value)
            field = value
            if(!equals) invokeListener()
        }

    abstract val type: ScheduleType

    abstract fun setBeginDate(time: LocalDateTime)

    abstract fun clone() : Schedule
    protected fun deepClone(schedule: Schedule){
        schedule.onceEvery = onceEvery
        schedule.timeOfDay = timeOfDay.clone()
        schedule.beginTime = beginTime
    }
}