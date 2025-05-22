package com.storage.cloudbackup.logic.model.io.reader.history

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.history.SchemeHistory
import com.storage.cloudbackup.logic.model.item.history.SchemeHistoryContainer
import org.json.JSONObject

class SchemeHistoryContainerReader : Reader<SchemeHistoryContainer> {
    private var schemeHistoryReader: Reader<SchemeHistory> = SchemeHistoryReader()

    override fun read(data: JSONObject): SchemeHistoryContainer {
        val output = SchemeHistoryContainer()

        val schemes = data.getJSONArray("Schemes")
        for(i in 0 until schemes.length()){
            output.schemes.add(schemeHistoryReader.read(schemes.getJSONObject(i)))
        }

        return output
    }
}