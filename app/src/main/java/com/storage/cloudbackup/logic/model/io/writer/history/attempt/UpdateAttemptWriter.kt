package com.storage.cloudbackup.logic.model.io.writer.history.attempt

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import org.json.JSONArray
import org.json.JSONObject

class UpdateAttemptWriter : Writer<UpdateAttempt> {
    override fun write(input: UpdateAttempt): JSONObject {
        val output = JSONObject()
        output.put("Begin", input.begin.toString())
        output.put("Finished", input.finished.toString())
        output.put("Result", input.result.value)
        output.put("LocalDirectory", input.localDirectory)
        output.put("DriveDirectory", input.driveDirectory)
        output.put("CloudProviderId", input.cloudProviderId)
        output.put("IsAutomatic", input.isAutomatic)

        val filesUploadedArray = JSONArray()
        input.filesUploaded.forEach { filesUploadedArray.put(it) }
        output.put("FilesUploaded", filesUploadedArray)

        val unsuccessfulFilesUploadedArray = JSONArray()
        input.unsuccessfulFilesUploaded.forEach { unsuccessfulFilesUploadedArray.put(it) }
        output.put("UnsuccessfulFilesUploaded", unsuccessfulFilesUploadedArray)

        val schemeErrorMessageArray = JSONArray()
        input.schemeErrorMessages.forEach { schemeErrorMessageArray.put(it) }
        output.put("SchemeErrorMessages", schemeErrorMessageArray)

        return output
    }
}