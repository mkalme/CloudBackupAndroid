package com.storage.cloudbackup.logic.model.io.reader.schedule

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.schedule.DailySchedule
import com.storage.cloudbackup.logic.model.item.schedule.TimeOfDay
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyScheduleReader : Reader<DailySchedule> {
    private val timeOfDayReader: Reader<TimeOfDay> = TimeOfDayReader()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun read(data: JSONObject): DailySchedule {
        val output = DailySchedule()
        output.onceEvery = data.getInt("OnceEvery")
        output.timeOfDay = timeOfDayReader.read(data.getJSONObject("TimeOfDay"))
        output.setBeginDate(LocalDate.parse(data.getString("BeginTime"), formatter).atStartOfDay())

        return output
    }
}