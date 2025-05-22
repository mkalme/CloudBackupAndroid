package com.storage.cloudbackup.logic.model.io.reader

import com.storage.cloudbackup.logic.model.item.data.amount.DataAmount
import com.storage.cloudbackup.logic.model.item.data.amount.DataUnit
import org.json.JSONObject

class DataAmountReader : Reader<DataAmount> {
    override fun read(data: JSONObject): DataAmount {
        val dataUnit = DataUnit.valueOf(data.getString("Unit"))
        val amount = data.getLong("Amount").toULong()

        return DataAmount(dataUnit, amount)
    }
}