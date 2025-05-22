package com.storage.cloudbackup.logic.model.io.reader.updateplan

import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.io.reader.scheme.SchemeReader
import com.storage.cloudbackup.logic.model.item.history.SchemeHistoryContainer
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlan
import org.json.JSONObject

class UpdatePlanReader(val cloudProviders: List<CloudProvider>, schemeHistory: SchemeHistoryContainer) : Reader<UpdatePlan> {
    private var schemeReader: Reader<Scheme> = SchemeReader(schemeHistory)

    override fun read(data: JSONObject): UpdatePlan {
        val output = UpdatePlan()
        output.name = data.getString("Name")
        output.folder = data.getString("Folder")

        val schemes = data.getJSONArray("Schemes")
        for(i in 0 until schemes.length()){
            output.schemes.add(schemeReader.read(schemes.getJSONObject(i)))
        }

        val id = data.getString("CloudProvider")
        output.cloudProvider = cloudProviders.firstOrNull {
            it.logicComponent.info.id == id
        }

        return output
    }
}