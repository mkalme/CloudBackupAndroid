package com.storage.cloudbackup.logic.model.resource

import android.content.Context
import com.storage.cloudbackup.logic.model.io.reader.SettingsReader
import com.storage.cloudbackup.logic.model.io.writer.SettingsWriter
import com.storage.cloudbackup.logic.model.item.settings.Settings
import com.storage.cloudbackup.logic.model.item.settings.language.container.LanguagePackContainer
import com.storage.cloudbackup.logic.model.item.settings.language.container.LanguagePackContainerReader
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.resource.JsonResourceObject
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.FileStreamProvider
import com.storage.cloudbackup.logic.shared.utilities.resource.stream.provider.StreamProvider
import org.json.JSONObject

class SettingsResource(context: Context) : JsonResourceObject() {
    lateinit var settings: Settings
    private val languagePackContainer: LanguagePackContainer

    override val streamProvider: StreamProvider
    override val propertyChangeListener: MutableList<EmptyEvent>
        get() {
            return settings.propertyChangedListener
        }

    init {
        languagePackContainer = LanguagePackContainerReader().readFromAssetsDirectory("language_packs", context)
        streamProvider = FileStreamProvider.fromLocalFile("settings.json", context)
        initialize()
    }

    override fun load(data: JSONObject) {
        settings = SettingsReader(languagePackContainer).read(data)
    }
    override fun save(): JSONObject {
        return SettingsWriter().write(settings)
    }

    override fun createDefault() {
        settings = Settings(languagePackContainer)
    }
}