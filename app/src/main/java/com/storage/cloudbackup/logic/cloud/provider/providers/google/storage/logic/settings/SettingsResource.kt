package com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.logic.settings

import android.content.Context
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.resource.JsonResourceObject
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.FileStreamProvider
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.StreamProvider
import org.json.JSONObject

class SettingsResource(context: Context) : JsonResourceObject() {
    lateinit var settings: Settings

    override val streamProvider: StreamProvider
    override val propertyChangeListener: MutableList<EmptyEvent>
        get() {
            return settings.propertyChangedListener
        }

    init {
        streamProvider = FileStreamProvider.fromLocalFile("google_cloud_provider_settings.json", context)
        initialize()
    }

    override fun load(data: JSONObject) {
        settings = SettingsReader().read(data)
    }
    override fun save(): JSONObject {
        return SettingsWriter().write(settings)
    }

    override fun createDefault() {
        settings = Settings()
    }
}