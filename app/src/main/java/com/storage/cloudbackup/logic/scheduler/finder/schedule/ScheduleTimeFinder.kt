package com.storage.cloudbackup.logic.scheduler.finder.schedule

import com.storage.cloudbackup.logic.model.item.schedule.DailySchedule
import com.storage.cloudbackup.logic.model.item.schedule.MonthlySchedule
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.schedule.WeeklySchedule
import com.storage.cloudbackup.logic.model.item.schedule.YearlySchedule
import com.storage.cloudbackup.logic.scheduler.finder.FindType
import com.storage.cloudbackup.logic.scheduler.finder.NearestUpdateTimeFinder
import com.storage.cloudbackup.logic.scheduler.finder.schedule.variant.Daily
import com.storage.cloudbackup.logic.scheduler.finder.schedule.variant.Monthly
import com.storage.cloudbackup.logic.scheduler.finder.schedule.variant.Weekly
import com.storage.cloudbackup.logic.scheduler.finder.schedule.variant.Yearly
import java.time.LocalDateTime

class ScheduleTimeFinder : NearestUpdateTimeFinder<Schedule> {
    private val dailyTimeFinder: NearestUpdateTimeFinder<DailySchedule> = Daily()
    private val weeklyTimeFinder: NearestUpdateTimeFinder<WeeklySchedule> = Weekly()
    private val monthlyTimeFinder: NearestUpdateTimeFinder<MonthlySchedule> = Monthly()
    private val yearlyTimeFinder: NearestUpdateTimeFinder<YearlySchedule> = Yearly()

    override fun getNearestTime(args: Schedule, currentTime: LocalDateTime, findType: FindType): LocalDateTime {
        return when(args){
            is DailySchedule -> dailyTimeFinder.getNearestTime(args, currentTime, findType)
            is WeeklySchedule -> weeklyTimeFinder.getNearestTime(args, currentTime, findType)
            is MonthlySchedule -> monthlyTimeFinder.getNearestTime(args, currentTime, findType)
            is YearlySchedule -> yearlyTimeFinder.getNearestTime(args, currentTime, findType)
            else -> LocalDateTime.MIN
        }
    }
}