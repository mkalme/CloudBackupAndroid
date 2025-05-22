package com.storage.cloudbackup.logic.model.io.writer.scheme

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.io.writer.schedule.ScheduleWriter
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.scheme.FileSearcher
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import org.json.JSONArray
import org.json.JSONObject

class SchemeWriter : Writer<Scheme> {
    private var fileFilterWriter: Writer<FileSearcher> = FileFilterWriter()
    private var scheduleWriter: Writer<Schedule> = ScheduleWriter()

    override fun write(input: Scheme): JSONObject {
        val schedules = JSONArray()

        input.schedules.forEach{
            schedules.put(scheduleWriter.write(it))
        }

        val output = JSONObject()
        output.put("Name", input.name)
        output.put("DriveFolder", input.driveFolder)
        output.put("FileFilter", fileFilterWriter.write(input.fileFilter))
        output.put("NetworkType", input.networkType)
        output.put("UseFileSizeForComparison", input.useFileSizeForComparison)
        output.put("Schedules", schedules)
        output.put("Id", input.id.toString())

        return output
    }
}