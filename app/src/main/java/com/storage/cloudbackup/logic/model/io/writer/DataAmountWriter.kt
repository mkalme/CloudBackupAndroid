package com.storage.cloudbackup.logic.model.io.writer

import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import org.json.JSONObject

class DataAmountWriter : Writer<DataAmount> {
    override fun write(input: DataAmount): JSONObject {
        val output = JSONObject()
        output.put("Unit", input.unit.toString())
        output.put("Amount", input.amount)

        return output
    }
}