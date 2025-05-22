package com.storage.cloudbackup.logic.model.io.reader.schedule

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.schedule.Month
import com.storage.cloudbackup.logic.model.item.schedule.TimeOfDay
import com.storage.cloudbackup.logic.model.item.schedule.YearlySchedule
import org.json.JSONObject
import java.time.LocalDateTime

class YearlyScheduleReader : Reader<YearlySchedule> {
    private var timeOfDayReader: Reader<TimeOfDay> = TimeOfDayReader()

    override fun read(data: JSONObject): YearlySchedule {
        val output = YearlySchedule()
        output.onceEvery = data.getInt("OnceEvery")
        output.month = Month.valueOf(data.getString("Month"))
        output.monthDay = data.getInt("MonthDay").toByte()
        output.timeOfDay = timeOfDayReader.read(data.getJSONObject("TimeOfDay"))
        output.setBeginDate(LocalDateTime.of(data.getInt("BeginTime"), 1, 1, 0, 0))

        return output
    }
}