package com.storage.cloudbackup.logic.model.io.reader.updateplan

import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.io.reader.Reader
import com.storage.cloudbackup.logic.model.item.history.SchemeHistoryContainer
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlan
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlanContainer
import org.json.JSONObject

class UpdatePlanContainerReader(val cloudProviders: List<CloudProvider>, schemeHistory: SchemeHistoryContainer) : Reader<UpdatePlanContainer> {
    private var updatePlanReader: Reader<UpdatePlan> = UpdatePlanReader(cloudProviders, schemeHistory)

    override fun read(data: JSONObject): UpdatePlanContainer {
        val output = UpdatePlanContainer()

        val updatePlans = data.getJSONArray("UpdatePlans")
        for(i in 0 until updatePlans.length()){
            output.updatePlans.add(updatePlanReader.read(updatePlans.getJSONObject(i)))
        }

        return output
    }
}