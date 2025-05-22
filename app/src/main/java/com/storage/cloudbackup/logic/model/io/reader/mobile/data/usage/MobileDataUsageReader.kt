package com.storage.cloudbackup.logic.model.io.reader.mobile.data.usage

import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.mobile.data.usage.MobileDataUsage
import org.json.JSONObject
import java.time.LocalDateTime

class MobileDataUsageReader : Reader<MobileDataUsage> {
    override fun read(data: JSONObject): MobileDataUsage {
        val output = MobileDataUsage()
        output.bytesUsage = data.getLong("BytesUsage")
        output.lastUpdated = LocalDateTime.parse(data.getString("LastUpdated"))

        return output
    }
}