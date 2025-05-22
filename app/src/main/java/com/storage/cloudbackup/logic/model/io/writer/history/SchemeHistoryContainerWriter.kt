package com.storage.cloudbackup.logic.model.io.writer.history

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.history.SchemeHistory
import com.storage.cloudbackup.logic.model.item.history.SchemeHistoryContainer
import org.json.JSONArray
import org.json.JSONObject

class SchemeHistoryContainerWriter : Writer<SchemeHistoryContainer> {
    private var schemeHistoryWriter: Writer<SchemeHistory> = SchemeHistoryWriter()

    override fun write(input: SchemeHistoryContainer): JSONObject {
        val schemes = JSONArray()

        input.schemes.forEach{
            schemes.put(schemeHistoryWriter.write(it))
        }

        val output = JSONObject()
        output.put("Schemes", schemes)

        return output
    }
}