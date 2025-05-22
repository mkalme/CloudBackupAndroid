package com.storage.cloudbackup.logic.cloud.provider.providers.s3

import android.content.Context
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.cloud.provider.model.GuiComponent
import com.storage.cloudbackup.logic.cloud.provider.model.LogicComponent
import com.storage.cloudbackup.logic.cloud.provider.providers.s3.gui.CloudGuiComponent
import com.storage.cloudbackup.logic.cloud.provider.providers.s3.logic.CloudLogicComponent

class CloudProvider(editPermit: EditPermit, languagePermit: LanguagePermit, context: Context) : CloudProvider {
    override val guiComponent: GuiComponent
    override val logicComponent: LogicComponent

    init {
        logicComponent = CloudLogicComponent(context)
        guiComponent = CloudGuiComponent(logicComponent, editPermit, languagePermit, context)
    }
}