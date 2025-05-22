package com.storage.cloudbackup.logic.model.io.writer.scheme

import com.storage.cloudbackup.logic.model.io.writer.DataAmountWriter
import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.scheme.FileSearcher
import org.json.JSONObject

class FileFilterWriter : Writer<FileSearcher> {
    private var dataAmountWriter: Writer<DataAmount> = DataAmountWriter()

    override fun write(input: FileSearcher): JSONObject {
        val output = JSONObject()
        output.put("FileNameFilter", input.fileNameFilter)
        output.put("MinFileSize", dataAmountWriter.write(input.minFileSize))
        output.put("MaxFileSize", dataAmountWriter.write(input.maxFileSize))

        return output
    }
}