package com.storage.cloudbackup.logic.model.io.writer.schedule

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.schedule.MonthlySchedule
import org.json.JSONObject
import java.time.format.DateTimeFormatter

class MonthlyScheduleWriter : Writer<MonthlySchedule> {
    private val timeOfDayWriter: TimeOfDayWriter = TimeOfDayWriter()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun write(input: MonthlySchedule): JSONObject {
        val output = JSONObject()
        output.put("OnceEvery", input.onceEvery)
        output.put("MonthDay", input.monthDay)
        output.put("TimeOfDay", timeOfDayWriter.write(input.timeOfDay))
        output.put("BeginTime", input.beginTime.format(formatter))
        output.put("Type", input.type.toString())

        return output
    }
}