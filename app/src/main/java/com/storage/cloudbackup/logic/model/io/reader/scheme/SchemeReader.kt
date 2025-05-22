package com.storage.cloudbackup.logic.model.io.reader.scheme

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.io.reader.schedule.ScheduleReader
import com.storage.cloudbackup.logic.model.item.history.SchemeHistoryContainer
import com.storage.cloudbackup.logic.model.item.schedule.Schedule
import com.storage.cloudbackup.logic.model.item.scheme.FileSearcher
import com.storage.cloudbackup.logic.model.item.scheme.NetworkType
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import org.json.JSONObject
import java.util.UUID

class SchemeReader(private val schemeHistory: SchemeHistoryContainer) : Reader<Scheme> {
    private var fileFilterReader: Reader<FileSearcher> = FileFilterReader()
    private var scheduleReader: Reader<Schedule> = ScheduleReader()

    override fun read(data: JSONObject): Scheme {
        val output = Scheme(UUID.fromString(data.getString("Id")))
        output.name = data.getString("Name")
        output.driveFolder = data.getString("DriveFolder")
        output.fileFilter = fileFilterReader.read(data.getJSONObject("FileFilter"))
        output.networkType = NetworkType.valueOf(data.getString("NetworkType"))
        output.useFileSizeForComparison = data.getBoolean("UseFileSizeForComparison")

        val history = schemeHistory.getHistory(output)
        if(history != null){
            output.history = history
        }else{
            schemeHistory.schemes.add(output.history)
        }

        val schedules = data.getJSONArray("Schedules")
        for(i in 0 until schedules.length()){
            output.schedules.add(scheduleReader.read(schedules.getJSONObject(i)))
        }

        return output
    }
}