package com.storage.cloudbackup.logic.scheduler.finder.schedule.variant

import com.storage.cloudbackup.logic.model.item.schedule.WeeklySchedule
import com.storage.cloudbackup.logic.scheduler.finder.FindType
import com.storage.cloudbackup.logic.scheduler.finder.NearestUpdateTimeFinder
import com.storage.cloudbackup.logic.shared.utilities.utilities.MathUtilities
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Weekly : NearestUpdateTimeFinder<WeeklySchedule> {
    override fun getNearestTime(args: WeeklySchedule, currentTime: LocalDateTime, findType: FindType): LocalDateTime {
        val date = getAfterDate(args, currentTime)

        if(findType == FindType.After) return date
        return date.minusWeeks(args.onceEvery.toLong())
    }

    private fun getAfterDate(args: WeeklySchedule, currentTime: LocalDateTime): LocalDateTime{
        val duration = ChronoUnit.WEEKS.between(args.beginTime, currentTime).toInt()
        val weeks = MathUtilities.roundUp(duration, args.onceEvery).toLong()

        val date = createDate(weeks, args)
        if(date.isAfter(currentTime)) return date
        return createDate(weeks + args.onceEvery, args)
    }

    private fun createDate(weeks: Long, schedule: WeeklySchedule): LocalDateTime {
        return schedule.beginTime.plusWeeks(weeks).plusDays(schedule.weekday.toInt().toLong())
            .plusHours(schedule.timeOfDay.hour.toLong()).plusMinutes(schedule.timeOfDay.minute.toLong())
    }
}