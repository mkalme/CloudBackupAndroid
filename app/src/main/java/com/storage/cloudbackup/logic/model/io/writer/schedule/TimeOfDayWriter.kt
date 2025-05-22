package com.storage.cloudbackup.logic.model.io.writer.schedule

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.schedule.TimeOfDay
import org.json.JSONObject

class TimeOfDayWriter : Writer<TimeOfDay> {
    override fun write(input: TimeOfDay): JSONObject {
        val output = JSONObject()
        output.put("Hour", input.hour.toInt())
        output.put("Minute", input.minute.toInt())

        return output
    }
}