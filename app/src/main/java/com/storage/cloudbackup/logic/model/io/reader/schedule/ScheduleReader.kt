package com.storage.cloudbackup.logic.model.io.reader.schedule

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.schedule.DailySchedule
import com.storage.cloudbackup.logic.model.item.schedule.MonthlySchedule
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.schedule.ScheduleType
import com.storage.cloudbackup.logic.model.item.schedule.WeeklySchedule
import com.storage.cloudbackup.logic.model.item.schedule.YearlySchedule
import org.json.JSONObject

class ScheduleReader : Reader<Schedule> {
    private var dailyScheduleReader: Reader<DailySchedule> = DailyScheduleReader()
    private var weeklyScheduleReader: Reader<WeeklySchedule> = WeeklyScheduleReader()
    private var monthlyScheduleReader: Reader<MonthlySchedule> = MonthlyScheduleReader()
    private var yearlyScheduleReader: Reader<YearlySchedule> = YearlyScheduleReader()

    override fun read(data: JSONObject): Schedule {
        return when(ScheduleType.valueOf(data.getString("Type"))) {
            ScheduleType.Daily -> dailyScheduleReader.read(data)
            ScheduleType.Weekly -> weeklyScheduleReader.read(data)
            ScheduleType.Monthly -> monthlyScheduleReader.read(data)
            ScheduleType.Yearly -> yearlyScheduleReader.read(data)
        }
    }
}