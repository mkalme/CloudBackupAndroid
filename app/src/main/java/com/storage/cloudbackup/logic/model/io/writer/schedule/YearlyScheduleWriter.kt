package com.storage.cloudbackup.logic.model.io.writer.schedule

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.schedule.YearlySchedule
import org.json.JSONObject

class YearlyScheduleWriter : Writer<YearlySchedule> {
    private val timeOfDayWriter: TimeOfDayWriter = TimeOfDayWriter()

    override fun write(input: YearlySchedule): JSONObject {
        val output = JSONObject()
        output.put("OnceEvery", input.onceEvery)
        output.put("Month", input.month.toString())
        output.put("MonthDay", input.monthDay.toInt())
        output.put("TimeOfDay", timeOfDayWriter.write(input.timeOfDay))
        output.put("BeginTime", input.beginTime.year)
        output.put("Type", input.type.toString())

        return output
    }
}