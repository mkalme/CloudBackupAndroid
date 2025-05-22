package com.storage.cloudbackup.logic.model.io.writer.updateplan

import com.storage.cloudbackup.logic.model.io.writer.Writer
import com.storage.cloudbackup.logic.model.io.writer.scheme.SchemeWriter
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlan
import org.json.JSONArray
import org.json.JSONObject

class UpdatePlanWriter  : Writer<UpdatePlan> {
    private var schemeWriter: Writer<Scheme> = SchemeWriter()

    override fun write(input: UpdatePlan): JSONObject {
        val schemes = JSONArray()

        input.schemes.forEach {
            schemes.put(schemeWriter.write(it))
        }

        val output = JSONObject()
        output.put("Name", input.name)
        output.put("Folder", input.folder)
        output.put("Schemes", schemes)
        output.put("CloudProvider", input.cloudProvider?.logicComponent?.info?.id ?: "")

        return output
    }
}
