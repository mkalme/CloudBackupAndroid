package com.storage.cloudbackup.gui.view.activity.cloud.provider

import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider

data class SettingsActivityArgs(
    val cloudProvider: CloudProvider,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit
)
