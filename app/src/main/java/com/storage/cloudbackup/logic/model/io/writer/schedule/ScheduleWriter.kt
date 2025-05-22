package com.storage.cloudbackup.logic.model.io.writer.schedule

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.schedule.DailySchedule
import com.storage.cloudbackup.logic.model.item.schedule.MonthlySchedule
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.schedule.ScheduleType
import com.storage.cloudbackup.logic.model.item.schedule.WeeklySchedule
import com.storage.cloudbackup.logic.model.item.schedule.YearlySchedule
import org.json.JSONObject

class ScheduleWriter : Writer<Schedule> {
    private var dailyScheduleWriter: Writer<DailySchedule> = DailyScheduleWriter()
    private var weeklyScheduleWriter: Writer<WeeklySchedule> = WeeklyScheduleWriter()
    private var monthlyScheduleWriter: Writer<MonthlySchedule> = MonthlyScheduleWriter()
    private var yearlyScheduleWriter: Writer<YearlySchedule> = YearlyScheduleWriter()

    override fun write(input: Schedule): JSONObject {
        return when(input.type) {
            ScheduleType.Daily -> dailyScheduleWriter.write(input as DailySchedule)
            ScheduleType.Weekly -> weeklyScheduleWriter.write(input as WeeklySchedule)
            ScheduleType.Monthly -> monthlyScheduleWriter.write(input as MonthlySchedule)
            ScheduleType.Yearly -> yearlyScheduleWriter.write(input as YearlySchedule)
        }
    }
}