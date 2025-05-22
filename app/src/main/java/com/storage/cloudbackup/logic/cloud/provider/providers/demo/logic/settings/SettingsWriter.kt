package com.storage.cloudbackup.logic.cloud.provider.providers.demo.logic.settings

import com.storage.cloudbackup.logic.model.io.writer.DataAmountWriter
import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import org.json.JSONArray
import org.json.JSONObject

class SettingsWriter : Writer<Settings> {
    private val dataAmountWriter: Writer<DataAmount> = DataAmountWriter()

    override fun write(input: Settings): JSONObject {
        val output = JSONObject()
        output.put("RootFolder", input.rootFolder)
        output.put("SimulatedUploadRate", dataAmountWriter.write(input.simulatedUploadRate))
        output.put("SimulatedLatencyMilliseconds", input.simulatedLatencyMilliseconds)
        output.put("IntroduceRandomErrors", input.introduceRandomErrors)
        output.put("TrackUploadedFiles", input.trackUploadedFiles)

        val uploadedFileArray = JSONArray()
        input.simulatedUploadedFiles.forEach {
            val fileArray = JSONArray()
            fileArray.put(it.name)
            fileArray.put(it.size)
            uploadedFileArray.put(fileArray)
        }
        output.put("UploadedFiles", uploadedFileArray)

        return output
    }
}