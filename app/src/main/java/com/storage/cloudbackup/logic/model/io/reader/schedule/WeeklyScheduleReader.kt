package com.storage.cloudbackup.logic.model.io.reader.schedule

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.schedule.TimeOfDay
import com.storage.cloudbackup.logic.model.item.schedule.WeekDay
import com.storage.cloudbackup.logic.model.item.schedule.WeeklySchedule
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeeklyScheduleReader : Reader<WeeklySchedule> {
    private val timeOfDayReader: Reader<TimeOfDay> = TimeOfDayReader()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun read(data: JSONObject): WeeklySchedule {
        val output = WeeklySchedule()
        output.onceEvery = data.getInt("OnceEvery")
        output.weekday = WeekDay.valueOf(data.getString("WeekDay"))
        output.timeOfDay = timeOfDayReader.read(data.getJSONObject("TimeOfDay"))
        output.setBeginDate(LocalDate.parse(data.getString("BeginTime"), formatter).atStartOfDay())

        return output
    }
}