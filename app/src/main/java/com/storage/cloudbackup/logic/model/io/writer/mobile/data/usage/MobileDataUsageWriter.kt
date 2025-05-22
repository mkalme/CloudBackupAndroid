package com.storage.cloudbackup.logic.model.io.writer.mobile.data.usage

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.mobile.data.usage.MobileDataUsage
import org.json.JSONObject

class MobileDataUsageWriter : Writer<MobileDataUsage> {
    override fun write(input: MobileDataUsage): JSONObject {
        val output = JSONObject()
        output.put("BytesUsage", input.bytesUsage)
        output.put("LastUpdated", input.lastUpdated.toString())

        return output
    }
}