package com.storage.cloudbackup.logic.model.io.writer.history

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.io.writer.history.attempt.UpdateAttemptWriter
import com.storage.cloudbackup.logic.model.item.history.SchemeHistory
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import org.json.JSONArray
import org.json.JSONObject

class SchemeHistoryWriter : Writer<SchemeHistory> {
    private val updateAttemptWriter: Writer<UpdateAttempt> = UpdateAttemptWriter()

    override fun write(input: SchemeHistory): JSONObject {
        val output = JSONObject()
        output.put("Id", input.id.toString())

        val attempts = JSONArray()
        input.updateAttempts.forEach{
            attempts.put(updateAttemptWriter.write(it))
        }

        output.put("Attempts", attempts)

        output.put("LastUpdated", input.lastUpdated.toString())

        return output
    }
}