package com.storage.cloudbackup.logic.model.resource

import android.content.Context
import com.storage.cloudbackup.logic.model.io.reader.history.SchemeHistoryContainerReader
import com.storage.cloudbackup.logic.model.io.writer.history.SchemeHistoryContainerWriter
import com.storage.cloudbackup.logic.model.item.history.SchemeHistoryContainer
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.resource.JsonResourceObject
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.FileStreamProvider
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.StreamProvider
import org.json.JSONObject

class SchemeHistoryContainerResource(context: Context) : JsonResourceObject() {
    lateinit var schemeHistoryContainer: SchemeHistoryContainer

    override val streamProvider: StreamProvider
    override val propertyChangeListener: MutableList<EmptyEvent>
        get() {
            return schemeHistoryContainer.propertyChangedListener
        }

    init {
        streamProvider = FileStreamProvider.fromLocalFile("scheme_history.json", context)
        initialize()
    }

    override fun load(data: JSONObject) {
        schemeHistoryContainer = SchemeHistoryContainerReader().read(data)
    }
    override fun save(): JSONObject {
        return SchemeHistoryContainerWriter().write(schemeHistoryContainer)
    }

    override fun createDefault() {
        schemeHistoryContainer = SchemeHistoryContainer()
    }
}