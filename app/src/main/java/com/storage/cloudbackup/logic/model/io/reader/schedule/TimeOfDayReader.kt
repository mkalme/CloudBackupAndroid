package com.storage.cloudbackup.logic.model.io.reader.schedule

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.schedule.TimeOfDay
import org.json.JSONObject

class TimeOfDayReader : Reader<TimeOfDay> {
    override fun read(data: JSONObject): TimeOfDay {
        val hour = data.getInt("Hour").toByte()
        val minute = data.getInt("Minute").toByte()

        return TimeOfDay(hour, minute)
    }
}