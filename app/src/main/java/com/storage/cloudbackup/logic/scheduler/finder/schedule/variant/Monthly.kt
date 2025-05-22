package com.storage.cloudbackup.logic.scheduler.finder.schedule.variant

import com.storage.cloudbackup.logic.model.item.schedule.MonthlySchedule
import com.storage.cloudbackup.logic.scheduler.finder.FindType
import com.storage.cloudbackup.logic.scheduler.finder.NearestUpdateTimeFinder
import com.storage.cloudbackup.logic.shared.utilities.utilities.MathUtilities
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class Monthly : NearestUpdateTimeFinder<MonthlySchedule> {
    override fun getNearestTime(args: MonthlySchedule, currentTime: LocalDateTime, findType: FindType): LocalDateTime {
        val date = getAfterDate(args, currentTime)

        if(findType == FindType.After) return date
        return date.minusMonths(args.onceEvery.toLong())
    }

    private fun getAfterDate(args: MonthlySchedule, currentTime: LocalDateTime): LocalDateTime{
        val duration = ChronoUnit.MONTHS.between(args.beginTime, currentTime).toInt()
        val months = MathUtilities.roundUp(duration, args.onceEvery).toLong()

        val date = createDate(months, args)
        if(date.isAfter(currentTime)) return date
        return createDate(months + args.onceEvery, args)
    }

    private fun createDate(months: Long, schedule: MonthlySchedule): LocalDateTime {
        val date = schedule.beginTime.plusMonths(months).plusHours(schedule.timeOfDay.hour.toLong())
            .plusMinutes(schedule.timeOfDay.minute.toLong())

        val maxDays = YearMonth.from(date).lengthOfMonth()
        var days = schedule.monthDay.toInt()
        days = if(days > maxDays) maxDays else days

        return date.plusDays(days.toLong() - 1)
    }
}