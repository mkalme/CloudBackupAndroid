package com.storage.cloudbackup.logic.model.resource

import android.content.Context
import com.storage.cloudbackup.logic.model.io.reader.mobile.data.usage.MobileDataUsageReader
import com.storage.cloudbackup.logic.model.io.writer.mobile.data.usage.MobileDataUsageWriter
import com.storage.cloudbackup.logic.model.item.mobile.data.usage.MobileDataUsage
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.resource.JsonResourceObject
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.FileStreamProvider
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.StreamProvider
import org.json.JSONObject

class MobileDataUsageResource(context: Context) : JsonResourceObject() {
    lateinit var mobileDataUsage: MobileDataUsage

    override val streamProvider: StreamProvider
    override val propertyChangeListener: MutableList<EmptyEvent>
        get() {
            return mobileDataUsage.propertyChangedListener
        }

    init {
        streamProvider = FileStreamProvider.fromLocalFile("mobile_data_usage.json", context)
        initialize()
    }

    override fun load(data: JSONObject) {
        mobileDataUsage = MobileDataUsageReader().read(data)
    }
    override fun save(): JSONObject {
        return MobileDataUsageWriter().write(mobileDataUsage)
    }

    override fun createDefault() {
        mobileDataUsage = MobileDataUsage()
    }
}