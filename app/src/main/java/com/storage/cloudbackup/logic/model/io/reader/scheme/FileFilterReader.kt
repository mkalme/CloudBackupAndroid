package com.storage.cloudbackup.logic.model.io.reader.scheme

import com.storage.cloudbackup.logic.model.io.reader.DataAmountReader
import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.scheme.FileSearcher
import org.json.JSONObject

class FileFilterReader : Reader<FileSearcher> {
    private var dataAmountReader: Reader<DataAmount> = DataAmountReader()

    override fun read(data: JSONObject): FileSearcher {
        val output = FileSearcher()
        output.fileNameFilter = data.getString("FileNameFilter")
        output.minFileSize = dataAmountReader.read(data.getJSONObject("MinFileSize"))
        output.maxFileSize = dataAmountReader.read(data.getJSONObject("MaxFileSize"))

        return output
    }
}