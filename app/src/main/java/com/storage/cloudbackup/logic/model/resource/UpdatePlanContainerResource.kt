package com.storage.cloudbackup.logic.model.resource

import android.content.Context
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.io.reader.updateplan.UpdatePlanContainerReader
import com.storage.cloudbackup.logic.model.io.writer.updateplan.UpdatePlanContainerWriter
import com.storage.cloudbackup.logic.model.item.history.SchemeHistoryContainer
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlanContainer
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.resource.JsonResourceObject
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.FileStreamProvider
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.StreamProvider
import org.json.JSONObject

class UpdatePlanContainerResource(context: Context, val cloudProviders: List<CloudProvider>, private val schemeHistory: SchemeHistoryContainer) : JsonResourceObject() {
    lateinit var updatePlanContainer: UpdatePlanContainer

    override val streamProvider: StreamProvider
    override val propertyChangeListener: MutableList<EmptyEvent>
        get() {
            return updatePlanContainer.propertyChangedListener
        }

    init {
        streamProvider = FileStreamProvider.fromLocalFile("update_plans.json", context)
        initialize()
    }

    override fun load(data: JSONObject) {
        updatePlanContainer = UpdatePlanContainerReader(cloudProviders, schemeHistory).read(data)
    }
    override fun save(): JSONObject {
        return UpdatePlanContainerWriter().write(updatePlanContainer)
    }

    override fun createDefault() {
        updatePlanContainer = UpdatePlanContainer()
    }
}