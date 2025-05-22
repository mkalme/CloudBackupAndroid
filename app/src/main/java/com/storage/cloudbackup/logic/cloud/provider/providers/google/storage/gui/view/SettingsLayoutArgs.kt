package com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.gui.view

import android.content.Context
import com.storage.cloudbackup.logic.cloud.provider.providers.google.storage.logic.settings.Settings
import com.storage.cloudbackup.logic.permit.edit.EditPermit
import com.storage.cloudbackup.logic.permit.language.LanguagePermit

data class SettingsLayoutArgs (
    val settings: Settings,
    val editPermit: EditPermit,
    val languagePermit: LanguagePermit,
    val context: Context
)
