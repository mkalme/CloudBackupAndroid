package com.storage.cloudbackup.logic.model.io.writer.updateplan

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlan
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlanContainer
import org.json.JSONArray
import org.json.JSONObject

class UpdatePlanContainerWriter : Writer<UpdatePlanContainer> {
    private var updatePlanWriter: Writer<UpdatePlan> = UpdatePlanWriter()

    override fun write(input: UpdatePlanContainer): JSONObject {
        val updatePlans = JSONArray()

        input.updatePlans.forEach{
            updatePlans.put(updatePlanWriter.write(it))
        }

        val output = JSONObject()
        output.put("UpdatePlans", updatePlans)

        return output
    }
}