package com.storage.cloudbackup.logic.cloud.provider.providers.s3.gui

import android.content.Context
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProviderSettings
import com.storage.cloudbackup.logic.cloud.provider.model.GuiComponent
import com.storage.cloudbackup.logic.cloud.provider.providers.s3.gui.view.SettingsLayoutArgs
import com.storage.cloudbackup.logic.cloud.provider.providers.s3.gui.view.ViewSettings
import com.storage.cloudbackup.logic.cloud.provider.providers.s3.logic.CloudLogicComponent
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit

class CloudGuiComponent(private val logicComponent: CloudLogicComponent, private val editPermit: EditPermit, private val languagePermit: LanguagePermit, private val context: Context) : GuiComponent {
    private var _settings: ViewSettings? = null
    override val settings: CloudProviderSettings?
        get() = _settings

    override fun load() {
        _settings = ViewSettings(SettingsLayoutArgs(logicComponent.settingsResource?.settings!!, editPermit, languagePermit, context))
    }
}