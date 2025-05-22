package com.storage.cloudbackup.logic.model.io.reader.history.attempt

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.history.attempt.AttemptResult
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import org.json.JSONObject
import java.time.LocalDateTime

class UpdateAttemptReader : Reader<UpdateAttempt> {
    override fun read(data: JSONObject): UpdateAttempt {
        val output = UpdateAttempt()
        output.begin = LocalDateTime.parse(data.getString("Begin"))
        output.finished = LocalDateTime.parse(data.getString("Finished"))
        output.result = AttemptResult.valueOf(data.getString("Result"))
        output.localDirectory = data.getString("LocalDirectory")
        output.driveDirectory = data.getString("DriveDirectory")
        output.cloudProviderId = data.getString("CloudProviderId")
        output.isAutomatic = data.getBoolean("IsAutomatic")

        val filesUploadedArray = data.getJSONArray("FilesUploaded")
        for(i in 0 until filesUploadedArray.length()){
            output.filesUploaded.add(filesUploadedArray.getString(i))
        }

        val unsuccessfulFilesUploadedArray = data.getJSONArray("UnsuccessfulFilesUploaded")
        for(i in 0 until unsuccessfulFilesUploadedArray.length()){
            output.unsuccessfulFilesUploaded.add(unsuccessfulFilesUploadedArray.getString(i))
        }

        val schemeErrorMessageArray = data.getJSONArray("SchemeErrorMessages")
        for(i in 0 until schemeErrorMessageArray.length()){
            output.schemeErrorMessages.add(schemeErrorMessageArray.getString(i))
        }

        return output
    }
}