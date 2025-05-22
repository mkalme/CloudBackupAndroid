package com.storage.cloudbackup.logic.scheduler.finder.schedule.variant

import com.storage.cloudbackup.logic.model.item.schedule.YearlySchedule
import com.storage.cloudbackup.logic.scheduler.finder.FindType
import com.storage.cloudbackup.logic.scheduler.finder.NearestUpdateTimeFinder
import com.storage.cloudbackup.logic.shared.utilities.utilities.MathUtilities
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Yearly : NearestUpdateTimeFinder<YearlySchedule> {
    override fun getNearestTime(args: YearlySchedule, currentTime: LocalDateTime, findType: FindType): LocalDateTime {
        val date = getAfterDate(args, currentTime)

        if(findType == FindType.After) return date
        return date.minusYears(args.onceEvery.toLong())
    }

    private fun getAfterDate(args: YearlySchedule, currentTime: LocalDateTime): LocalDateTime{
        val duration = ChronoUnit.YEARS.between(args.beginTime, currentTime).toInt()
        val years = MathUtilities.roundUp(duration, args.onceEvery).toLong()

        val date = createDate(years, args)
        if(date.isAfter(currentTime)) return date
        return createDate(years + args.onceEvery, args)
    }

    private fun createDate(years: Long, schedule: YearlySchedule): LocalDateTime {
        val date = schedule.beginTime.plusYears(years).plusHours(schedule.timeOfDay.hour.toLong()).plusMinutes(schedule.timeOfDay.minute.toLong())
        return date.plusMonths(schedule.month.toInt().toLong()).plusDays(schedule.monthDay.toLong() - 1)
    }
}