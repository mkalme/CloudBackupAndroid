package com.storage.cloudbackup.logic.model.io.writer.schedule

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.schedule.WeeklySchedule
import org.json.JSONObject
import java.time.format.DateTimeFormatter

class WeeklyScheduleWriter : Writer<WeeklySchedule> {
    private val timeOfDayWriter: TimeOfDayWriter = TimeOfDayWriter()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun write(input: WeeklySchedule): JSONObject {
        val dayOfWeek = input.beginTime.dayOfWeek

        val output = JSONObject()
        output.put("OnceEvery", input.onceEvery)
        output.put("WeekDay", input.weekday.toString())
        output.put("TimeOfDay", timeOfDayWriter.write(input.timeOfDay))
        output.put("BeginTime", input.beginTime.minusDays((dayOfWeek.value - 1).toLong()).format(formatter))
        output.put("Type", input.type.toString())

        return output
    }
}