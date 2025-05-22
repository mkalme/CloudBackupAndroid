package com.storage.cloudbackup.logic.scheduler.finder.schedule.variant

import com.storage.cloudbackup.logic.model.item.schedule.DailySchedule
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.scheduler.finder.FindType
import com.storage.cloudbackup.logic.scheduler.finder.NearestUpdateTimeFinder
import com.storage.cloudbackup.logic.shared.utilities.utilities.MathUtilities
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Daily : NearestUpdateTimeFinder<DailySchedule> {
    override fun getNearestTime(args: DailySchedule, currentTime: LocalDateTime, findType: FindType): LocalDateTime {
        val date = getAfterDate(args, currentTime)

        if(findType == FindType.After) return date
        return date.minusDays(args.onceEvery.toLong())
    }

    private fun getAfterDate(args: DailySchedule, currentTime: LocalDateTime): LocalDateTime{
        val duration = ChronoUnit.DAYS.between(args.beginTime, currentTime).toInt()
        val days = MathUtilities.roundUp(duration, args.onceEvery).toLong()

        val date = createDate(days, args)
        if(date.isAfter(currentTime)) return date
        return createDate(days + args.onceEvery, args)
    }

    private fun createDate(days: Long, schedule: Schedule): LocalDateTime {
        return schedule.beginTime.plusDays(days).plusHours(schedule.timeOfDay.hour.toLong())
            .plusMinutes(schedule.timeOfDay.minute.toLong())
    }
}