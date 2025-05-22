package com.storage.cloudbackup.logic.model.io.reader.history

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.io.reader.history.attempt.UpdateAttemptReader
import com.storage.cloudbackup.logic.model.item.history.SchemeHistory
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import org.json.JSONObject
import java.time.LocalDateTime
import java.util.UUID

class SchemeHistoryReader : Reader<SchemeHistory> {
    private val updateAttemptReader: Reader<UpdateAttempt> = UpdateAttemptReader()

    override fun read(data: JSONObject): SchemeHistory {
        val output = SchemeHistory(UUID.fromString(data.getString("Id")))

        val attempts = data.getJSONArray("Attempts")
        for(i in 0 until attempts.length()){
            output.updateAttempts.add(updateAttemptReader.read(attempts.getJSONObject(i)))
        }

        output.lastUpdated = LocalDateTime.parse(data.getString("LastUpdated"))

        return output
    }
}