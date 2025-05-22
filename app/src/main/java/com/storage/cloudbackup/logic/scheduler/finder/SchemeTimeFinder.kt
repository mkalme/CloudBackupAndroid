package com.storage.cloudbackup.logic.scheduler.finder

import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.scheduler.finder.schedule.ScheduleTimeFinder
import java.time.LocalDateTime

class SchemeTimeFinder : NearestUpdateTimeFinder<Scheme> {
    private var scheduleTimeFinder: NearestUpdateTimeFinder<Schedule> = ScheduleTimeFinder()

    override fun getNearestTime(args: Scheme, currentTime: LocalDateTime, findType: FindType): LocalDateTime {
        var date = if(findType == FindType.After) LocalDateTime.MAX else LocalDateTime.MIN

        args.schedules.forEach {
            val thisDate = scheduleTimeFinder.getNearestTime(it, currentTime, findType)

            if(findType == FindType.After){
                if(thisDate.isBefore(date)) date = thisDate
            }else{
                if(thisDate.isAfter(date)) date = thisDate
            }
        }

        return date
    }
}