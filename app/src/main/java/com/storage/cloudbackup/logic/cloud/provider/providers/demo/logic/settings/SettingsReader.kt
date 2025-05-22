package com.storage.cloudbackup.logic.cloud.provider.providers.demo.logic.settings

import com.storage.cloudbackup.logic.cloud.provider.model.upload.File
import com.storage.cloudbackup.logic.model.io.reader.DataAmountReader
import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import org.json.JSONObject

class SettingsReader : Reader<Settings> {
    private val dataAmountReader: Reader<DataAmount> = DataAmountReader()

    override fun read(data: JSONObject): Settings {
        val output = Settings()
        output.rootFolder = data.getString("RootFolder")
        output.simulatedUploadRate = dataAmountReader.read(data.getJSONObject("SimulatedUploadRate"))
        output.simulatedLatencyMilliseconds = data.getInt("SimulatedLatencyMilliseconds")
        output.introduceRandomErrors = data.getBoolean("IntroduceRandomErrors")
        output.trackUploadedFiles = data.getBoolean("TrackUploadedFiles")

        val uploadedFilesArray = data.getJSONArray("UploadedFiles")
        for(i in 0 until uploadedFilesArray.length()){
            val fileArray = uploadedFilesArray.getJSONArray(i)
            val file = File(fileArray.getString(0), fileArray.getLong(1).toULong())
            output.simulatedUploadedFiles.add(file)
        }

        return output
    }
}