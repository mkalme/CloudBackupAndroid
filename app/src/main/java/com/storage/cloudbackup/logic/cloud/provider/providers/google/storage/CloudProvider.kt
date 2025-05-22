package com.storage.cloudbackup.logic.cloud.provider.providers.google.storage

import android.content.Context
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.cloud.provider.model.GuiComponent
import com.storage.cloudbackup.logic.cloud.provider.model.LogicComponent
import com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.gui.CloudGuiComponent
import com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.logic.CloudLogicComponent
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit

class CloudProvider(editPermit: EditPermit, languagePermit: LanguagePermit, context: Context) : CloudProvider {
    override val guiComponent: GuiComponent
    override val logicComponent: LogicComponent

    init {
        logicComponent = CloudLogicComponent(context)
        guiComponent = CloudGuiComponent(logicComponent, editPermit, languagePermit, context)
    }
}